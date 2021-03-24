package com.ivan.app.screenshare;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import com.ivan.app.utils.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkReceiver implements Closeable {

    private static final Logger LOG = LogManager.getLogger(NetworkReceiver.class);

    private BufferedImage shareImage;
    private Thread thread;
    private AtomicBoolean threadIsRunning = new AtomicBoolean(false);
    private InetAddress host;
    private int port;

    public NetworkReceiver(InetAddress host, int port) {
        this.host = host;
        this.port = port;

        this.thread = new Thread(() -> {
            List<byte[]> imageChunks = new LinkedList<>();

            while (threadIsRunning.get()) {
                try (DatagramSocket server = new DatagramSocket(this.port, this.host)) {

                    server.setSoTimeout(3000);
                    final int BUF_SIZE = 256;
                    byte[] buffer = new byte[BUF_SIZE];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    server.receive(packet);

                    byte[] receivedBytes = packet.getData();

                    // if (receivedBytes[0] == 83 && receivedBytes[1] == 84 && receivedBytes[2] ==
                    // 65
                    // && receivedBytes[3] == 82 && receivedBytes[4] == 84) {

                    // continue;
                    // }
                    if (receivedBytes[0] == 69 && receivedBytes[1] == 78
                            && receivedBytes[2] == 68) {
                        buildImage(imageChunks);
                        imageChunks.clear();
                        continue;
                    }

                    imageChunks.add(receivedBytes);

                    //
                } catch (Exception e) {
                    if (e instanceof SocketTimeoutException) {
                        if (threadIsRunning.get()) {
                            LOG.info("UDP socket connection timeout :(");
                            continue;
                        }
                    }

                    LOG.error(e);
                    threadIsRunning.getAndSet(false);
                    return;
                }
            }
        });
    }

    public void run() throws SocketException, IOException {
        threadIsRunning.getAndSet(true);



        this.thread.run();
    }

    public BufferedImage getShareImage() {
        return shareImage;
    }

    private void buildImage(List<byte[]> imageChunks) {
        int len = 0;
        for (int i = 0; i < imageChunks.size(); i++) {
            len += imageChunks.get(i).length;
        }

        int counter = 0;
        byte[] imageBytes = new byte[len];
        for (byte[] arr : imageChunks) {
            for (byte b : arr) {
                imageBytes[counter++] = b;
            }
        }


        BufferedImage shareImage = ImageUtils.createImageFromBytes(imageBytes);
        this.shareImage = shareImage;
    }

    @Override
    public void close() throws IOException {
        this.threadIsRunning.getAndSet(false);
        try {
            thread.join();
        } catch (InterruptedException ex) {
            LOG.error(ex);
        }
    }

}
