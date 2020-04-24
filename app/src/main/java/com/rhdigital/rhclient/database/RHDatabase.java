package com.rhdigital.rhclient.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rhdigital.rhclient.database.DAO.CourseDAO;
import com.rhdigital.rhclient.database.DAO.CourseWithWorkbooksDAO;
import com.rhdigital.rhclient.database.DAO.WorkbookDAO;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.Workbook;

import java.util.List;

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

          FirebaseFirestore firestore = FirebaseFirestore.getInstance();
          firestore.collection("courses")
            .get()
            .addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                // TESTING FIRESTORE DATA
                for (QueryDocumentSnapshot doc : task.getResult()) {
                  Log.d("FIREBASE", doc.getData().toString());
                }
                new PopulateDbAsync(INSTANCE, task.getResult()).execute();
              }
            });
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final CourseDAO courseDAO;
        private final WorkbookDAO workbookDAO;
        private final CourseWithWorkbooksDAO courseWithWorkbooksDAO;
        private final QuerySnapshot fireStoreData;

        public PopulateDbAsync(RHDatabase instance, QuerySnapshot fireStoreData) {
            courseDAO = instance.courseDAO();
            workbookDAO = instance.workbookDAO();
            courseWithWorkbooksDAO = instance.courseWithWorkbooksDAO();
            this.fireStoreData = fireStoreData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
          workbookDAO.deleteAll();
          courseDAO.deleteAll();

          for (QueryDocumentSnapshot doc : fireStoreData) {
            courseDAO.insert(
              new Course(
                doc.getId(),
                doc.get("name").toString(),
                doc.get("description").toString(),
                doc.get("thumbnailURL").toString(),
                doc.get("videoURL").toString()
              )
            );
          }
//            long milli = courseDAO.insert(
//                    new Course("How to be a Millionare",
//                            "Becoming super Swag easy!",
//                            "rh_poster_3",
//                            "https://swagmoney.com/millionare")
//            );
//            long billi = courseDAO.insert(
//                    new Course("How to be a Billionare",
//                            "Becoming super Swag easy!",
//                            "rh_poster_1",
//                            "https://swagmoney.com/billionare")
//            );
//            long trilli = courseDAO.insert(
//                    new Course("How to be a Trillionare",
//                            "Becoming super Swag easy!",
//                            "rh_poster_3",
//                            "https://swagmoney.com/trillionare")
//            );
//            long quadrilli = courseDAO.insert(
//                    new Course("How to be a Quadrillionare",
//                            "Becoming super Swag easy!",
//                            "rh_poster_1",
//                            "https://swagmoney.com/quadrillionare")
//            );
//            long quintilli = courseDAO.insert(
//                    new Course("How to be a Quintilli",
//                      "Becoming super Swag easy!",
//                      "rh_poster_3",
//                      "https://swagmoney.com/quintillionare")
//            );
//
//            //WORKBOOKS
//            workbookDAO.insert(
//              new Workbook("Module 1", "https://swagmoney.com/millionare/workbooks?module=1", milli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 2", "https://swagmoney.com/millionare/workbooks?module=2", milli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 3", "https://swagmoney.com/millionare/workbooks?module=3", milli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 1", "https://swagmoney.com/billionare/workbooks?module=1", billi)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 2", "https://swagmoney.com/billionare/workbooks?module=2", billi)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 3", "https://swagmoney.com/billionare/workbooks?module=3", billi)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 1", "https://swagmoney.com/trillionare/workbooks?module=1", trilli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 2", "https://swagmoney.com/trillionare/workbooks?module=2", trilli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 3", "https://swagmoney.com/trillionare/workbooks?module=3", trilli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 1", "https://swagmoney.com/quadrillionare/workbooks?module=1", quadrilli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 2", "https://swagmoney.com/quadrillionare/workbooks?module=2", quadrilli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 3", "https://swagmoney.com/quadrillionare/workbooks?module=3", quadrilli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 1", "https://swagmoney.com/quintillion/workbooks?module=1", quintilli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 2", "https://swagmoney.com/quintillion/workbooks?module=2", quintilli)
//            );
//            workbookDAO.insert(
//              new Workbook("Module 3", "https://swagmoney.com/quintillion/workbooks?module=3", quintilli)
//            );
            return null;
        }
    }
}
