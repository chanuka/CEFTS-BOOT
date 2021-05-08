package com.epic.cefts.pool;

import com.epic.cefts.CeftsBootApplication;
import com.epic.cefts.bean.ListenerSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class RequestQueue {

    private  List<ListenerSession>  queue =  Collections.synchronizedList(new LinkedList<>());
    private List<Thread> threadPool = new ArrayList<>();
    private int maxQueueLength;
    private  int minThreads;
    private int maxThreads;
    private int currentThreads;
    private String requestHandlerClassName;
    private boolean running = true;
    private static final Logger LOGGER = LogManager.getLogger(RequestQueue.class);

    public RequestQueue( String requestHandlerClassName,
                         int maxQueueLength,
                         int minThreads,
                         int maxThreads )
    {
        this.requestHandlerClassName = requestHandlerClassName;
        this.maxQueueLength = maxQueueLength;
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.currentThreads = this.minThreads;

        for( int i=0; i<this.minThreads; i++ )
        {
            RequestThread thread = new RequestThread( this, i, requestHandlerClassName );
            thread.start();
            this.threadPool.add( thread );
        }
    }

    public synchronized  int  getSizeOfCoreMessageQueue()throws Exception {
        return queue.size();
    }

    public synchronized void add( ListenerSession object )
    {
        LOGGER.info(object.getSessionId() + " Add: core message queue size : "+ this.queue.size() );
        if( this.queue.size() > this.maxQueueLength )
        {
            LOGGER.info(object.getSessionId() + " The request queue  is full. Max size = "+ this.maxQueueLength );
            return;
        }

        this.queue.add( object );

        boolean availableThread = false;
        for (Thread aThreadPool : this.threadPool) {
            RequestThread rt = (RequestThread) aThreadPool;
            if (!rt.isProcessing()) {
                LOGGER.info(object.getSessionId() + " Thread Number :" + rt.getThreadNumber() + " Found an available thread from pool ");
                object.setOpen_thread_no(rt.getThreadNumber() + "");
                availableThread = true;

                break;
            }
            LOGGER.info(object.getSessionId() + " Thread no [" + rt.getThreadNumber() + " ] is busy in pool..");
        }
        if( !availableThread )
        {
            if( this.currentThreads < this.maxThreads )
            {
                LOGGER.info(object.getSessionId() +" "+ currentThreads +" Creating a new thread of  pool to satisfy the next request" );
                RequestThread thread = new RequestThread( this, currentThreads++, this.requestHandlerClassName );
                object.setOpen_thread_no(thread.getThreadNumber()+"");
                thread.start();
                this.threadPool.add( thread );
            }
            else
            {
                LOGGER.info(object.getSessionId() + " Sorry, can't grow the thread pool  , guess you have to wait little while" );
            }
        }
        LOGGER.info(object.getSessionId() + " Thread Number : "+object.getOpen_thread_no()+ " Thread pool is notify...");
        notifyAll();
    }

    public synchronized ListenerSession getNextObject()
    {
        ListenerSession s;
        while( this.queue.isEmpty() )
        {
            try
            {
                if( !this.running )
                {
                    return null;
                }
                wait();
            }
            catch( InterruptedException ie ) {
            }
        }
        s = this.queue.remove(0);
        LOGGER.info(s.getSessionId() + " Remove: core message queue size : "+ this.queue.size() );
        return s;
    }
}
