package com.epic.cefts.handler;

public class CeftIssuerTimeOutHandler implements  Runnable{
    @Override
    public void run() {
        // remove timeout sessions from the queue
        System.out.println("CeftIssuerTimeOutHandler Running");
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
            }
        }


    }
}
