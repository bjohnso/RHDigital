package com.example.rhdigital.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                            RHDatabase.class, "RHDatabase")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final CourseDAO courseDAO;

        public PopulateDbAsync(RHDatabase instance) {
            courseDAO = instance.courseDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            courseDAO.deleteAll();
            courseDAO.insert(
                    new Course("How to be a Millionare",
                            "Becoming super Swag easy!",
                            "https://swagmoney.com/millionare")
            );
            courseDAO.insert(
                    new Course("How to be a Billionare",
                            "Becoming super Swag easy!",
                            "https://swagmoney.com/millionare")
            );
            courseDAO.insert(
                    new Course("How to be a Trillionare",
                            "Becoming super Swag easy!",
                            "https://swagmoney.com/millionare")
            );
            courseDAO.insert(
                    new Course("How to be a Quadrillionare",
                            "Becoming super Swag easy!",
                            "https://swagmoney.com/millionare")
            );
            return null;
        }
    }
}
