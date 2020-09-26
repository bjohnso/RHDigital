package com.rhdigital.rhclient.database.services;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PopulateRoomAsync extends AsyncTask<LinkedHashMap<String, QuerySnapshot>, Void, Void> {
  private String TAG = "POPULATEROOMASYNC";
  // DAO
  private CourseDAO courseDAO;
  private PackageDAO packageDAO;
  private ReportDAO reportDAO;
  private UserDAO userDAO;
  private VideoDAO videoDAO;
  private WorkbookDAO workbookDAO;
  // FIREBASE
  private FirebaseChainBuilder firebaseChainBuilder = new FirebaseChainBuilder();
  private ExecutorService executorService = Executors.newSingleThreadExecutor();
  private Task firebaseCollectionChain;
  // OBSERVABLE
  private MutableLiveData<ArrayList<Long>> inserts;

  public PopulateRoomAsync() { }

  public MutableLiveData<ArrayList<Long>> getInserts() {
    if (inserts == null) {
      inserts = new MutableLiveData<>();
    }
    return inserts;
  }

  private void initialiseDAOs(RHDatabase instance){
    courseDAO = instance.courseDAO();
    packageDAO = instance.packageDAO();
    reportDAO = instance.reportDAO();
    userDAO = instance.userDAO();
    videoDAO = instance.videoDAO();
    workbookDAO = instance.workbookDAO();
//    for (String collection: collections) {
//      try {
//        Log.d(TAG, "FIELD : " + collection + "DAO");
//        Field field = this.getClass().getDeclaredField(collection + "DAO");
//        field.setAccessible(true);
//        for (Method method: instance.getClass().getDeclaredMethods()) {
//          if (method.getName().contains("DAO")) {
//            Log.d(TAG, "INSTANCE : " + method.invoke(instance).toString());
//          }
//        }
//        field.set(this, instance.getClass().getDeclaredMethod(collection + "DAO").getDefaultValue());
//      } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//        e.printStackTrace();
//      }
//    }
  }

  public void populateFromUpstream(RHDatabase instance) {
    initialiseDAOs(instance);
    firebaseCollectionChain = Tasks.call(executorService, CallableFunction.callable(firebaseChainBuilder, collections[0]));
    for (int i = 1; i < collections.length; i++) {
      firebaseCollectionChain = firebaseCollectionChain
        .continueWith(
          executorService,
          CallableFunction
            .continuation(firebaseChainBuilder,
              collections[i],
              firebaseCollectionChain
            )
        );
    }
    firebaseCollectionChain.addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        this.execute(firebaseChainBuilder.getQuerySnapshotMap());
      } else {
        Log.d(TAG, "FAILED :" + task.getException().getMessage());
      }
    });
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
  protected Void doInBackground(LinkedHashMap<String, QuerySnapshot>... fireStoreData) {
    Log.d(TAG, "POPULATING DB... " + fireStoreData[0]);

    deleteAllFromLocal();

    ArrayList<Long> pop = new ArrayList<>();

    Iterator<Map.Entry<String, QuerySnapshot>> it = fireStoreData[0].entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, QuerySnapshot> pair = it.next();
      Log.d(TAG, "Collection : " + pair.getKey());

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
            break;
          case "packages":
            pop.add(packageDAO.insert(
              new Package(
                doc.getId(),
                doc.getString("title"),
                doc.getString("type"),
                doc.getDouble("price")
              )
            ));
            break;
          case "reports":
            pop.add(reportDAO.insert(
              new Report(
                doc.getId(),
                doc.getString("packageId"),
                doc.getString("month"),
                doc.getString("url")
              )
            ));
            break;
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
            break;
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
            break;
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
            break;
        }
      }
    }

    // NOTIFY POPULATION EVENT HAS OCCURRED
    //this.inserts.postValue(pop);
    return null;
  }
}
