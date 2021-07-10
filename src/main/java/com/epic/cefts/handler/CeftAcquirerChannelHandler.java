package com.epic.cefts.handler;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
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
                        byte[] HD = new byte[4];
                        IN.readFully(HD, 0, 4);
                        int HD_LEN = Integer.parseInt(ISOUtil.hexString(HD), 16);
                        byte BUFF[] = new byte[HD_LEN];
                        IN.readFully(BUFF, 0, HD_LEN);

                        byte ceftHeader[] = new byte[20];
                        byte ceftMsg[] = new byte[BUFF.length - 20];
                        for (int i = 0; i < BUFF.length; i++) {

                            if (i < 20) {
                                ceftHeader[i] = BUFF[i];
                            } else {
                                ceftMsg[i - 20] = BUFF[i];
                            }
                        }
                        InputStream is = getClass().getResourceAsStream("/iso87binary.xml");
                        GenericPackager packager = new GenericPackager(is);
                        ISOMsg isoMsg = new ISOMsg();
                        isoMsg.setPackager(packager);
                        isoMsg.unpack(ceftMsg);
                        printISOMessage(isoMsg);
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

    public void printISOMessage(ISOMsg isoMsg) {
        try {
            System.out.printf("MTI = %s%n", isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }
}
