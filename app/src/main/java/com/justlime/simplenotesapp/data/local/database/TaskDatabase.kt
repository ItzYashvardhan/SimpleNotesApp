package com.justlime.simplenotesapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.justlime.simplenotesapp.data.local.dao.TaskDao
import com.justlime.simplenotesapp.data.local.entity.TaskEntity


@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}