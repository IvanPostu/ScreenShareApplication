package com.ivan.app.temp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.ivan.app.screenshare.Constants;


public class Sender {


    private static byte[] intToBytes(final int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }

    private void sendLargeMessages(String ip, int port, List<byte[]> messages)
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

    private void sendMessage(String ip, int port, byte[] message) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(null);

        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
        ByteBuffer byteBuffer = ByteBuffer.wrap(message);
        datagramChannel.send(byteBuffer, inetSocketAddress);

    }

    public void sendImage(byte[] imageByteArray, String ip, int port) throws IOException {
        int BUF_SIZE = Constants.BUF_SIZE;
        byte[] dataz = imageByteArray;
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
            this.sendLargeMessages(ip, port, listOfByteArrays);
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

        this.sendMessage(ip, port, end);
    }

    // public static void main(String[] args) throws IOException {
    // int BUF_SIZE = Constants.BUF_SIZE;
    // Sender mp = new Sender();
    // Path path = Paths.get("q1.png");
    // byte[] dataz = Files.readAllBytes(path);
    // byte[] currentByteArray = new byte[BUF_SIZE];

    // List<byte[]> listOfByteArrays = new ArrayList<>();
    // for (int i = 0; i < dataz.length; i++) {
    // if (i % BUF_SIZE == 0) {
    // int arrSize = dataz.length - i >= BUF_SIZE ? BUF_SIZE : dataz.length - i;
    // currentByteArray = new byte[arrSize];
    // listOfByteArrays.add(currentByteArray);
    // }
    // currentByteArray[i % BUF_SIZE] = dataz[i];
    // }

    // try {
    // mp.sendLargeMessages(Constants.HOST, Constants.PORT, listOfByteArrays);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }


    // byte[] end = new byte[4 + 3]; // 69, 78, 68
    // byte[] len = intToBytes(dataz.length);
    // end[0] = 69;
    // end[1] = 78;
    // end[2] = 68;
    // end[3] = len[0];
    // end[4] = len[1];
    // end[5] = len[2];
    // end[6] = len[3];

    // mp.sendMessage(Constants.HOST, Constants.PORT, end);
    // }


}
