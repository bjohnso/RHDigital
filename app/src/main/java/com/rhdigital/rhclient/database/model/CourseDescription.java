package com.rhdigital.rhclient.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "course_descriptions",
        foreignKeys = @ForeignKey(onDelete = CASCADE,
                entity = Course.class,
                parentColumns = "id",
                childColumns = "course_id"
        ), indices = {@Index("course_id")})
public class CourseDescription extends Model {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    private String id;

    @NonNull
    @ColumnInfo(name = "course_id")
    private String courseId;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @Ignore
    public CourseDescription() { }

    public CourseDescription(
            @NonNull String id,
            @NonNull String courseId,
            @NonNull String description)
    {
        this.id = id;
        this.courseId = courseId;
        this.description = description;
    }

    @NonNull
    public String getCourseId() {
        return courseId;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setCourseId(@NonNull String courseId) {
        this.courseId = courseId;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
