package com.lyn.cleansing.other;

import com.lyn.cleansing.service.handler.DatabaseHandler;

/**
 * @author lyn
 * @date 2023/8/7
 */
public class DbHandlerThreadLocal {

    /**
     *  当前线程用的数据库操作handler
     *
     */
    private static final ThreadLocal<DatabaseHandler> dbHandler = new ThreadLocal<>();

    public static void setDbHandler(DatabaseHandler handler){
        dbHandler.set(handler);
    }

    public static DatabaseHandler getDbHandler(){
        return dbHandler.get();
    }

    public static void removeDbHandler(){
        dbHandler.remove();
    }
}
