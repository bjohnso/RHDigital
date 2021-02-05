package com.rhdigital.rhclient.room.services;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rhdigital.rhclient.common.dto.PopulateRoomDto;
import com.rhdigital.rhclient.common.interfaces.CallableFunction;
import com.rhdigital.rhclient.room.DAO.AuthorisedProgramDAO;
import com.rhdigital.rhclient.room.DAO.AuthorisedReportDAO;
import com.rhdigital.rhclient.room.DAO.CourseDAO;
import com.rhdigital.rhclient.room.DAO.CourseDescriptionDAO;
import com.rhdigital.rhclient.room.DAO.ProgramDAO;
import com.rhdigital.rhclient.room.DAO.ReportDAO;
import com.rhdigital.rhclient.room.DAO.VideoDAO;
import com.rhdigital.rhclient.room.DAO.UserDAO;
import com.rhdigital.rhclient.room.DAO.WorkbookDAO;
import com.rhdigital.rhclient.room.RHDatabase;
import com.rhdigital.rhclient.room.model.AuthorisedProgram;
import com.rhdigital.rhclient.room.model.AuthorisedReport;
import com.rhdigital.rhclient.room.model.Course;
import com.rhdigital.rhclient.room.model.CourseDescription;
import com.rhdigital.rhclient.room.model.Program;
import com.rhdigital.rhclient.room.model.Report;
import com.rhdigital.rhclient.room.model.User;
import com.rhdigital.rhclient.room.model.Video;
import com.rhdigital.rhclient.room.model.Workbook;

