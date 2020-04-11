package com.example.rhdigital.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.rhdigital.database.DAO.CourseDAO;
import com.example.rhdigital.database.DAO.WorkbookDAO;
import com.example.rhdigital.database.model.Course;
import com.example.rhdigital.database.model.Workbook;

@Database(entities = {Course.class, Workbook.class}, version = 5, exportSchema = false)
public abstract class RHDatabase extends RoomDatabase {

    private static volatile RHDatabase INSTANCE;
    public abstract CourseDAO courseDAO();
    public abstract WorkbookDAO workbookDAO();

    public static RHDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RHDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RHDatabase.class, "RHDatabase")
                            .addCallback(roomDatabaseCallback)
                            .fallbackToDestructiveMigration()
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
        private final WorkbookDAO workbookDAO;

        public PopulateDbAsync(RHDatabase instance) {
            courseDAO = instance.courseDAO();
            workbookDAO = instance.workbookDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //courseDAO.deleteAll();

//            long milli = courseDAO.insert(
//                    new Course("How to be a Millionare",
//                            "Becoming super Swag easy!",
//                            "stock1",
//                            "https://swagmoney.com/millionare")
//            );
//            long billi = courseDAO.insert(
//                    new Course("How to be a Billionare",
//                            "Becoming super Swag easy!",
//                            "stock2",
//                            "https://swagmoney.com/millionare")
//            );
//            long trilli = courseDAO.insert(
//                    new Course("How to be a Trillionare",
//                            "Becoming super Swag easy!",
//                            "stock3",
//                            "https://swagmoney.com/millionare")
//            );
//            long qudrilli = courseDAO.insert(
//                    new Course("How to be a Quadrillionare",
//                            "Becoming super Swag easy!",
//                            "stock4",
//                            "https://swagmoney.com/millionare")
//            );
//
//            Log.d("RHDB", "MILLI : " + milli);
//            Log.d("RHDB", "BILLI : " + billi);
//            Log.d("RHDB", "TRILLI : " + trilli);
//            Log.d("RHDB", "QUADRILLI : " + qudrilli);

//            //WORKBOOKS
            workbookDAO.insert(
                    new Workbook("How to be a Millionare",
                            "Module 1",
                            "https://swagmoney.com/millionare/workbooks?module=1",
                            5)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Millionare",
                            "Module 2",
                            "https://swagmoney.com/millionare/workbooks?module=2",
                            5)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Millionare",
                            "Module 3",
                            "https://swagmoney.com/millionare/workbooks?module=3",
                            5)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Billionare",
                            "Module 1",
                            "https://swagmoney.com/millionare/workbooks?module=1",
                            6)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Billionare",
                            "Module 2",
                            "https://swagmoney.com/millionare/workbooks?module=2",
                            6)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Billionare",
                            "Module 3",
                            "https://swagmoney.com/millionare/workbooks?module=3",
                            6)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Trillionare",
                            "Module 1",
                            "https://swagmoney.com/millionare/workbooks?module=1",
                            7)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Trillionare",
                            "Module 2",
                            "https://swagmoney.com/millionare/workbooks?module=2",
                            7)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Trillionare",
                            "Module 3",
                            "https://swagmoney.com/millionare/workbooks?module=3",
                            7)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Quadrillion",
                            "Module 1",
                            "https://swagmoney.com/millionare/workbooks?module=1",
                            8)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Quadrillion",
                            "Module 2",
                            "https://swagmoney.com/millionare/workbooks?module=2",
                            8)
            );
            workbookDAO.insert(
                    new Workbook("How to be a Quadrillion",
                            "Module 3",
                            "https://swagmoney.com/millionare/workbooks?module=3",
                            8)
            );
            return null;
        }
    }
}
