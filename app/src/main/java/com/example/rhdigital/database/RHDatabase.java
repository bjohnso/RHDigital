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
import com.example.rhdigital.database.DAO.CourseWithWorkbooksDAO;
import com.example.rhdigital.database.DAO.WorkbookDAO;
import com.example.rhdigital.database.model.Course;
import com.example.rhdigital.database.model.CourseWithWorkbooks;
import com.example.rhdigital.database.model.Workbook;

@Database(entities = {Course.class, Workbook.class}, version = 2, exportSchema = false)
public abstract class RHDatabase extends RoomDatabase {

    private static volatile RHDatabase INSTANCE;
    public abstract CourseDAO courseDAO();
    public abstract WorkbookDAO workbookDAO();
    public abstract CourseWithWorkbooksDAO courseWithWorkbooksDAO();

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
        private final CourseWithWorkbooksDAO courseWithWorkbooksDAO;

        public PopulateDbAsync(RHDatabase instance) {
            courseDAO = instance.courseDAO();
            workbookDAO = instance.workbookDAO();
            courseWithWorkbooksDAO = instance.courseWithWorkbooksDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
          workbookDAO.deleteAll();
          courseDAO.deleteAll();

            long milli = courseDAO.insert(
                    new Course("How to be a Millionare",
                            "Becoming super Swag easy!",
                            "stock1",
                            "https://swagmoney.com/millionare")
            );
            long billi = courseDAO.insert(
                    new Course("How to be a Billionare",
                            "Becoming super Swag easy!",
                            "stock2",
                            "https://swagmoney.com/millionare")
            );
            long trilli = courseDAO.insert(
                    new Course("How to be a Trillionare",
                            "Becoming super Swag easy!",
                            "stock3",
                            "https://swagmoney.com/millionare")
            );
            long qudrilli = courseDAO.insert(
                    new Course("How to be a Quadrillionare",
                            "Becoming super Swag easy!",
                            "stock4",
                            "https://swagmoney.com/millionare")
            );

            //WORKBOOKS
            workbookDAO.insert(
              new Workbook("Module 1", "https://swagmoney.com/millionare/workbooks?module=1", milli)
            );
            workbookDAO.insert(
              new Workbook("Module 2", "https://swagmoney.com/millionare/workbooks?module=2", milli)
            );
            workbookDAO.insert(
              new Workbook("Module 3", "https://swagmoney.com/millionare/workbooks?module=3", milli)
            );
            workbookDAO.insert(
              new Workbook("Module 1", "https://swagmoney.com/billionare/workbooks?module=1", billi)
            );
            workbookDAO.insert(
              new Workbook("Module 2", "https://swagmoney.com/billionare/workbooks?module=2", billi)
            );
            workbookDAO.insert(
              new Workbook("Module 3", "https://swagmoney.com/billionare/workbooks?module=3", billi)
            );
            workbookDAO.insert(
              new Workbook("Module 1", "https://swagmoney.com/trillionare/workbooks?module=1", trilli)
            );
            workbookDAO.insert(
              new Workbook("Module 2", "https://swagmoney.com/trillionare/workbooks?module=2", trilli)
            );
            workbookDAO.insert(
              new Workbook("Module 3", "https://swagmoney.com/trillionare/workbooks?module=3", trilli)
            );
            workbookDAO.insert(
              new Workbook("Module 1", "https://swagmoney.com/quadrillionare/workbooks?module=1", qudrilli)
            );
            workbookDAO.insert(
              new Workbook("Module 2", "https://swagmoney.com/quadrillionare/workbooks?module=2", qudrilli)
            );
            workbookDAO.insert(
              new Workbook("Module 3", "https://swagmoney.com/quadrillionare/workbooks?module=3", qudrilli)
            );
            return null;
        }
    }
}
