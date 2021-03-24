package com.ivan.app.temp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constants {

    public static InetAddress IP() throws UnknownHostException {
        return InetAddress.getByName("localhost"); // "224.0.0.1"
    }

    // "192.168.0.19"

    public static final int PORT = 52684;

}
