package com.example.todoapp.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.util.Log;

import com.example.todoapp.Task;

import java.util.List;

@Dao
public interface TaskDao {



    @Query("SELECT * FROM task ORDER BY createdTime desc")
    LiveData<List<Task>> getAll();

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM task ORDER BY createdTime asc")
    LiveData<List<Task>> getAllByAscending();

    @Query("SELECT * FROM task WHERE imageImportance = 1")
    LiveData<List<Task>> getAllUrgent();

    @Query("SELECT * FROM task WHERE imageImportance = 2")
    LiveData<List<Task>> getAllImportant();

    @Query("SELECT * FROM task WHERE imageImportance = 3")
    LiveData<List<Task>> getAllCommon();

    @Query("SELECT * FROM task WHERE createdTime + :timeZone     < :current - (:current % :dayInMilliseconds)")
    LiveData<List<Task>> getAllOldTAsks(long current, long dayInMilliseconds, long timeZone);

}
