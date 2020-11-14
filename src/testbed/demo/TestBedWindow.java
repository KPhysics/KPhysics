package testbed.demo;

import library.collision.AABB;
import library.dynamics.Body;
import library.dynamics.Ray;
import library.dynamics.ShadowCasting;
import library.dynamics.World;
import library.explosions.Explosion;
import library.explosions.ParticleExplosion;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.joints.Joint;
import library.utils.Settings;
import testbed.ColourSettings;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.Trail;
import testbed.demo.input.*;
import testbed.demo.tests.BouncingBall;
import testbed.demo.tests.Car;
import testbed.demo.tests.Chains;
import testbed.demo.tests.Raycast;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class TestBedWindow extends JPanel implements Runnable {
    private final Camera CAMERA;

    public void setCamera(Vectors2D centre, double zoom) {
        CAMERA.setCentre(centre);
        CAMERA.setZoom(zoom);
    }

    public Camera getCamera() {
        return CAMERA;
    }

    private final boolean ANTIALIASING;
    private final Thread PHYSICS_THREAD;

    //Input handler classes
    private final KeyBoardInput KEY_INPUT;
    private final MouseInput MOUSE_INPUT;
    private final MouseScroll MOUSE_SCROLL_INPUT;
    private final MouseMotionListener MOUSE_MOTION_INPUT;

    public TestBedWindow(boolean antiAliasing) {
        this.ANTIALIASING = antiAliasing;

        PHYSICS_THREAD = new Thread(this);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        CAMERA = new Camera((int) screenSize.getWidth(), (int) screenSize.getHeight(), this);

        MOUSE_INPUT = new MouseInput(this);
        addMouseListener(MOUSE_INPUT);

        KEY_INPUT = new KeyBoardInput(this);
        addKeyListener(KEY_INPUT);

        MOUSE_SCROLL_INPUT = new MouseScroll(this);
        addMouseWheelListener(MOUSE_SCROLL_INPUT);

        MOUSE_MOTION_INPUT = new MouseMovement(this);
        addMouseMotionListener(MOUSE_MOTION_INPUT);
    }

    public void startThread() {
        PHYSICS_THREAD.start();
    }

    public ArrayList<Ray> rays = new ArrayList<>();

    public void add(Ray ray) {
        rays.add(ray);
    }

    public ArrayList<Explosion> explosionObj = new ArrayList<>();

    public ArrayList<Explosion> getProximityExp() {
        return explosionObj;
    }

    public void add(Explosion ex) {
        explosionObj.add(ex);
    }

    public ArrayList<ParticleExplosion> particles = new ArrayList<>();

    public void add(ParticleExplosion p, double lifespan) {
        particles.add(p);
        for (Body b : p.getParticles()) {
            trailsToBodies.add(new Trail(1000, 1, b, lifespan));
        }
    }

    private World world = new World();

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public ArrayList<Trail> trailsToBodies = new ArrayList<>();

    public void add(Trail trail) {
        trailsToBodies.add(trail);
    }

    private boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    public void stop() {
        running = false;
        PHYSICS_THREAD.interrupt();
    }

    public void pause() {
        paused = true;
    }

    public boolean isPaused() {
        return paused;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    private void updateProximityCast() {
        for (Explosion p : explosionObj) {
            p.update(world.bodies);
        }
    }

    private void updateRays() {
        for (Ray r : rays) {
            if (Raycast.active) {
                Raycast.action(r);
            }
            r.updateProjection(world.bodies);
        }
        for (ShadowCasting s : shadowCastings) {
            s.updateProjections(world.bodies);
        }
    }

    private void updateTrails() {
        for (Trail t : trailsToBodies) {
            t.updateTrail();
        }
    }

    @Override
    public void run() {
        while (running) {
            synchronized (pauseLock) {
                if (!running) {
                    break;
                }
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait();
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                    if (!running) {
                        break;
                    }
                }
            }
            repaint();
        }
    }

    private void update() {
        double dt = Settings.HERTZ > 0.0 ? 1.0 / Settings.HERTZ : 0.0;
        world.step(dt);
        updateTrails();
        updateProximityCast();
        updateRays();
        checkParticleLifetime(dt);
    }

    private void checkParticleLifetime(double timePassed) {
        ArrayList<Body> bodiesToRemove = new ArrayList<>();
        Iterator<Trail> i = trailsToBodies.iterator();
        while (i.hasNext()) {
            Trail s = i.next();
            if (s.checkLifespan(timePassed)) {
                bodiesToRemove.add(s.getBody());
                i.remove();
            }
        }
        Iterator<ParticleExplosion> p = particles.iterator();
        while (p.hasNext()) {
            Body[] s = p.next().getParticles();
            if (containsBody(s, bodiesToRemove)) {
                removeParticlesFromWorld(s);
                p.remove();
            }
        }
    }

    private void removeParticlesFromWorld(Body[] s) {
        for (Body b : s) {
            world.removeBody(b);
        }
    }

    private boolean containsBody(Body[] s, ArrayList<Body> bodiesToRemove) {
        for (Body a : s) {
            if (bodiesToRemove.contains(a)) {
                return true;
            }
        }
        return false;
    }

    public void clearTestbedObjects() {
        CAMERA.reset();
        world.clearWorld();
        trailsToBodies.clear();
        rays.clear();
        explosionObj.clear();
        shadowCastings.clear();
        repaint();
    }

    public final ColourSettings PAINT_SETTINGS = new ColourSettings();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (ANTIALIASING) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        setBackground(PAINT_SETTINGS.background);
        update();
        if (PAINT_SETTINGS.getDrawGrid()) {
            drawGridMethod(g2d);
        }
        drawTrails(g2d);
        for (Body b : world.bodies) {
            if (PAINT_SETTINGS.getDrawShapes()) {
                b.shape.draw(g2d, PAINT_SETTINGS, CAMERA);
            }
            if (PAINT_SETTINGS.getDrawAABBs()) {
                b.shape.drawAABB(g2d, PAINT_SETTINGS, CAMERA);
            }
            if (PAINT_SETTINGS.getDrawContactPoints()) {
                world.drawContact(g2d, PAINT_SETTINGS, CAMERA);
            }
            if (PAINT_SETTINGS.getDrawCOMs()) {
                b.shape.drawCOMS(g2d, PAINT_SETTINGS, CAMERA);
            }
        }
        if (PAINT_SETTINGS.getDrawJoints()) {
            for (Joint j : world.joints) {
                j.draw(g2d, PAINT_SETTINGS, CAMERA);
            }
        }
        for (Explosion p : explosionObj) {
            p.draw(g2d, PAINT_SETTINGS, CAMERA);
        }
        for (Ray r : rays) {
            r.draw(g2d, PAINT_SETTINGS, CAMERA);
        }
        for (ShadowCasting s : shadowCastings) {
            s.draw(g2d, PAINT_SETTINGS, CAMERA);
        }
    }

    private void drawGridMethod(Graphics2D g2d) {
        int projection = 20000;
        int spacing = 10;
        int minXY = -projection;
        int maxXY = projection;
        int totalProjectionDistance = projection + projection;
        g2d.setColor(PAINT_SETTINGS.gridLines);
        for (int i = 0; i <= totalProjectionDistance; i += spacing) {
            if (i == projection) {
                g2d.setStroke(PAINT_SETTINGS.axisStrokeWidth);
                g2d.setColor(PAINT_SETTINGS.gridAxis);
            }

            Vectors2D currentMinY = CAMERA.convertToScreen(new Vectors2D(minXY + i, minXY));
            Vectors2D currentMaxY = CAMERA.convertToScreen(new Vectors2D(minXY + i, maxXY));
            g2d.draw(new Line2D.Double(currentMinY.x, currentMinY.y, currentMaxY.x, currentMaxY.y));

            Vectors2D currentMinX = CAMERA.convertToScreen(new Vectors2D(minXY, minXY + i));
            Vectors2D currentMaxX = CAMERA.convertToScreen(new Vectors2D(maxXY, minXY + i));
            g2d.draw(new Line2D.Double(currentMinX.x, currentMinX.y, currentMaxX.x, currentMaxX.y));

            if (i == projection) {
                g2d.setStroke(PAINT_SETTINGS.defaultStrokeWidth);
                g2d.setColor(PAINT_SETTINGS.gridLines);
            }
        }
    }

    private void drawTrails(Graphics2D g) {
        g.setColor(PAINT_SETTINGS.trail);
        for (Trail t : trailsToBodies) {
            Path2D.Double s = new Path2D.Double();
            for (int i = 0; i < t.getTrailPoints().length; i++) {
                Vectors2D v = t.getTrailPoints()[i];
                if (v == null) {
                    break;
                } else {
                    v = CAMERA.convertToScreen(v);
                    if (i == 0) {
                        s.moveTo(v.x, v.y);
                    } else {
                        s.lineTo(v.x, v.y);
                    }
                }
            }
            g.draw(s);
        }
    }

    public static void showWindow(TestBedWindow gameScreen, String title, int windowWidth, int windowHeight) {
        if (gameScreen != null) {
            JFrame window = new JFrame(title);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.add(gameScreen);
            window.setMinimumSize(new Dimension(800, 600));
            window.setPreferredSize(new Dimension(windowWidth, windowHeight));
            window.pack();
            window.setLocationRelativeTo(null);
            gameScreen.setFocusable(true);
            gameScreen.setOpaque(true);
            gameScreen.setBackground(gameScreen.PAINT_SETTINGS.background);

            JMenuBar menuBar = new JMenuBar();
            menuBar.add(createTestMenu(gameScreen));
            menuBar.add(createColourSchemeMenu(gameScreen));
            menuBar.add(createFrequencyMenu(gameScreen));
            window.setJMenuBar(menuBar);

            window.setVisible(true);
        }
    }

    private static Component createFrequencyMenu(TestBedWindow gameScreen) {
        JMenu hertzMenu = new JMenu("Hertz");
        int number = 30;
        for (int i = 1; i < 5; i++) {
            JMenuItem hertzMenuItem = new JMenuItem("" + number * i);
            hertzMenu.add(hertzMenuItem);
            hertzMenuItem.addActionListener(e -> {
                switch (e.getActionCommand()) {
                    case "30" -> Settings.HERTZ = 30;
                    case "60" -> Settings.HERTZ = 60;
                    case "90" -> Settings.HERTZ = 90;
                    case "120" -> Settings.HERTZ = 120;
                }
            });
        }
        return hertzMenu;
    }

    private static JMenu createColourSchemeMenu(TestBedWindow gameScreen) {
        JMenu colourScheme = new JMenu("Colour schemes");

        JMenuItem defaultScheme = new JMenuItem("Default");
        colourScheme.add(defaultScheme);
        defaultScheme.addActionListener(new ColourMenuInput(gameScreen));

        JMenuItem box2dScheme = new JMenuItem("Box2d");
        colourScheme.add(box2dScheme);
        box2dScheme.addActionListener(new ColourMenuInput(gameScreen));

        JMenuItem matterjsScheme = new JMenuItem("MatterJs");
        colourScheme.add(matterjsScheme);
        matterjsScheme.addActionListener(new ColourMenuInput(gameScreen));

        JMenuItem monochromaticScheme = new JMenuItem("Monochromatic");
        colourScheme.add(monochromaticScheme);
        monochromaticScheme.addActionListener(new ColourMenuInput(gameScreen));

        return colourScheme;
    }

    private static JMenu createTestMenu(TestBedWindow gameScreen) {
        JMenu testMenu = new JMenu("Demos");
        testMenu.setMnemonic(KeyEvent.VK_M);

        JMenuItem bouncingBall = new JMenuItem("Bouncing ball");
        bouncingBall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK));
        testMenu.add(bouncingBall);
        bouncingBall.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem chains = new JMenuItem("Chains");
        chains.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK));
        testMenu.add(chains);
        chains.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem compoundBodies = new JMenuItem("Compound bodies");
        compoundBodies.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK));
        testMenu.add(compoundBodies);
        compoundBodies.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem drag = new JMenuItem("Drag");
        drag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_DOWN_MASK));
        testMenu.add(drag);
        drag.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem friction = new JMenuItem("Friction");
        friction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.ALT_DOWN_MASK));
        testMenu.add(friction);
        friction.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem lineOfSight = new JMenuItem("Line of sight");
        lineOfSight.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.ALT_DOWN_MASK));
        testMenu.add(lineOfSight);
        lineOfSight.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem mixedShapes = new JMenuItem("Mixed shapes");
        mixedShapes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, InputEvent.ALT_DOWN_MASK));
        testMenu.add(mixedShapes);
        mixedShapes.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem newtonsCradle = new JMenuItem("Newtons cradle");
        newtonsCradle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, InputEvent.ALT_DOWN_MASK));
        testMenu.add(newtonsCradle);
        newtonsCradle.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem particleExplosion = new JMenuItem("Particle explosion");
        particleExplosion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9, InputEvent.ALT_DOWN_MASK));
        testMenu.add(particleExplosion);
        particleExplosion.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem proximityExplosion = new JMenuItem("Proximity explosion");
        proximityExplosion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_DOWN_MASK));
        testMenu.add(proximityExplosion);
        proximityExplosion.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem raycastExplosion = new JMenuItem("Raycast explosion");
        raycastExplosion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.ALT_DOWN_MASK));
        testMenu.add(raycastExplosion);
        raycastExplosion.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem raycast = new JMenuItem("Raycast");
        raycast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_DOWN_MASK));
        testMenu.add(raycast);
        raycast.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem restitution = new JMenuItem("Restitution");
        restitution.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK));
        testMenu.add(restitution);
        restitution.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem stackedObjects = new JMenuItem("Stacked objects");
        stackedObjects.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK));
        testMenu.add(stackedObjects);
        stackedObjects.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem trebuchet = new JMenuItem("Trebuchet");
        trebuchet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.ALT_DOWN_MASK));
        testMenu.add(trebuchet);
        trebuchet.addActionListener(new DemoMenuInput(gameScreen));

        JMenuItem wreckingBall = new JMenuItem("Wrecking ball");
        wreckingBall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_DOWN_MASK));
        testMenu.add(wreckingBall);
        wreckingBall.addActionListener(new DemoMenuInput(gameScreen));

        //TO DO: Create car demo and add to JMenu
        /* JMenuItem car = new JMenuItem("Car");
        car.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK));
        testMenu.add(car);
        car.addActionListener(new DemoMenuInput(gameScreen));*/

        return testMenu;
    }

    public void generateRandomObjects(Vectors2D lowerBound, Vectors2D upperBound, int totalObjects, int maxRadius) {
        while (totalObjects > 0) {
            Body b = createRandomObject(lowerBound, upperBound, maxRadius);
            if (overlap(b)) {
                world.addBody(b);
                totalObjects--;
            }
        }
    }

    private boolean overlap(Body b) {
        for (Body a : world.bodies) {
            if (AABB.AABBOverLap(a, b)) {
                return false;
            }
        }
        return true;
    }

    private Body createRandomObject(Vectors2D lowerBound, Vectors2D upperBound, int maxRadius) {
        int objectType = Settings.generateRandomNoInRange(1, 2);
        Body b = null;
        int radius = Settings.generateRandomNoInRange(5, maxRadius);
        double x = Settings.generateRandomNoInRange(lowerBound.x + radius, upperBound.x - radius);
        double y = Settings.generateRandomNoInRange(lowerBound.y + radius, upperBound.y - radius);
        double rotation = Settings.generateRandomNoInRange(0.0, 7.0);
        switch (objectType) {
            case 1:
                b = new Body(new Circle(radius), x, y);
                b.setOrientation(rotation);
                break;
            case 2:
                int sides = Settings.generateRandomNoInRange(3, 10);
                b = new Body(new Polygon(radius, sides), x, y);
                b.setOrientation(rotation);
                break;
        }
        return b;
    }

    public void setStaticWorldBodies() {
        for (Body b : world.bodies) {
            b.setDensity(0);
        }
    }

    private final ArrayList<ShadowCasting> shadowCastings = new ArrayList<>();

    public void add(ShadowCasting shadowCasting) {
        shadowCastings.add(shadowCasting);
    }
}