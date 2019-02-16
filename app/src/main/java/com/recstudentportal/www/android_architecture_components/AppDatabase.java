package com.recstudentportal.www.android_architecture_components;

import android.appwidget.AppWidgetManager;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {TaskEntry.class},version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public  abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG= AppDatabase.class.getSimpleName();
    private static final Object LOCK=new Object();
    private static final String DATABASE_NAME="todolist";
    private static AppDatabase sIntance;

    public static AppDatabase getInstance(Context context) {
        if (sIntance==null){
            synchronized (LOCK){
                Log.d(LOG_TAG,"Creating database Instance");
                sIntance= Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,AppDatabase.DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG,"Getting Database Instance");
        return sIntance;
    }
    public abstract TaskDao taskDao();
}
