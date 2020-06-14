package com.rhdigital.rhclient.database.util;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rhdigital.rhclient.database.DAO.CourseDAO;
import com.rhdigital.rhclient.database.DAO.CourseWithWorkbooksDAO;
import com.rhdigital.rhclient.database.DAO.UserDAO;
import com.rhdigital.rhclient.database.DAO.WorkbookDAO;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.Workbook;

import java.util.ArrayList;
import java.util.List;

public class PopulateRoomAsync extends AsyncTask<Void, Void, Void> {
  private CourseDAO courseDAO;
  private WorkbookDAO workbookDAO;
  private UserDAO userDAO;
  private CourseWithWorkbooksDAO courseWithWorkbooksDAO;
  private QuerySnapshot[] fireStoreData;
  private MutableLiveData<ArrayList<Long>> inserts;

  public PopulateRoomAsync() { }

  public MutableLiveData<ArrayList<Long>> getInserts() {
    if (inserts == null) {
      inserts = new MutableLiveData<>();
    }
    return inserts;
  }

  private void setFireStoreData (QuerySnapshot... snapshots) {
    this.fireStoreData = snapshots;
  }

  //TODO: CREATE MODEL FOR COURSEPACKGES

  public void populateFromUpstream(RHDatabase instance) {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    firestore
      .collection("course_packages")
      .get()
      .addOnCompleteListener(coursePackagesTask -> {
        if (coursePackagesTask.isSuccessful()) {
          firestore
            .collectionGroup("courses")
            .get()
            .addOnCompleteListener(coursesTask -> {
              if (coursesTask.isSuccessful()) {
                firestore
                  .collectionGroup("workbooks")
                  .get()
                  .addOnCompleteListener(workbooksTask -> {
                    if (workbooksTask.isSuccessful()) {
                      courseDAO = instance.courseDAO();
                      workbookDAO = instance.workbookDAO();
                      userDAO = instance.userDAO();
                      courseWithWorkbooksDAO = instance.courseWithWorkbooksDAO();
                      setFireStoreData(
                        coursePackagesTask.getResult(),
                        coursePackagesTask.getResult(),
                        workbooksTask.getResult());
                      this.execute();
                    }
                  });
            }
            });
        }
      });
  }

  @Override
  protected Void doInBackground(Void... voids) {
    Log.d("POP", "POPULATED DB");
    workbookDAO.deleteAll();
    courseDAO.deleteAll();
    userDAO.deleteAll();

    ArrayList<Long> pop = new ArrayList<>();

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
    this.inserts.postValue(pop);
    return null;
  }
}
