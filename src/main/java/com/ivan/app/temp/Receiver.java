package com.ivan.app.temp;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;
import java.util.List;
import com.ivan.app.screenshare.Constants;


public class Receiver {
    private boolean ready = true;

    private static byte[] getImageData(byte[] buffer, List<byte[]> receivedBytes)
            throws IOException {
        byte[] intBytes = new byte[4];
        intBytes[0] = buffer[3];
        intBytes[1] = buffer[4];
        intBytes[2] = buffer[5];
        intBytes[3] = buffer[6];

        int size = ByteBuffer.wrap(intBytes).getInt();
        byte[] receivedBytesWithFixedSize = new byte[size];

        int counter = 0;
        for (byte[] bytes : receivedBytes) {
            for (byte b : bytes) {
                if (counter == size) {
                    break;
                }

                receivedBytesWithFixedSize[counter++] = b;
            }
        }

        return receivedBytesWithFixedSize;

    }


    private byte[] receiveMessage(String ip, int port) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET);
        datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        datagramChannel.bind(new InetSocketAddress(port));
        ByteBuffer byteBuffer = ByteBuffer.allocate(Constants.BUF_SIZE);
        datagramChannel.receive(byteBuffer);
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes, 0, byteBuffer.limit());
        return bytes;
    }

    public byte[] receiveImage(String ip, int port) throws IOException {
        ready = false;
        List<byte[]> byteArrays = new LinkedList<>();

        while (true) {
            byte[] data = this.receiveMessage(ip, port);
            boolean isEnd = data[0] == 69 && data[1] == 78 && data[2] == 68;
            if (isEnd) {
                ready = true;
                return getImageData(data, byteArrays);
            }

            byteArrays.add(data);
        }

    }

    public boolean isReady() {
        return ready;
    }

}
