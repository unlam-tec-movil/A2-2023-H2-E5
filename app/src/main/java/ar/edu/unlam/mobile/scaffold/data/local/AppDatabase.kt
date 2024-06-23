package ar.edu.unlam.mobile.scaffold.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ar.edu.unlam.mobile.scaffold.data.local.entity.StepHistoryEntity

@Database(entities = [StepHistoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stepHistoryDao(): StepHistoryDao
}
