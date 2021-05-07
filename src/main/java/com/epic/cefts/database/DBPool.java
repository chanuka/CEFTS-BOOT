package com.epic.cefts.database;

import com.epic.cefts.CeftsBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import snaq.db.ConnectionPool;

import java.sql.Connection;

public class DBPool extends Thread {
    private static ConnectionPool pool;
    private static final Logger LOGGER = LogManager.getLogger(DBPool.class);

    @Override
    public void run() {

        try {
            createDbPool();
        } catch (Exception e) {
            System.exit(0);
        }
    }

    public static void createDbPool() throws Exception {

        pool = new ConnectionPool("ORACLE-LINUX-12C-sdf",
                5,
                10,
                20,
                0,
                "jdbc:oracle:thin:@192.168.20.109:1521:orcl",
                "ECONNECTORDEV1",
                "password");

        Thread.sleep(3000);
        Connection contest = getConnection();
        releaseConnectionToPool(contest);

    }

    public static Connection getConnection() throws Exception {
        Connection con = null;
        con = pool.getConnection(8000);
        if (con != null) con.setAutoCommit(false);

        return con;
    }

    public static void releaseConnectionToPool(Connection con) throws Exception {
        if (con != null) {
            con.close();
            con = null;
        }
    }
}
