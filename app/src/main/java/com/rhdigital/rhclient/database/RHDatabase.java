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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Database(entities = {Course.class, Workbook.class}, version = 1, exportSchema = false)
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
            .addOnCompleteListener(courseTask -> {
              if (courseTask.isSuccessful()) {
                firestore.collectionGroup("workbooks")
                  .get()
                  .addOnCompleteListener(workbookTask -> {
                    if (workbookTask.isSuccessful()) {
                      new PopulateDbAsync(
                        INSTANCE,
                        courseTask.getResult(),
                        workbookTask.getResult()).execute();
                    }
                  });
              }
            });
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final CourseDAO courseDAO;
        private final WorkbookDAO workbookDAO;
        private final CourseWithWorkbooksDAO courseWithWorkbooksDAO;
        private final QuerySnapshot[] fireStoreData;

        public PopulateDbAsync(RHDatabase instance, QuerySnapshot... snapshots) {
            courseDAO = instance.courseDAO();
            workbookDAO = instance.workbookDAO();
            courseWithWorkbooksDAO = instance.courseWithWorkbooksDAO();
            this.fireStoreData = snapshots;
        }

        @Override
        protected Void doInBackground(Void... voids) {
          workbookDAO.deleteAll();
          courseDAO.deleteAll();

          for (QueryDocumentSnapshot doc : fireStoreData[0]) {
            courseDAO.insert(
              new Course(
                doc.getId(),
                doc.get("name").toString(),
                doc.get("description").toString(),
                doc.get("thumbnailURL").toString(),
                doc.get("videoURL").toString()
              )
            );
            Log.d("Firestore", "Course: " + doc.getId());
            Log.d("Firestore", "Course: " + doc.getData().toString());
          }

          for (QueryDocumentSnapshot doc : fireStoreData[1]) {
            workbookDAO.insert(
              new Workbook(
                doc.getId(),
                doc.get("name").toString(),
                doc.get("workbookURL").toString(),
                doc.get("courseId").toString()
              )
            );
            Log.d("Firestore", "Workbook: " + doc.getId());
            Log.d("Firestore", "Workbook: " + doc.getData().toString());
          }

          return null;
        }
    }
}
