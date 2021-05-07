package com.epic.cefts;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import com.epic.cefts.database.DBPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import snaq.db.ConnectionPool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class CeftsBootApplication {

    private static Connection con;
    private static final Logger LOGGER = LogManager.getLogger(CeftsBootApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CeftsBootApplication.class, args);
        LOGGER.fatal("fatal level log message");
        LOGGER.error("Error level log message");
        LOGGER.warn("warn level log message");
        LOGGER.info("Info level log message");
        LOGGER.debug("Debug level log message");
        LOGGER.trace("trace level log message");
        try {



            new Thread(new DBPool()).start();
            Thread.sleep(5000);

            LOGGER.info("Db pool creation success..");


//            test();
            test2();


        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Db pool creation error.." + e.getMessage());

        }

    }

    public void test(){
        String test;
        System.out.println("test git command");
        test2();
    }

    public static void test2(){
        String test;
        System.out.println("sdgsdf");
//        test();
    }
}
