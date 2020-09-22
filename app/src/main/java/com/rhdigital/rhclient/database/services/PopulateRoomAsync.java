package com.rhdigital.rhclient.database.services;

import android.os.AsyncTask;
import android.util.Log;

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
import com.rhdigital.rhclient.database.model.Package;
import com.rhdigital.rhclient.database.model.Workbook;

import static com.rhdigital.rhclient.database.constants.Collections.collections;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PopulateRoomAsync extends AsyncTask<Void, Void, Void> {
  private String TAG = "POPULATEROOMASYNC";
  private CourseDAO courseDAO;
  private PackageDAO packageDAO;
  private ReportDAO reportDAO;
  private UserDAO userDAO;
  private VideoDAO videoDAO;
  private WorkbookDAO workbookDAO;
  private ArrayList<QuerySnapshot> fireStoreData = new ArrayList<>();
  private MutableLiveData<ArrayList<Long>> inserts;

  public PopulateRoomAsync() { }

  public MutableLiveData<ArrayList<Long>> getInserts() {
    if (inserts == null) {
      inserts = new MutableLiveData<>();
    }
    return inserts;
  }

  private void updateFireStoreData (QuerySnapshot... snapshots) {
    for (QuerySnapshot snapshot: snapshots) {
      this.fireStoreData.add(snapshot);
    }
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
            updateFireStoreData(snapshot);
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

    for (String collection: collections) {
      try {
        BaseDAO baseDAO = (BaseDAO) this.getClass().getDeclaredField(collection + "DAO").get(this);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        e.printStackTrace();
      }

    }

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

    for (QueryDocumentSnapshot doc : fireStoreData[0]) {
      pop.add(packageDAO.insert(
        new Package(
          doc.getId(),
          doc.getString("title"),
          doc.getString("packageClass"),
          doc.getDouble("price")
        )
      ));
    }

    for (QueryDocumentSnapshot doc : fireStoreData[1]) {
      pop.add(workbookDAO.insert(
        new Workbook(
          doc.getId(),
          doc.getString("title"),
          doc.getString("packageClass"),
          doc.getDouble("price")
        )
      ));
    }

    for (QueryDocumentSnapshot doc : fireStoreData[2]) {
      pop.add(packageDAO.insert(
        new Package(
          doc.getId(),
          doc.getString("title"),
          doc.getString("language"),
          doc.getDouble("price")
        )
      ));
    }

    for (QueryDocumentSnapshot doc : fireStoreData[0]) {
      pop.add(courseDAO.insert(
        new Course(
          doc.getId(),
          doc.get("name").toString(),
          doc.get("author").toString(),
          doc.get("description").toString(),
          doc.get("videoPosterURL").toString(),
          doc.get("workbookPosterURL").toString(),
          doc.get("videoURL").toString()
        ))
      );
    }

    for (QueryDocumentSnapshot doc : fireStoreData[1]) {
      pop.add(workbookDAO.insert(
        new Workbook(
          doc.getId(),
          doc.get("name").toString(),
          doc.get("workbookURL").toString(),
          doc.get("courseId").toString()
        ))
      );
    }

    // NOTIFY POPULATION EVENT HAS OCCURRED
    this.inserts.postValue(pop);
    return null;
  }
}
