package com.epic.cefts.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CeftAcquirerChannelHandler implements Runnable {

    private static String conIP = "127.0.0.1";
    private static int conPort = 4020;
    private static DataInputStream IN = null;
    private static DataOutputStream OUT = null;
    private static Socket s = null;
    private static boolean keepAliveStatus = true;
    private static int conTimeoutValue = 10000;
    private static boolean isConnect = false;


    private static void connect() throws Exception {
        InetAddress anetAdd = InetAddress.getByName(conIP);
        InetSocketAddress sockAddress = new InetSocketAddress(anetAdd, conPort);

        s = new Socket();
        s.setKeepAlive(keepAliveStatus);
        s.connect(sockAddress, conTimeoutValue);
        IN = new DataInputStream(s.getInputStream());
        OUT = new DataOutputStream(s.getOutputStream());
        isConnect = true;
        // auto log on
        // logOn();
    }

    public static void disconnect() throws Exception {
//        setLog_on_status(false);
        isConnect = false;
        if (s != null) {
            s.close();
            s = null;
        }
        if (IN != null) {
            IN.close();
            IN = null;
        }
        if (OUT != null) {
            OUT.close();
            OUT = null;
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                if (isConnect) {
                    synchronized (IN) {
                        int msgFromReply = IN.readInt();
                        System.out.println("Message from Server is : " + msgFromReply);
                    }

                } else {
                    Thread.sleep(3000);
                    try {
                        connect();
                        System.out.println("connected ..." + isConnect);
                    } catch (Exception ex) {
                    }
                }

            } catch (Exception e) {

            }
        }
    }
}
