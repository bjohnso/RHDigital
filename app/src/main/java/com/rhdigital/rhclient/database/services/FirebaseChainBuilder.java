package com.rhdigital.rhclient.database.services;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseChainBuilder implements CallableFunction<Object, Object> {

  private LinkedHashMap<String, QuerySnapshot> querySnapshotMap = new LinkedHashMap<>();
  private String collectionKey;

  @Override
  public Task<QuerySnapshot> call(Object... args) throws Exception {
    Semaphore semaphore = new Semaphore(0);
    AtomicReference<Task<QuerySnapshot>> querySnapshotTask = new AtomicReference<>();
    collectionKey = (String) args[0];
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    firestore.collection(collectionKey)
      .get()
      .addOnCompleteListener(task -> {
        querySnapshotMap.put(collectionKey, task.getResult());
        querySnapshotTask.set(task);
        semaphore.release();
      });
    semaphore.acquire();
    return querySnapshotTask.get();
  }

  @Override
  public Task<QuerySnapshot> then(Object... args) throws Exception {
    Task<QuerySnapshot> task = (Task)args[1];
    String collection = (String) args[0];

    if (task.isSuccessful()) {
      return this.call(collection);
    }
    Log.d("TASK", "TASK FAILED :  " + task.getException().getMessage());
    return null;
  }

  public LinkedHashMap<String, QuerySnapshot> getQuerySnapshotMap() {
    return querySnapshotMap;
  }
}
