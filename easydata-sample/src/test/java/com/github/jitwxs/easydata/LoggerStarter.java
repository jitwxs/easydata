package com.github.jitwxs.easydata;

import com.github.jitwxs.easydata.conn.MySQLConnectionTest;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 19:50
 */
public class LoggerStarter {
    static {
        PropertyConfigurator.configure(MySQLConnectionTest.class.getResourceAsStream("/log4j.properties"));
    }
}
