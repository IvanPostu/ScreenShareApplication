package com.ivan.app.screenshare;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;
import com.ivan.app.utils.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkSender {
    private static final Logger LOG = LogManager.getLogger(NetworkSender.class);


    private InetAddress host;
    private int port;

    public NetworkSender(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public void sendData(byte[] data) {
        try (DatagramSocket sender = new DatagramSocket()) {

            final int BUF_SIZE = 256;
            byte[] arr = new byte[BUF_SIZE];

            LinkedList<byte[]> chunks = new LinkedList<>();
            for (int i = 0; i < data.length; i++) {
                if (i % BUF_SIZE == 0) {
                    if (i > 0) {
                        chunks.add(arr);
                    }


                    arr = (data.length - i) >= BUF_SIZE ? new byte[BUF_SIZE]
                            : new byte[data.length - i];
                }

                arr[i % BUF_SIZE] = data[i];
            }

            chunks.add(arr);

            Iterator<byte[]> iterator = chunks.iterator();

            byte[] start = "START".getBytes();
            DatagramPacket packet = new DatagramPacket(start, start.length, host, port);
            sender.send(packet);

            while (iterator.hasNext()) {
                byte[] part = iterator.next();
                packet = new DatagramPacket(part, part.length, host, port);
                sender.send(packet);

            }

            byte[] end = "END".getBytes();
            packet = new DatagramPacket(end, end.length, host, port);
            sender.send(packet);

            char c = 'a';

        } catch (Exception e) {
            LOG.error(e);
        }
    }



}
