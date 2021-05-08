package com.epic.cefts;

import com.epic.cefts.bean.ListenerSession;
import com.epic.cefts.database.DBPool;
import com.epic.cefts.pool.RequestQueue;
import com.sun.prism.impl.PrismSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;

@SpringBootApplication
public class CeftsBootApplication {

    private static Connection con;
    private static final Logger LOGGER = LogManager.getLogger(CeftsBootApplication.class);
    public static RequestQueue ENGINE_QUEUE;

    public static void main(String[] args) {
        SpringApplication.run(CeftsBootApplication.class, args);
//        LOGGER.fatal("fatal level log message");
//        LOGGER.error("Error level log message");
//        LOGGER.warn("warn level log message");
//        LOGGER.info("Info level log message");
//        LOGGER.debug("Debug level log message");
//        LOGGER.trace("trace level log message");
        try {


//            new Thread(new DBPool()).start();
//            Thread.sleep(5000);

//            LOGGER.info("Db Pool creation success..");

            ENGINE_QUEUE = new RequestQueue("com.epic.cefts.message.MessageHandler",
                    10,
                    2,
                    8);

            Thread.sleep(5000);
            LOGGER.info("Main Queue creation success..");

            for (int i = 0; i < 20; i++) {
                ListenerSession s = new ListenerSession();
                s.setSessionId(String.valueOf(i));
//                LOGGER.info("adding :"+ i);
                ENGINE_QUEUE.add(s);
                Thread.sleep(500);
            }


        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Db pool creation error.." + e.getMessage());

        }

    }


}
