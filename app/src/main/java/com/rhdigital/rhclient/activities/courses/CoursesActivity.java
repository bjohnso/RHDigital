package com.rhdigital.rhclient.activities.courses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rhdigital.rhclient.room.model.Course;
import com.rhdigital.rhclient.room.model.embedded.CourseWithWorkbooks;
import com.rhdigital.rhclient.room.viewmodel.CourseViewModel;
import com.rhdigital.rhclient.room.viewmodel.UserViewModel;
import com.rhdigital.rhclient.room.viewmodel.WorkbookViewModel;

import java.util.List;

public class CoursesActivity extends AppCompatActivity {

    private Context context;

    //Observables
    private LiveData<Bitmap> userProfileImageObservable;
    private LiveData<List<Course>> undiscoveredCoursesObservable;
    private LiveData<List<Course>> authorisedCoursesObservable;
    private LiveData<List<CourseWithWorkbooks>> courseWithWorkbooksObservable;

    //View Model
    private CourseViewModel courseViewModel;
    private UserViewModel userViewModel;
    private WorkbookViewModel workbookViewModel;

    //Components
    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigationView;
    private ImageView userProfileButton;
    private FrameLayout userProfileButtonContainer;

    //Static Components
    private CoordinatorLayout appBarContainer;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        this.context = this;

//        if (FirebaseAuth.getInstance().getCurrentUser() == null ||
//          !FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
//          startAuthActivity();
//        }

        //FirebaseMessaging.getInstance().subscribeToTopic("workbook_downloads");

//        PushNotificationHelperService.getINSTANCE().setContext(this);
//        PushNotificationHelperService.getINSTANCE().generateNotificationChannel();
//        PushNotificationHelperService.getINSTANCE().saveTokenRemote();

//        courseViewModel = new CourseViewModel(getApplication());
//        userViewModel = new UserViewModel(getApplication());
//        workbookViewModel = new WorkbookViewModel(getApplication());
//
//        authorisedCoursesObservable = courseViewModel.getAllAuthorisedCourses();
//        undiscoveredCoursesObservable = courseViewModel.getAllUndiscoveredCourses();
//        courseWithWorkbooksObservable = workbookViewModel.getAllAuthorisedCoursesWithWorkbooks();
//
//        mToolbar = findViewById(R.id.topNavigationView);
//        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
//        mToolbar.setTitle("Courses");
//        appBarContainer = findViewById(R.id.app_bar);
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);

//        //Setup Toolbar
//        setSupportActionBar(mToolbar);
//
//      //Initialise Navigator
//      NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//        .findFragmentById(R.id.nav_host_courses);
//      NavController navController = navHostFragment.getNavController();
//      // Call Navigation Service
//      NavigationService.getINSTANCE().initNav(
//        getLocalClassName(),
//        navController,
//        R.navigation.courses_nav_graph,
//        R.id.coursesTabFragment);
//
//      //Set Listeners
//      mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavOnClick(getLocalClassName()));
//      //mToolbar.setOnMenuItemClickListener(new MenuItemOnClick(this));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(courses_menu_top, menu);
//        MenuItem menuItem = menu.findItem(R.id.courses_top_nav_profile);
//        menuItem.setActionView(R.layout.menu_profile_button_layout);
//    return true;
//    }

//  @Override
//  public boolean onPrepareOptionsMenu(Menu menu) {
//      MenuItem menuItem = menu.findItem(R.id.courses_top_nav_profile);
//
//      userProfileButton = (ImageView) menuItem
//        .getActionView()
//        .findViewById(R.id.menu_profile_image_button);
//
//    userProfileButtonContainer = menuItem.getActionView()
//      .findViewById(R.id.menu_profile_image_button_container);
//
//    // Observers
//    final Observer<Bitmap> userProfileImageObserver = bitmap -> {
//      userProfileImageObservable.removeObservers(this);
//      if (bitmap != null) {
//        userProfileButton.setImageBitmap(bitmap);
//      }
//    };

//    userProfileButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//      @Override
//      public void onGlobalLayout() {
//        if (userProfileButton.getHeight() > 0 && userProfileButton.getWidth() > 0) {
//          userProfileButton.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
//          userProfileImageObservable = userViewModel.getProfilePhoto(context,
//            FirebaseAuth.getInstance().getUid(),
//            userProfileButton.getWidth(),
//            userProfileButton.getHeight());
//          userProfileImageObservable.observe((LifecycleOwner) context, userProfileImageObserver);
//        }
//      }
//    });

//    userProfileButtonContainer.setOnClickListener(new UserProfileButtonOnClick(this));
//
//    return true;
//  }

  @SuppressLint("SourceLockedOrientationActivity")
    public void configureScreenOrientation(boolean isLandscape) {
      // Forced Orientation Landscape
      if (isLandscape)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
      else
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void revealVideoPlayerFullscreen(boolean isPlayer) {
      if (isPlayer) {
        appBarContainer.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
      } else {
        appBarContainer.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
      }
    }

//    private void startAuthActivity() {
//      Intent intent = new Intent(this, AuthActivity.class);
//      startActivity(intent);
//    }

  public LiveData<List<Course>> getUndiscoveredCoursesObservable() {
    return undiscoveredCoursesObservable;
  }

  public LiveData<List<Course>> getAuthorisedCoursesObservable() {
    return authorisedCoursesObservable;
  }

  public LiveData<List<CourseWithWorkbooks>> getCourseWithWorkbooksObservable() {
    return courseWithWorkbooksObservable;
  }

//  public void sendWorkbookDownloadNotification(Intent intent) {
//      PushNotificationHelperService.getINSTANCE().initialisePendingIntent(intent);
//      PushNotificationHelperService.getINSTANCE().displayNotification(
//        intent.getStringExtra("NAME"),
//        intent.getStringExtra("BODY"));
//  }

  public void setToolbarTitle(String title) {
      mToolbar.setTitle(title);
    }

  public CourseViewModel getCourseViewModel() {
    return courseViewModel;
  }

  public UserViewModel getUserViewModel() {
    return userViewModel;
  }

  public WorkbookViewModel getWorkbookViewModel() {
    return workbookViewModel;
  }

  public static class BottomNavOnClick implements BottomNavigationView.OnNavigationItemSelectedListener {

      private String className;

    public BottomNavOnClick(String className) {
      this.className = className;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//      switch (menuItem.getItemId()){
//        case R.id.courses_bottom_nav_courses:
//          NavigationService.getINSTANCE().navigate(className, R.id.coursesTabFragment, null, null);
//          return true;
//        case R.id.courses_bottom_nav_workbooks:
//          NavigationService.getINSTANCE().navigate(className, R.id.myWorkbooksFragment, null, null);
//          return true;
//        case R.id.courses_bottom_nav_research:
//          NavigationService.getINSTANCE().navigate(className, R.id.myResearchFragment, null, null);
//          return true;
//      }
      return false;
    }
  }

  public static class UserProfileButtonOnClick implements View.OnClickListener {

      Context context;

      public UserProfileButtonOnClick(Context context) {
        this.context = context;
      }

    @Override
    public void onClick(View view) {
//      Intent intent = new Intent(context, UserActivity.class);
//      context.startActivity(intent);
    }
  }

  public static class MenuItemOnClick implements Toolbar.OnMenuItemClickListener {

      Context context;

      public MenuItemOnClick(Context context) {
        this.context = context;
      }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
//      if (menuItem.getItemId() == R.id.courses_top_nav_profile) {
//        Intent intent = new Intent(context, UserActivity.class);
//        context.startActivity(intent);
//      }
      return false;
    }
  }
}
