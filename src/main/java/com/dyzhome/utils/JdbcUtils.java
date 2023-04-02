package com.dyzhome.utils;

import com.dyzhome.entity.DataBaseEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.UUID;

/**
 * @author Dyz
 */
public class JdbcUtils {

    public static String uuid;
    public static Connection connection;

    public static String getConnection(DataBaseEntity dataBase) throws Exception {
        Class.forName(dataBase.getDriver());
        connection = DriverManager.getConnection(dataBase.getUrl(), dataBase.getUserName(), dataBase.getPassword());
        uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
