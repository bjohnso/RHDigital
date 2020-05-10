package com.rhdigital.rhclient.activities.auth.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.repository.RHRepository;
import com.rhdigital.rhclient.database.util.PopulateRoomAsync;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Authenticator {

  private MutableLiveData<FirebaseUser> firebaseUser;
  private FirebaseAuth firebaseAuth;
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private RHRepository rhRepository;
  private Context context;

  public Authenticator(Context context) {
    this.context = context;
    rhRepository = new RHRepository(((Activity) context).getApplication());
    firebaseAuth = FirebaseAuth.getInstance();
  }

  // Utility to update Auth status of courses belonging to this user
  public void authoriseCourses(QuerySnapshot snapshot) {
    for (QueryDocumentSnapshot doc : snapshot) {
      rhRepository.authoriseCourse(doc.getId());
    }
  }

  //RePopulates Room
  public MutableLiveData<ArrayList<Long>> populateRoomFromUpstream() {
    PopulateRoomAsync
      .getInstance()
      .populateFromUpstream(RHDatabase.getDatabase(context));
    return PopulateRoomAsync.getInstance().getInserts();
  }

  // This Method must be called upon registering of MutableLiveData to confirm user initialisation in users node
  // -inserts newly created user upstream if none exists and persists the user locally as well,
  // Updates the authorisation status of the local store of courses for the newly created user
  public void postAuthenticate(FirebaseUser firebaseUser, Intent intent) {
    db.collection("users")
      .document(firebaseUser.getUid()).get().addOnCompleteListener(userTask -> {
        if (userTask.isSuccessful()) {
          Log.d("AUTH", userTask.getResult().getId());
          User user = new User(firebaseUser.getUid(),
            "test",
            firebaseUser.getEmail(),
            "test",
            "test",
            "test");
          if (!userTask.getResult().exists()) {
            insertUserUpstream(user);
          }
          insertLocal(user);
          userTask.getResult().getReference()
            .collection("myCourses")
            .get()
            .addOnCompleteListener(courseTask -> {
              if (courseTask.isSuccessful()) {
                authoriseCourses(courseTask.getResult());
                context.startActivity(intent);
              }
            });
        }
      });
  }

  // Utility to upstream the newly created user
  private void insertUserUpstream(User user) {
    db.collection("users")
      .document(user.getId())
      .set(user);
  }

  // utility to persist locally the newly pulled or created user
  private void insertLocal(User user) {
    rhRepository.insert(user);
  }

  // STRATEGIES
  // EMAIL & PASSWORD

  // Email Registration Entry Point - This method updates MutableLiveData when successful
  public void registerEmail(String email, String password) {
    if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
      firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(task -> {
          if (task.isSuccessful())
            firebaseUser.setValue(firebaseAuth.getCurrentUser());
          else {
            if (password.length() < 6) {
              Toast.makeText(
                this.context,
                "Sorry, your password needs to be at least 6 characters. Please try again.",
                Toast.LENGTH_LONG).show();
            } else {
              Toast.makeText(
                this.context,
                "Oops, An unknown error has occurred. This incident has been reported to the RHDigital Team. Please try again later.",
                Toast.LENGTH_LONG).show();
            }
          }
        });
    } else {
      Toast.makeText(this.context,
        "Please enter a valid email address and password.",
        Toast.LENGTH_LONG).show();
    }
  }

  // Email Authentication Entry Point - This method updates MutableLiveData when successful
  public void authenticateEmail(String email, String password) {
    if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
      firebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(task -> {
          if (task.isSuccessful())
            firebaseUser.setValue(firebaseAuth.getCurrentUser());
          else {
            if (password.length() < 6) {
              Toast.makeText(this.context,
                "Sorry, your password needs to be at least 6 characters. Please try again.",
                Toast.LENGTH_LONG).show();
            } else {
              Toast.makeText(this.context,
                task.getException().getMessage(),
                Toast.LENGTH_LONG).show();
            }
            firebaseUser.setValue(null);
          }
        });
    } else {
      Toast.makeText(this.context,
        "Please enter a valid email address and password.",
        Toast.LENGTH_LONG).show();
    }
  }

  // PHONE NUMBER & PASSWORD

  // Phone Authentication handler - This method updates MutableLiveData when successful
  private void authenticatePhone(PhoneAuthCredential phoneAuthCredential) {
    firebaseAuth.signInWithCredential(phoneAuthCredential)
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          firebaseUser.setValue(task.getResult().getUser());
        } else {
          Toast.makeText(context,
            task.getException().getMessage(),
            Toast.LENGTH_LONG).show();
        }
      });
  }

  // Phone Authentication Entry Point - This method sends an OTP request to the user
  public void authenticatePhone(String country, String number) {
    if (country != null && number != null && !country.isEmpty() && !number.isEmpty()) {
      String complete = country + number;
      PhoneAuthProvider.getInstance().verifyPhoneNumber(
        complete,
        30,
        TimeUnit.SECONDS,
        (Activity) this.context,
        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
          @Override
          public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            authenticatePhone(phoneAuthCredential);
          }

          @Override
          public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(context,
              e.getMessage(),
              Toast.LENGTH_LONG).show();
          }

          @Override
          public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Toast.makeText(context,
              "BAM",
              Toast.LENGTH_LONG).show();
          }
        }
      );
    } else {
      Toast.makeText(this.context,
        "PLease enter a valid cell phone number.",
        Toast.LENGTH_LONG).show();
    }
  }

  public MutableLiveData<FirebaseUser> getFirebaseUser() {
    if (firebaseUser == null) {
      firebaseUser = new MutableLiveData<>();
    }
    return firebaseUser;
  }
}
