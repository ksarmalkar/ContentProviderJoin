package com.myexample.jointables.helper;

public class DBHelper {

    public static final String ALIAS_JOINER = "_";

    public static String addPrefix(String tableName, String column) {
        return tableName + "." + column;
    }

    public static String getColumnAlias(String tableName, String column){
        return tableName + ALIAS_JOINER + column;
    }
}