import static com.rhdigital.rhclient.room.constants.DatabaseConstants.DAOsALL;
import static com.rhdigital.rhclient.room.constants.DatabaseConstants.DAOsProgramContent;
import static com.rhdigital.rhclient.room.constants.DatabaseConstants.DAOsReports;
import static com.rhdigital.rhclient.room.constants.DatabaseConstants.DAOsUser;
import static com.rhdigital.rhclient.room.constants.DatabaseConstants.collectionsAll;
import static com.rhdigital.rhclient.room.constants.DatabaseConstants.collectionsProgramContent;
import static com.rhdigital.rhclient.room.constants.DatabaseConstants.collectionsReports;
import static com.rhdigital.rhclient.room.constants.DatabaseConstants.collectionsUser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirebaseRoomService implements CallableFunction<Object, Void> {
  private String TAG = "POPULATEROOMASYNC";
  // DAO
  protected CourseDAO courseDAO;
  protected ProgramDAO programDAO;
  protected ReportDAO reportDAO;
  protected UserDAO userDAO;
  protected VideoDAO videoDAO;
  protected WorkbookDAO workbookDAO;
  protected CourseDescriptionDAO courseDescriptionDAO;
  protected AuthorisedProgramDAO authorisedProgramDAO;
  protected AuthorisedReportDAO authorisedReportDAO;
  // FIREBASE
  private FirebaseChainBuilder firebaseChainBuilder = new FirebaseChainBuilder();
  private ExecutorService executorService = Executors.newCachedThreadPool();
  private Task firebaseCollectionChain;
  // OBSERVABLE
  private MutableLiveData<ArrayList<Long>> inserts = new MutableLiveData<>();

  private PopulateRoomDto populateRoom;

  public FirebaseRoomService() { }

  private void initialiseDAOs(RHDatabase instance){
    for (String dao: DAOsALL) {
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

  public MutableLiveData<ArrayList<Long>> populateFromUpstream(RHDatabase instance, PopulateRoomDto populateRoom) {
    this.populateRoom = populateRoom;
    if (inserts == null) {
      inserts = new MutableLiveData<>();
    }
    initialiseDAOs(instance);

    switch(populateRoom.getType()) {
      case PopulateRoomDto.USER:
        this.populateRoom.setCollections(collectionsUser);
        this.populateRoom.setDAOS(DAOsUser);
        break;
      case PopulateRoomDto.REPORTS:
        this.populateRoom.setCollections(collectionsReports);
        this.populateRoom.setDAOS(DAOsReports);
        break;
      case PopulateRoomDto.PROGRAM_CONTENT:
        this.populateRoom.setCollections(collectionsProgramContent);
        this.populateRoom.setDAOS(DAOsProgramContent);
        break;
      case PopulateRoomDto.APP_START:
        this.populateRoom.setCollections(collectionsAll);
        this.populateRoom.setDAOS(DAOsALL);
        break;
    }

    if (populateRoom.getCollections() != null && populateRoom.getDAOS() != null) {
      firebaseCollectionChain = Tasks.call(executorService, CallableFunction.callable(firebaseChainBuilder, populateRoom.getCollections()[0]));
      for (int i = 1; i < populateRoom.getCollections().length; i++) {
        firebaseCollectionChain = firebaseCollectionChain
                .continueWith(
                        executorService,
                        CallableFunction
                                .continuation(firebaseChainBuilder,
                                        populateRoom.getCollections()[i],
                                        firebaseCollectionChain
                                )
                );
      }
      firebaseCollectionChain.addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          firebaseCollectionChain = Tasks.call(
                  executorService,
                  CallableFunction.callable(this, firebaseChainBuilder.getQuerySnapshotMap())
          );
        } else {
          Log.d(TAG, "FAILED :" + task.getException().getMessage());
        }
      });
    }
    return inserts;
  }

  public void deleteFromLocal(String[] DAOs) {
    for (String dao: DAOs) {
      try {
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

  private void insertProgram(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    Program program = new Program(
            doc.getId(),
            Objects.requireNonNull(doc.getString("title")),
            Objects.requireNonNull(doc.getString("type")),
            Objects.requireNonNull(doc.getDouble("price")),
            Objects.requireNonNull(doc.getString("posterUrl"))
    );
    pop.add(programDAO.insert(
            program
    ));
  }

  private void insertCourse(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    Course course = new Course(
            doc.getId(),
            Objects.requireNonNull(doc.getString("programId")),
            Objects.requireNonNull(doc.getString("title")),
            Objects.requireNonNull(doc.getString("author"))
    );
    pop.add(courseDAO.insert(
            course
    ));
    insertCourseDescriptions(doc, pop);
  }

  private void insertCourseDescriptions(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    List<String> descriptions =
            doc.get("descriptions") == null ?
                    Collections.emptyList() :
                    (List<String>) doc.get("descriptions");
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
            Objects.requireNonNull(doc.getString("programId")),
            Objects.requireNonNull(doc.getString("courseId")),
            Objects.requireNonNull(doc.getString("title")),
            Objects.requireNonNull(doc.getString("language")),
            Objects.requireNonNull(doc.getString("url"))
    );
    pop.add(workbookDAO.insert(
            workbook
    ));
  }

  private void insertVideo(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    Video video = new Video(
            doc.getId(),
            Objects.requireNonNull(doc.getString("programId")),
            Objects.requireNonNull(doc.getString("courseId")),
            Objects.requireNonNull(doc.getString("title")),
            Objects.requireNonNull(doc.getString("language")),
            doc.getString("subtitle"),
            Objects.requireNonNull(doc.getString("videoUrl")),
            doc.getString("thumbnailUrl")
    );
    pop.add(videoDAO.insert(
            video
    ));
  }

  private void insertReport(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    Report report = new Report(
            doc.getId(),
            Objects.requireNonNull(doc.getString("programId")),
            Objects.requireNonNull(doc.getString("title")),
            Objects.requireNonNull(doc.getString("month")),
            Objects.requireNonNull(doc.getString("url"))
    );
    pop.add(reportDAO.insert(
            report
    ));
  }

  private void insertUser(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    User user = new User(
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
    );
    pop.add(userDAO.insert(
            user
    ));
    insertAuthorisedPrograms(doc, pop);
    insertAuthorisedReports(doc, pop);
  }

  private void insertAuthorisedPrograms(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    List<String> authorisedPrograms =
            doc.get("authorisedPrograms") == null ?
                    Collections.emptyList() :
                    (List<String>) doc.get("authorisedPrograms");
    for (String id : authorisedPrograms) {
      pop.add(authorisedProgramDAO.insert(
              new AuthorisedProgram(id)
      ));
    }
  }

  private void insertAuthorisedReports(QueryDocumentSnapshot doc, ArrayList<Long> pop) {
    List<String> authorisedReports =
            doc.get("authorisedReports") == null ?
                    Collections.emptyList() :
                    (List<String>) doc.get("authorisedReports");
    for (String id : authorisedReports) {
      pop.add(authorisedReportDAO.insert(
              new AuthorisedReport(id)
      ));
    }
  }

  @Override
  public Void call(Object... args) throws Exception {
    LinkedHashMap<String, QuerySnapshot> fireStoreData = null;
    ArrayList<Long> pop = null;
    try {
      fireStoreData = (LinkedHashMap<String, QuerySnapshot>)args[0];
      deleteFromLocal(populateRoom.getDAOS());
    } catch (Exception e) {
      Log.e("PopulateRoom", e.getMessage());
    }

    if (fireStoreData != null) {
      Log.d(TAG, "POPULATING DB... " + fireStoreData);
      pop = new ArrayList<>();

      Iterator<Map.Entry<String, QuerySnapshot>> it = fireStoreData.entrySet().iterator();
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
    }
    // NOTIFY POPULATION EVENT HAS OCCURRED
    this.inserts.postValue(pop);
    return null;
  }

  @Override
  public Void then(Object... args) throws Exception {
    return null;
  }
}
