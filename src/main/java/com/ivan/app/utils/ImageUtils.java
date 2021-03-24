package com.ivan.app.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ImageUtils {
    private static final Logger LOG = LogManager.getLogger(ImageUtils.class);


    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream in = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }

    public static byte[] toByteArray(BufferedImage bi) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        baos.flush();
        byte[] bytes = baos.toByteArray();
        baos.close();

        return bytes;

    }
}
