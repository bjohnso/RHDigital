package com.rhdigital.rhclient.database.services;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rhdigital.rhclient.database.DAO.BaseDAO;
import com.rhdigital.rhclient.database.DAO.CourseDAO;
import com.rhdigital.rhclient.database.DAO.ReportDAO;
import com.rhdigital.rhclient.database.DAO.VideoDAO;
import com.rhdigital.rhclient.database.DAO.PackageDAO;
import com.rhdigital.rhclient.database.DAO.UserDAO;
import com.rhdigital.rhclient.database.DAO.WorkbookDAO;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.Package;
import com.rhdigital.rhclient.database.model.Report;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.model.Video;
import com.rhdigital.rhclient.database.model.Workbook;

import static com.rhdigital.rhclient.database.constants.Collections.collections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PopulateRoomAsync extends AsyncTask<Void, Void, Void> {
  private String TAG = "POPULATEROOMASYNC";
  private CourseDAO courseDAO;
  private PackageDAO packageDAO;
  private ReportDAO reportDAO;
  private UserDAO userDAO;
  private VideoDAO videoDAO;
  private WorkbookDAO workbookDAO;
  private HashMap<String, QuerySnapshot> fireStoreData = new HashMap<>();
  private MutableLiveData<ArrayList<Long>> inserts;

  public PopulateRoomAsync() { }

  public MutableLiveData<ArrayList<Long>> getInserts() {
    if (inserts == null) {
      inserts = new MutableLiveData<>();
    }
    return inserts;
  }

  private void updateFireStoreData (String collection, QuerySnapshot snapshot) {
    this.fireStoreData.put(collection, snapshot);
  }

  public void populateFromUpstream(RHDatabase instance) {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    for (String collection: collections) {
      firestore
        .collection(collection)
        .get()
        .addOnSuccessListener(snapshot -> {
          try {
            Field field = this.getClass().getDeclaredField(collection + "DAO");
            field.setAccessible(true);
            field.set(this, instance.getClass().getDeclaredField(collection + "DAO"));
            updateFireStoreData(collection, snapshot);
            if (collection == collections[collections.length - 1]) {
              this.execute();
            }
          } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
          }
        });
    }
  }

  public void deleteAllFromLocal() {
    courseDAO.deleteAll();
    packageDAO.deleteAll();
    reportDAO.deleteAll();
    userDAO.deleteAll();
    videoDAO.deleteAll();
    workbookDAO.deleteAll();
  }

  @Override
  protected Void doInBackground(Void... voids) {
    Log.d(TAG, "POPULATING DB...");

    deleteAllFromLocal();

    ArrayList<Long> pop = new ArrayList<>();

    Iterator<Map.Entry<String, QuerySnapshot>> it = fireStoreData.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, QuerySnapshot> pair = it.next();

      for (QueryDocumentSnapshot doc : pair.getValue()) {
        switch (pair.getKey()) {
          case "courses":
            pop.add(courseDAO.insert(
              new Course(
                doc.getId(),
                doc.getString("packageId"),
                doc.getString("title"),
                doc.getString("author"),
                doc.getString("description"),
                doc.getString("posterURL")
              )
            ));
          case "packages":
            pop.add(packageDAO.insert(
              new Package(
                doc.getId(),
                doc.getString("title"),
                doc.getString("type"),
                doc.getDouble("price")
              )
            ));
          case "reports":
            pop.add(reportDAO.insert(
              new Report(
                doc.getId(),
                doc.getString("packageId"),
                doc.getString("month"),
                doc.getString("url")
              )
            ));
          case "users":
            pop.add(userDAO.insert(
              new User(
                doc.getId(),
                doc.getString("email"),
                doc.getString("cell"),
                doc.getString("name"),
                doc.getString("surname"),
                doc.getString("title"),
                doc.getString("city"),
                doc.getString("country"),
                doc.getString("industry"),
                doc.getString("about")
              )
            ));
          case "videos":
            pop.add(videoDAO.insert(
              new Video(
                doc.getId(),
                doc.getString("courseId"),
                doc.getString("title"),
                doc.getString("language"),
                doc.getString("subtitle"),
                doc.getString("url")
              )
            ));
          case "workbooks":
            pop.add(workbookDAO.insert(
              new Workbook(
                doc.getId(),
                doc.getString("courseId"),
                doc.getString("title"),
                doc.getString("language"),
                doc.getString("url")
              )
            ));
        }
      }

    }

    // NOTIFY POPULATION EVENT HAS OCCURRED
    this.inserts.postValue(pop);
    return null;
  }
}
