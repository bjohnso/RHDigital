package com.rhdigital.rhclient.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.rhdigital.rhclient.database.DAO.CourseDAO;
import com.rhdigital.rhclient.database.DAO.ReportDAO;
import com.rhdigital.rhclient.database.DAO.VideoDAO;
import com.rhdigital.rhclient.database.DAO.embedded.CourseWithWorkbooksDAO;
import com.rhdigital.rhclient.database.DAO.PackageDAO;
import com.rhdigital.rhclient.database.DAO.UserDAO;
import com.rhdigital.rhclient.database.DAO.WorkbookDAO;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.Package;
import com.rhdigital.rhclient.database.model.Report;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.model.Video;
import com.rhdigital.rhclient.database.model.Workbook;

@Database(entities = {Course.class, Package.class, Report.class, User.class, Video.class, Workbook.class}, version = 2, exportSchema = false)
public abstract class RHDatabase extends RoomDatabase {

    private static volatile RHDatabase INSTANCE;
    public abstract CourseDAO courseDAO();
    public abstract PackageDAO packageDAO();
    public abstract ReportDAO reportDAO();
    public abstract UserDAO userDAO();
    public abstract VideoDAO videoDAO();
    public abstract WorkbookDAO workbookDAO();
    public abstract CourseWithWorkbooksDAO courseWithWorkbooksDAO();

    public static RHDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RHDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RHDatabase.class, "RHDatabase")
                            .addCallback(roomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
          super.onOpen(db);
        }
    };
}
