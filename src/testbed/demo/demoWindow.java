package testbed.demo;

import library.*;
import library.Polygon;
import library.joints.Joint;
import library.math.Vectors2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class demoWindow extends JPanel implements Runnable {
    private int windowWidth;
    private int windowHeight;
    private boolean antiAliasing;

    private World world;
    private Thread physicsThread;

    //Input handler classes
    KeyboardInput keyInput = new KeyboardInput();
    MouseInput mouseInput = new MouseInput();
    MouseScroll mouseScrollInput = new MouseScroll();

    public demoWindow(int width, int height, boolean antiAliasing) {
        this.windowWidth = width;
        this.windowHeight = height;
        this.antiAliasing = antiAliasing;

        this.world = new World(new Vectors2D(0, -10));
        physicsThread = new Thread(this);

        addKeyListener(keyInput);
        addMouseListener(mouseInput);
        addMouseWheelListener(mouseScrollInput);
        Body b = world.addBody(new Body(new Polygon(50.0, 50.0), 190, 100));
        Body b1 = world.addBody(new Body(new Polygon(500.0, 50.0), 150, 400));
        b1.setDensity(0);
        world.addJoint(new Joint(b, b1, 200.0, 1000, 10, false, new Vectors2D(), new Vectors2D()));
        startThread();
    }

    public void startThread() {
        physicsThread.start();
        running = true;
    }

    private boolean running = false;

    @Override
    public void run() {
        while (running) {
            world.step(0.000001, 10);
            repaint();
        }
    }

    private boolean drawShapes = true;
    private boolean drawJoints = true;
    private boolean drawAABBs = true;
    private boolean drawContactPoints = false;
    private boolean drawContactNormals = false;
    private boolean drawContactImpulse = false;
    private boolean drawFrictionImpulse = false;
    private boolean drawCOMs = false;

    private ColourSettings paintSettings = new ColourSettings();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gi = (Graphics2D) g;
        if (antiAliasing) gi.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (world != null) {
            for (Body b : world.bodies) {
                if (drawShapes) {
                    b.shape.draw(g, paintSettings);
                }
                if (drawAABBs) {
                    b.shape.drawAABB(g, paintSettings);
                }
                if (drawContactPoints) {
                    //TO DO
                }
                if (drawContactNormals) {
                    //TO DO
                }
                if (drawContactImpulse) {
                    //TO DO
                }
                if (drawFrictionImpulse) {
                    //TO DO
                }
                if (drawCOMs) {
                    //TO DO
                    b.shape.drawCOMS(g, paintSettings);
                }
            }
            if (drawJoints) {
                for (Joint j : world.joints) {
                    j.draw(gi);
                }
            }
        }
    }

    public static void showWindow(demoWindow gameScreen, String title) {
        if (gameScreen != null) {
            JFrame window = new JFrame(title);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.add(gameScreen);
            window.setMinimumSize(new Dimension(800, 600));
            window.setPreferredSize(new Dimension(gameScreen.windowWidth, gameScreen.windowHeight));
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            gameScreen.setFocusable(true);
            gameScreen.setOpaque(true);
            gameScreen.setBackground(gameScreen.paintSettings.background);
        }
    }
}

class KeyboardInput implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

class MouseScroll implements MouseWheelListener {
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}

class MouseInput implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}