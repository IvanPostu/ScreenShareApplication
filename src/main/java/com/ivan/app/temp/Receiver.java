package com.ivan.app.temp;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author uli
 */
public class Receiver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // Address
        String multiCastAddress = "224.0.0.1";
        final int multiCastPort = 52684;
        final int bufferSize = 1024 * 2; // Maximum size of transfer object
        final List<byte[]> receivedBytes = new ArrayList<>();

        // Create Socket
        System.out.println("Create socket on address " + multiCastAddress + " and port "
                + multiCastPort + ".");
        InetAddress group = InetAddress.getByName(multiCastAddress);
        try (MulticastSocket s = new MulticastSocket(multiCastPort)) {
            s.joinGroup(group);

            // Receive data
            while (true) {
                System.out.println("Wating for datagram to be received...");

                // Create buffer
                byte[] buffer = new byte[bufferSize];
                s.receive(new DatagramPacket(buffer, bufferSize, group, multiCastPort));
                System.out.println("Datagram received!");

                boolean isStart = buffer[0] == 83 && buffer[1] == 84 && buffer[2] == 65
                        && buffer[3] == 82 && buffer[4] == 84;

                boolean isEnd = buffer[0] == 69 && buffer[1] == 78 && buffer[2] == 68;

                if (isStart) {
                    continue;
                    // try (FileOutputStream fos = new FileOutputStream("q2.png")) {
                    // fos.write(dataz);
                    // }
                    // receivedBytes.clear();
                }

                if (isEnd) {
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

                    continue;

                    // try (FileOutputStream fos = new FileOutputStream("q2.png")) {
                    // fos.write(dataz);
                    // }
                    // receivedBytes.clear();
                }

                receivedBytes.add(buffer);
                // Deserialze object
                // ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                // ObjectInputStream ois = new ObjectInputStream(bais);
                // try {
                // Object readObject = ois.readObject();
                // if (readObject instanceof String) {
                // String message = (String) readObject;
                // System.out.println("Message is: " + message);
                // } else {
                // System.out.println("The received object is not of type String!");
                // }
                // } catch (Exception e) {
                // System.out.println("No object could be read from the received UDP datagram.");
                // }

            }
        }


    }
}
