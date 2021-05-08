package com.epic.cefts.pool;

import com.epic.cefts.bean.ListenerSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestThread extends Thread {

    private RequestQueue queue;
    private boolean running;
    private boolean processing = false;
    private int threadNumber;
    private RequestHandler requestHandler;
    private ListenerSession session;
//    private int execution = 0;
    private static final Logger LOGGER = LogManager.getLogger(RequestThread.class);


    public int getThreadNumber() {
        return threadNumber;
    }


    public RequestThread(RequestQueue queue, int threadNumber, String requestHandlerClassName) {
        this.queue = queue;
        this.threadNumber = threadNumber;
        try {
            this.requestHandler = (RequestHandler) (Class.forName(requestHandlerClassName).newInstance());
        } catch (Exception e) {
        }
    }

    public boolean isProcessing() {
        return this.processing;
    }

    @Override
    public void run() {
        this.running = true;
        while (running) {
            try {
                System.out.println("threadNumber : " + threadNumber);
                this.session = queue.getNextObject();
//                execution++;
                session.setOpen_thread_no(threadNumber + "");
//                LOGGER.info(session.getSessionId() +" " +session.getOpen_thread_no() + " Final execution number (start) : "+execution );
                if (running) {
                    this.processing = true;
                    LOGGER.info(session.getSessionId() +" " +session.getOpen_thread_no() + " [Processing thread number in pool[ " + threadNumber + " ]: Processing request ........" );

                    this.requestHandler.handleRequest(this.session);
                    LOGGER.info(session.getSessionId() +" " +session.getOpen_thread_no() + " [Processing thread number in pool[ " + threadNumber + " ]: Finished Processing request ......." );
                }
            } catch (Exception e) {
                LOGGER.info(session.getSessionId() +" " +session.getOpen_thread_no() + " Error is  detected in thread number [ "+threadNumber+" ]  is processing ");
            } finally {
//                execution--;
                LOGGER.info(session.getSessionId() +" " +session.getOpen_thread_no() + "Processing thread number [ "+threadNumber+" ] in  pool returns back to the threads pool....");
                this.processing = false;
//                LOGGER.info(session.getSessionId() +" " +session.getOpen_thread_no() + "Final execution number (end) : "+execution);
            }

        }
    }
}
