package com.example.todoapp.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.todoapp.Task;

@Database(entities = {Task.class}, version = 2, exportSchema =  false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract TaskDao taskDao();

}
