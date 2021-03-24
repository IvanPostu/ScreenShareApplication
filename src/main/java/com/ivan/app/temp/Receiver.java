package com.ivan.app.temp;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author uli
 */
public class Receiver {

    /**
     * @param args the command line arguments
     */
    /*
     * public static void main(String[] args) throws IOException { // Address // String
     * multiCastAddress = "224.0.0.1"; final int multiCastPort = Constants.PORT; final int
     * bufferSize = Constants.BUF_SIZE; // Maximum size of transfer object final List<byte[]>
     * receivedBytes = new ArrayList<>();
     * 
     * // Create Socket // InetAddress group = InetAddress.getByName(multiCastAddress); InetAddress
     * IP = Constants.IP();
     * 
     * System.out.println("Create socket on address " + IP.getHostAddress() + " and port " +
     * multiCastPort + "."); try (DatagramSocket s = new DatagramSocket(multiCastPort)) { //
     * ((MulticastSocket) s).joinGroup(IP); s.setSoTimeout(3000);
     * 
     * int loopCounter = 0; // Receive data while (true) {
     * System.out.println("Wating for datagram to be received...");
     * 
     * // Create buffer byte[] buffer = new byte[bufferSize];
     * 
     * try { s.receive(new DatagramPacket(buffer, bufferSize)); } catch (SocketTimeoutException e) {
     * char c = 'a'; }
     * 
     * System.out.println("Datagram received!");
     * 
     * boolean isStart = buffer[0] == 83 && buffer[1] == 84 && buffer[2] == 65 && buffer[3] == 82 &&
     * buffer[4] == 84;
     * 
     * boolean isEnd = buffer[0] == 69 && buffer[1] == 78 && buffer[2] == 68;
     * 
     * if (isStart) { continue; }
     * 
     * if (isEnd) { saveImageData(buffer, receivedBytes); loopCounter = 0; continue;
     * 
     * }
     * 
     * loopCounter++; receivedBytes.add(buffer);
     * 
     * 
     * } }
     * 
     * 
     * }
     */

    private static void saveImageData(byte[] buffer, List<byte[]> receivedBytes)
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

        try (FileOutputStream fos = new FileOutputStream("q2.png")) {
            fos.write(receivedBytesWithFixedSize);
        }

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

    public static void main(String[] args) throws IOException {
        Receiver mr = new Receiver();
        List<byte[]> byteArrays = new LinkedList<>();

        while (true) {
            byte[] data = mr.receiveMessage(Constants.HOST, Constants.PORT);
            boolean isEnd = data[0] == 69 && data[1] == 78 && data[2] == 68;
            if (isEnd) {

                saveImageData(data, byteArrays);
                break;
            }

            byteArrays.add(data);

        }

        // System.out.println("Message received : " + new String(data));
    }

}
