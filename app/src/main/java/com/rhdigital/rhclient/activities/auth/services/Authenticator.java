package com.rhdigital.rhclient.activities.auth.services;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.services.Toaster;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.repository.RHRepository;
import com.rhdigital.rhclient.database.services.PopulateRoomAsync;

import java.util.ArrayList;

public class Authenticator {
  private FirebaseAuth firebaseAuth;
  private FirebaseFirestore db;
  private RHRepository rhRepository;
  private Context context;

  private MutableLiveData<Boolean> launchReadyObservable;

  private static Authenticator INSTANCE;

  private Authenticator() {
  }

  public static Authenticator getInstance() {
    if(INSTANCE == null) {
      INSTANCE = new Authenticator();
    }
    return INSTANCE;
  }

  public void init(Context context) {
    this.context = context;
    rhRepository = new RHRepository(((Activity) context).getApplication());
    firebaseAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();
    launchReadyObservable = new MutableLiveData<>();
  }

  public MutableLiveData<Boolean> getLaunchReadyObservable() {
    return launchReadyObservable;
  }

  //Sign In Email
  public LiveData<Boolean> authenticate(String email, String password, boolean isReauth) {
    MutableLiveData<Boolean> isAuthenticationSuccessful = new MutableLiveData<>();
    if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
      firebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            isAuthenticationSuccessful.setValue(true);
            if (!isReauth) {
                LiveData<ArrayList<Long>> populateRoomObservable = Authenticator.getInstance().populateRoomFromUpstream();
                populateRoomObservable.observe((LifecycleOwner) context, ids -> {
                if (ids != null && ids.size() > 0) {
                  populateRoomObservable.removeObservers((LifecycleOwner) context);
                  postAuthenticate(task.getResult().getUser(), null, null);
                }
              });
            }
          }
          else {
            if (password.length() < 6) {
              Toaster.getINSTANCE()
                .ToastMessage(context.getResources().getString(R.string.user_error_password_length), true);
            } else {
              Toast.makeText(this.context,
                task.getException().getMessage(),
                Toast.LENGTH_LONG).show();
            }
            isAuthenticationSuccessful.setValue(false);
          }
        });
    } else {
      isAuthenticationSuccessful.setValue(false);
      Toaster.getINSTANCE()
        .ToastMessage(context.getResources().getString(R.string.user_error_auth_credentials), true);
    }
    return isAuthenticationSuccessful;
  }

  // This Method must be called upon registering of MutableLiveData to confirm user initialisation in users node
  // -inserts newly created user upstream if none exists and persists the user locally as well,
  public void postAuthenticate(FirebaseUser firebaseUser, String firstName, String lastName) {
    db.collection("users")
      .document(firebaseUser.getUid())
      .get()
      .addOnCompleteListener(userTask -> {
        if (userTask.isSuccessful()) {
          if (!userTask.getResult().exists()) {
            //Create Brand New User
            User user = new User(firebaseUser.getUid(),
              firebaseUser.getEmail(),
              "",
              firstName,
              lastName,
              "",
              "",
              "",
              "",
              "");
            insertUserUpstream(user);
            insertLocal(user);
          } else {
            // Fetch Existing User From Upstream
            db.collection("users")
              .document(firebaseUser.getUid())
              .get()
              .addOnSuccessListener(doc -> {
                User user = new User(
                  doc.get("id").toString(),
                  doc.get("email").toString(),
                  doc.get("cell").toString(),
                  doc.get("name").toString(),
                  doc.get("surname").toString(),
                  doc.get("title").toString(),
                  doc.get("city").toString(),
                  doc.get("country").toString(),
                  doc.get("industry").toString(),
                  doc.get("about").toString()
                );
                insertLocal(user);
              });
          }
          authoriseCourses(userTask.getResult().getId());
        }
      });
  }

  // Email Registration Entry Point - This method updates MutableLiveData when successful
  public LiveData<Boolean> register(String email, String password, String firstName, String lastName) {
    MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>();
    if (email != null && password != null &&
      !email.isEmpty() && !password.isEmpty() &&
      firstName != null && !firstName.isEmpty() &&
      lastName != null && !lastName.isEmpty()) {
      firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            //Send User a verification email
            task.getResult()
              .getUser()
              .sendEmailVerification()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                  registrationSuccess.setValue(true);
                } else {
                  registrationSuccess.setValue(false);
                  Toaster.getINSTANCE()
                    .ToastMessage(context.getResources().getString(R.string.server_error_registration), true);
                }
              }
            });

            LiveData<ArrayList<Long>> populateRoomObservable = Authenticator.getInstance().populateRoomFromUpstream();
            populateRoomObservable.observe((LifecycleOwner) context, ids -> {
              if (ids != null && ids.size() > 0) {
                populateRoomObservable.removeObservers((LifecycleOwner) context);
                postAuthenticate(task.getResult().getUser(), firstName, lastName);
              }
            });
          }
          else {
            registrationSuccess.setValue(false);
            Toaster.getINSTANCE()
              .ToastMessage(context.getResources().getString(R.string.server_error_registration), true);
          }
        });
    }
    return registrationSuccess;
  }

  //UTILITIES

  // Updates the authorisation status of the local store of courses for the newly created user
  public void authoriseCourses(String userId) {
    db.collection("users")
      .document(userId)
      .collection("myCourses")
      .get()
      .addOnCompleteListener(courseTask -> {
        if (courseTask.isSuccessful()) {
          authoriseCourses(courseTask.getResult());
          launchReadyObservable.setValue(true);
        } else {
          launchReadyObservable.setValue(false);
        }
      });
  }

  // Utility to update Auth status of courses belonging to this user
  public void authoriseCourses(QuerySnapshot snapshot) {
    for (QueryDocumentSnapshot doc : snapshot) {
      rhRepository.authoriseCourse(doc.getId());
    }
  }

  //RePopulates Room
  public MutableLiveData<ArrayList<Long>> populateRoomFromUpstream() {
    PopulateRoomAsync populateRoomAsync = new PopulateRoomAsync();
    populateRoomAsync.populateFromUpstream(RHDatabase.getDatabase(context));
    return populateRoomAsync.getInserts();
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

  public LiveData<Boolean> isExistingUserEmail(String email) {
    MutableLiveData<Boolean> isExitingUser = new MutableLiveData<>();
    db.collection("users")
      .whereEqualTo("email", email)
      .get()
      .addOnCompleteListener(task -> {
        if (task.getResult().isEmpty()) {
          isExitingUser.setValue(false);
        } else {
          isExitingUser.setValue(true);
        }
      });
    return isExitingUser;
  }

  public void logout() {
    FirebaseUser firebaseUser;
    if ((firebaseUser = FirebaseAuth.getInstance().getCurrentUser()) != null) {
      rhRepository.unauthoriseAllCourses();
      rhRepository.getAuthenticatedUser(firebaseUser.getUid())
        .observe((LifecycleOwner) context, user -> {
          rhRepository.deleteUser(user);
        });
      firebaseAuth.signOut();
    }
  }

  // PHONE NUMBER & PASSWORD

  // Phone Authentication handler - This method updates MutableLiveData when successful
//  public MutableLiveData<Boolean> authenticatePhone(PhoneAuthCredential phoneAuthCredential) {
//    MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
//    firebaseAuth.signInWithCredential(phoneAuthCredential)
//      .addOnCompleteListener(task -> {
//        if (task.isSuccessful()) {
//          isSuccessful.setValue(true);
//        } else {
//          Toast.makeText(context,
//            task.getException().getMessage(),
//            Toast.LENGTH_LONG).show();
//          isSuccessful.setValue(false);
//        }
//      });
//    return isSuccessful;
//  }

//  // Phone Authentication Entry Point - This method sends an OTP request to the user
//  public MutableLiveData<PhoneAuthCredential> authenticatePhone(String country, String number) {
//    MutableLiveData<PhoneAuthCredential> isSuccessful = new MutableLiveData<>();
//    if (country != null && number != null && !country.isEmpty() && !number.isEmpty()) {
//      String complete = country + number;
//      PhoneAuthProvider.getInstance().verifyPhoneNumber(
//        complete,
//        30,
//        TimeUnit.SECONDS,
//        (Activity) this.context,
//        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//          @Override
//          public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//            isSuccessful.setValue(phoneAuthCredential);
//          }
//
//          @Override
//          public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(context,
//              e.getMessage(),
//              Toast.LENGTH_LONG).show();
//            isSuccessful.setValue(null);
//          }
//
//          @Override
//          public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            Toast.makeText(context,
//              "BAM",
//              Toast.LENGTH_LONG).show();
//          }
//        }
//      );
//    } else {
//      Toast.makeText(this.context,
//        "Please enter a valid cell phone number.",
//        Toast.LENGTH_LONG).show();
//      isSuccessful.setValue(null);
//    }
//    return isSuccessful;
//  }

//  public LiveData<Boolean> isExistingUserPhone(String phone) {
//    MutableLiveData<Boolean> isExitingUser = new MutableLiveData<>();
//    db.collection("users")
//      .whereEqualTo("cell", phone)
//      .get()
//      .addOnCompleteListener(task -> {
//        if (task.getResult().isEmpty()) {
//          isExitingUser.setValue(false);
//        } else {
//          isExitingUser.setValue(true);
//        }
//      });
//    return isExitingUser;
//  }
}
