package com.epic.cefts.pool;

import com.epic.cefts.bean.ListenerSession;

public interface RequestHandler {
    void handleRequest(ListenerSession lSession) throws Exception;
}
