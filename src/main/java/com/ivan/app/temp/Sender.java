package com.ivan.app.temp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

    /*
     * public static void main(String[] args) throws IOException {
     * 
     * // Address final int multiCastPort = Constants.PORT;
     * 
     * Path path = Paths.get("q1.png"); byte[] dataz = Files.readAllBytes(path);
     * 
     * int BUF_SIZE = Constants.BUF_SIZE; List<byte[]> listOfByteArrays = new ArrayList<>(); byte[]
     * currentByteArray = new byte[BUF_SIZE];
     * 
     * for (int i = 0; i < dataz.length; i++) { if (i % BUF_SIZE == 0) { currentByteArray = new
     * byte[BUF_SIZE]; listOfByteArrays.add(currentByteArray); }
     * 
     * currentByteArray[i % BUF_SIZE] = dataz[i]; }
     * 
     * 
     * System.out.println("Create socket on address " + Constants.IP().getHostAddress() +
     * " and port " + multiCastPort + "."); InetAddress group = Constants.IP();
     * 
     * try (DatagramSocket s = new DatagramSocket()) { // ((MulticastSocket) s).joinGroup(group);
     * 
     * 
     * byte[] start = "START".getBytes(); // 83, 84, 65, 82, 84 s.send(new DatagramPacket(start,
     * start.length, group, multiCastPort));
     * 
     * for (byte[] bytes : listOfByteArrays) { s.send(new DatagramPacket(bytes, bytes.length, group,
     * multiCastPort)); }
     * 
     * byte[] end = new byte[4 + 3]; // 69, 78, 68 end[0] = 69; end[1] = 78; end[2] = 68;
     * 
     * byte[] len = intToBytes(dataz.length);
     * 
     * end[3] = len[0]; end[4] = len[1]; end[5] = len[2]; end[6] = len[3];
     * 
     * s.send(new DatagramPacket(end, end.length, group, multiCastPort));
     * 
     * }
     * 
     * 
     * }
     */

    public void sendLargeMessages(String ip, int port, List<byte[]> messages)
            throws IOException, InterruptedException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(null);

        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);

        Iterator<byte[]> messagesIterator = messages.iterator();
        while (messagesIterator.hasNext()) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(messagesIterator.next());
            datagramChannel.send(byteBuffer, inetSocketAddress);
            Thread.sleep(20);
        }
    }

    public void sendMessage(String ip, int port, byte[] message) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(null);

        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
        ByteBuffer byteBuffer = ByteBuffer.wrap(message);
        datagramChannel.send(byteBuffer, inetSocketAddress);

    }


    public static void main(String[] args) throws IOException {
        int BUF_SIZE = Constants.BUF_SIZE;
        Sender mp = new Sender();
        Path path = Paths.get("q1.png");
        byte[] dataz = Files.readAllBytes(path);
        byte[] currentByteArray = new byte[BUF_SIZE];

        List<byte[]> listOfByteArrays = new ArrayList<>();
        for (int i = 0; i < dataz.length; i++) {
            if (i % BUF_SIZE == 0) {
                int arrSize = dataz.length - i >= BUF_SIZE ? BUF_SIZE : dataz.length - i;
                currentByteArray = new byte[arrSize];
                listOfByteArrays.add(currentByteArray);
            }
            currentByteArray[i % BUF_SIZE] = dataz[i];
        }

        try {
            mp.sendLargeMessages(Constants.HOST, Constants.PORT, listOfByteArrays);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        byte[] end = new byte[4 + 3]; // 69, 78, 68
        byte[] len = intToBytes(dataz.length);
        end[0] = 69;
        end[1] = 78;
        end[2] = 68;
        end[3] = len[0];
        end[4] = len[1];
        end[5] = len[2];
        end[6] = len[3];

        mp.sendMessage(Constants.HOST, Constants.PORT, end);
    }


}
