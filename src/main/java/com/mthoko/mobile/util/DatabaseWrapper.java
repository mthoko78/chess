package com.mthoko.mobile.util;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseWrapper {

    private SQLiteDatabase database;

    public DatabaseWrapper() {
    }

    public DatabaseWrapper(SQLiteDatabase database) {
        this.setDatabase(database);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
