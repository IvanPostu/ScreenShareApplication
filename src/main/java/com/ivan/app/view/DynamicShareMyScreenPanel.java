/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.ivan.app.view;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class DynamicShareMyScreenPanel extends javax.swing.JPanel {

    private static final Logger LOG = LogManager.getLogger(DynamicShareMyScreenPanel.class);
    private static final long serialVersionUID = 1187542582181776233L;

    private Robot robot;
    private Timer timer;
    private BufferedImage shareImage;

    public DynamicShareMyScreenPanel() {
        try {
            this.robot = new Robot();
        } catch (AWTException ex) {
            LOG.error(ex);
        }

        initComponents();
        this.timer = new Timer(1000 / 30, e -> {
            this.timerEvent();
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        this.timer.start();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        this.timer.stop();

    }

    private void timerEvent() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle r = new Rectangle(screenSize);
        BufferedImage img = robot.createScreenCapture(r);
        
        
        this.shareImage = resize(img, this.getWidth(), this.getHeight());
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.shareImage == null) {
            return;
        }

        g.drawImage(this.shareImage, 0, 0, this);

    }

    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(204, 255, 153));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
