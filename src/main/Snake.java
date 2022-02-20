package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;


import static main.Rectangle.rec_height;
import static main.Rectangle.rec_width;

public class Snake extends JPanel {

    private static final Color c = new Color(115,162,78);
    private static final int start = 250;
    private static final int speed = 25;

    private ArrayList<Rectangle> body;

    private String direction;

    private Apple apple;

    private Main window;


    public Snake(main.Main window) {
        this.window = window;

        this.body = new ArrayList<>();
        body.add(new Rectangle(start, start));
        Rectangle last = this.body.get(0);
        body.add(new Rectangle(last.getPosx() - rec_width, last.getPosy()));
        Rectangle last_2 = this.body.get(1);
        body.add(new Rectangle(last_2.getPosx() - rec_width, last_2.getPosy()));

        this.direction = "right";
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getDirection() {
        return this.direction;
    }

    public void addPart() {
        Rectangle last = this.body.get(this.body.size() -1);
        switch (this.direction) {
            case "right" -> this.body.add(new Rectangle(last.getPosx() - rec_width, last.getPosy()));
            case "left" -> this.body.add(new Rectangle(last.getPosx() + rec_width, last.getPosy()));
            case "up" -> this.body.add(new Rectangle(last.getPosx() , last.getPosy() + rec_width));
            case "down" -> this.body.add(new Rectangle(last.getPosx(), last.getPosy()  - rec_width));
        }
    }

    public void checkColission() {
        Rectangle r3 = this.body.get(0);
        for (int i = 1; i < this.body.size(); i++) {
            Rectangle r2 = this.body.get(i);

            if (r3.intersects(r2)) {
                System.out.println("You lose!");
                this.window.setVisible(false);

                JFrame parent = new JFrame("Game over!");
                JOptionPane.showMessageDialog(parent, "Your score: " + this.body.size());

                this.window.dispatchEvent(new WindowEvent(this.window, WindowEvent.WINDOW_CLOSING));
                System.exit(0);
            }
        }

        if (this.apple != null) {
            if (r3.intersects(new Rectangle(this.apple.getPosx(),this.apple.getPosy()))) {
                this.apple = null;
                this.addPart();
            }
        }

    }

    public void moveSnake() {

        ArrayList<Rectangle> newLst = new ArrayList<>();

        Rectangle first = this.body.get(0);
        Rectangle head = new Rectangle(first.getPosx(), first.getPosy());

        switch (this.direction) {
            case "right" -> head.setPosx(speed);
            case "left" -> head.setPosx(-speed);
            case "up" -> head.setPosy(-speed);
            case "down" -> head.setPosy(speed);
        }
        newLst.add(head);

        for (int i = 1; i < this.body.size(); i++) {
            Rectangle previous = this.body.get(i-1);
            Rectangle newRec = new Rectangle(previous.getPosx(), previous.getPosy());
            newLst.add(newRec);
        }


        this.body = newLst;
        checkColission();
    }

    private void drawSnake(Graphics g) {
        moveSnake();

        // draw moved snake
        Graphics2D g2d = (Graphics2D) g;


        if (this.apple != null) {
            g2d.setPaint(Color.red);
            g2d.drawRect(this.apple.getPosx(), this.apple.getPosy(), rec_width, rec_height);
            g2d.fillRect(this.apple.getPosx(),this.apple.getPosy(),rec_width,rec_height);
        }

        g2d.setPaint(Color.blue);
        for (Rectangle rec: this.body) {
            g2d.drawRect(rec.getPosx(),rec.getPosy(),rec_width,rec_height);
            g2d.fillRect(rec.getPosx(),rec.getPosy(),rec_width,rec_height);
        }
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }

    public Apple getApple() {
        return this.apple;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(c);
        drawSnake(g);
    }
}
