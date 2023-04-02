package com.dyzhome.entity;

import lombok.Data;

/**
 * @author Dyz
 */
@Data
public class DataBaseEntity {
    private String dbName;
    private String userName;
    private String password;
    private String driver;
    private String url;

    public DataBaseEntity(DataBaseParams params) {
        this(params.getIp(), params.getPort(), params.getDbName(),
                params.getUsername(), params.getPassword());
    }

    public DataBaseEntity(String ip, String port, String dbName, String userName, String password) {
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
        this.driver = "com.mysql.cj.jdbc.Driver";
        this.url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
    }
}