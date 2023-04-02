package com.dyzhome.entity;

import lombok.Data;

/**
 * @author Dyz
 */
@Data
public class Settings {
    public static String pack;

    public static String author;

    public static String[] tablePrefix;

    static {
        pack = "com.dyzhome";
        author = "Dyz";
        tablePrefix = null;
    }

    public static void initSettings(DataBaseParams params){
        pack = params.getPack();
        author = params.getAuthor();
        tablePrefix = params.getTablePrefix();
    }
}
