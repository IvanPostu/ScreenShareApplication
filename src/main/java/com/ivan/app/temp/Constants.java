package com.ivan.app.temp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constants {

    public static InetAddress IP() throws UnknownHostException {
        return InetAddress.getByName(HOST); // "224.0.0.1"
    }

    public static final String HOST = "230.0.0.0";
    // "192.168.0.19"

    public static final int PORT = 8080;

    public static final int BUF_SIZE = 2048;

    public static final String MULTICAST_INTERFACE = "wlp1s0";

}
