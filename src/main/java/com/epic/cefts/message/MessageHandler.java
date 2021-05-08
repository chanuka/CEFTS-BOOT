package com.epic.cefts.message;

import com.epic.cefts.bean.ListenerSession;
import com.epic.cefts.pool.RequestHandler;

public class MessageHandler implements RequestHandler {

    @Override
    public void handleRequest(ListenerSession lSession) throws Exception {
        System.out.println( "lSession" + lSession.getSessionId());
        Thread.sleep(15000);
    }
}
