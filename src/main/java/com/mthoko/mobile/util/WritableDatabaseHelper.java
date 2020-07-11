package com.mthoko.mobile.util;

/**
 * Created by mthokozisi_mhlanga on 10 Apr 2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mthoko.mobile.entity.UniqueEntity;

import java.util.List;

public class WritableDatabaseHelper extends SQLiteOpenHelper {

    public WritableDatabaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String packageName = "com.mthoko.mobile.entity";
        List<Class<? extends UniqueEntity>> entities = EntityMapper.getSortedEntities(packageName);
        for (Class<? extends UniqueEntity> aClass : entities) {
            String createQuery = EntityMapper.getCreateQuery(aClass);
            db.execSQL(createQuery);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS "+ WORDS_TABLE);
        onCreate(db);
    }

}
