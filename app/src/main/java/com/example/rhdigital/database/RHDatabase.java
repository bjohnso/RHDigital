package com.example.rhdigital.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.rhdigital.database.DAO.CourseDAO;
import com.example.rhdigital.database.model.Course;

@Database(entities = {Course.class}, version = 1)
public abstract class RHDatabase extends RoomDatabase {

    private static volatile RHDatabase INSTANCE;
    public abstract CourseDAO courseDAO();

    public static RHDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RHDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RHDatabase.class, "RHDatabse").build();
                }
            }
        }
        return INSTANCE;
    }
}
