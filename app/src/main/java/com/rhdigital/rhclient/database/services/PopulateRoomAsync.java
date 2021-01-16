package com.rhdigital.rhclient.database.services;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rhdigital.rhclient.common.interfaces.CallableFunction;
import com.rhdigital.rhclient.database.DAO.CourseDAO;
import com.rhdigital.rhclient.database.DAO.CourseDescriptionDAO;
import com.rhdigital.rhclient.database.DAO.ProgramDAO;
import com.rhdigital.rhclient.database.DAO.ReportDAO;
import com.rhdigital.rhclient.database.DAO.VideoDAO;
import com.rhdigital.rhclient.database.DAO.UserDAO;
import com.rhdigital.rhclient.database.DAO.WorkbookDAO;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseDescription;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.database.model.Report;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.model.Video;
import com.rhdigital.rhclient.database.model.Workbook;

import static com.rhdigital.rhclient.database.constants.DatabaseConstants.collections;
import static com.rhdigital.rhclient.database.constants.DatabaseConstants.DAOs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PopulateRoomAsync extends AsyncTask<LinkedHashMap<String, QuerySnapshot>, Void, Void> {
  private String TAG = "POPULATEROOMASYNC";
  // DAO
  private CourseDAO courseDAO;
  private ProgramDAO programDAO;
  private ReportDAO reportDAO;
  private UserDAO userDAO;
  private VideoDAO videoDAO;
  private WorkbookDAO workbookDAO;
  private CourseDescriptionDAO courseDescriptionDAO;
  // FIREBASE
  private FirebaseChainBuilder firebaseChainBuilder = new FirebaseChainBuilder();
  private ExecutorService executorService = Executors.newSingleThreadExecutor();
  private Task firebaseCollectionChain;
  // OBSERVABLE
  private MutableLiveData<ArrayList<Long>> inserts = new MutableLiveData<>();

  public PopulateRoomAsync() { }

  public MutableLiveData<ArrayList<Long>> getInserts() {
    if (inserts == null) {
      inserts = new MutableLiveData<>();
    }
    return inserts;
  }

  private void initialiseDAOs(RHDatabase instance){
    for (String dao: DAOs) {
      try {
        // USE REFLECTION
        Field field = this.getClass().getDeclaredField(dao);
        field.setAccessible(true);
        field.set(this, instance.getClass().getDeclaredMethod(dao).invoke(instance));
      } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
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
    for (String dao: DAOs) {
      try {
        Log.d("DAO", dao);
        // USE REFLECTION
        Field field = (this.getClass().getDeclaredField(dao));
        field.setAccessible(true);
        Object daoClass = field.get(this);
        String className = daoClass.getClass().getCanonicalName();
        daoClass.getClass().getDeclaredMethod("deleteAll").invoke(daoClass);
      } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
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
            insertCourse(doc, pop);
            break;
          case "programs":
            insertProgram(doc, pop);
            break;
          case "reports":
            insertReport(doc, pop);
            break;
          case "users":
            insertUser(doc, pop);
            break;
          case "videos":
            insertVideo(doc, pop);
            break;
          case "workbooks":
            insertWorkbook(doc, pop);
            break;
        }
      }
    }

    // NOTIFY POPULATION EVENT HAS OCCURRED
    this.inserts.postValue(pop);
    return null;
  }

  private void insertProgram(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    Program program = new Program(
            doc.getId(),
            doc.getString("title"),
            doc.getString("type"),
            doc.getDouble("price"),
            doc.getString("posterUrl")
    );
    pop.add(programDAO.insert(
            program
    ));
  }

  private void insertCourse(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    Course course = new Course(
            doc.getId(),
            doc.getString("programId"),
            doc.getString("title"),
            doc.getString("author")
    );
    List<String> descriptions =
            doc.get("descriptions") == null ?
                    Collections.emptyList() :
                    (List<String>) doc.get("descriptions");
    pop.add(courseDAO.insert(
            course
    ));
    for(String description: descriptions) {
      pop.add(courseDescriptionDAO.insert(
              new CourseDescription(
                      UUID.randomUUID().toString(),
                      doc.getId(),
                      description
              )
      ));
    }
  }

  private void insertWorkbook(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    Workbook workbook = new Workbook(
            doc.getId(),
            doc.getString("programId"),
            doc.getString("courseId"),
            doc.getString("title"),
            doc.getString("language"),
            doc.getString("url")
    );
    pop.add(workbookDAO.insert(
            workbook
    ));
  }

  private void insertVideo(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    Video video = new Video(
            doc.getId(),
            doc.getString("programId"),
            doc.getString("courseId"),
            doc.getString("title"),
            doc.getString("language"),
            doc.getString("subtitle"),
            doc.getString("videoUrl"),
            doc.getString("thumbnailUrl")
    );
    pop.add(videoDAO.insert(
            video
    ));
  }

  private void insertReport(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    Report report = new Report(
            doc.getId(),
            doc.getString("programId"),
            doc.getString("title"),
            doc.getString("month"),
            doc.getString("url")
    );
    pop.add(reportDAO.insert(
            report
    ));
  }

  private void insertUser(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
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
  }
}
