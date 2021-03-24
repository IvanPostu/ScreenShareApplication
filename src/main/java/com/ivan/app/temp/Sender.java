package com.ivan.app.temp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author uli
 */
public class Sender {

    /**
     * @param args the command line arguments
     */

    public static byte[] intToBytes(final int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }

    public static void main(String[] args) throws IOException {

        // Address
        final int multiCastPort = Constants.PORT;

        Path path = Paths.get("q1.png");
        byte[] dataz = Files.readAllBytes(path);

        int BUF_SIZE = 2048;
        List<byte[]> listOfByteArrays = new ArrayList<>();
        byte[] currentByteArray = new byte[BUF_SIZE];

        for (int i = 0; i < dataz.length; i++) {
            if (i % 2048 == 0) {
                currentByteArray = new byte[BUF_SIZE];
                listOfByteArrays.add(currentByteArray);
            }

            currentByteArray[i % 2048] = dataz[i];
        }


        System.out.println("Create socket on address " + Constants.IP().getHostAddress()
                + " and port " + multiCastPort + ".");
        InetAddress group = Constants.IP();
        DatagramSocket s = new DatagramSocket();
        // s.joinGroup(group);


        byte[] start = "START".getBytes(); // 83, 84, 65, 82, 84
        s.send(new DatagramPacket(start, start.length, group, multiCastPort));

        for (byte[] bytes : listOfByteArrays) {
            s.send(new DatagramPacket(bytes, bytes.length, group, multiCastPort));
        }

        byte[] end = new byte[4 + 3]; // 69, 78, 68
        end[0] = 69;
        end[1] = 78;
        end[2] = 68;

        byte[] len = intToBytes(dataz.length);

        end[3] = len[0];
        end[4] = len[1];
        end[5] = len[2];
        end[6] = len[3];

        s.send(new DatagramPacket(end, end.length, group, multiCastPort));

        s.close();
    }

}
