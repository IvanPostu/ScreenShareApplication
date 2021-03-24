package com.ivan.app.temp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constants {

    public static InetAddress IP() throws UnknownHostException {
        return InetAddress.getByName("224.0.0.1"); // "224.0.0.1"
    }

    // "192.168.0.19"

    public static final int PORT = 8080;

    public static final int BUF_SIZE = 2048;

}
