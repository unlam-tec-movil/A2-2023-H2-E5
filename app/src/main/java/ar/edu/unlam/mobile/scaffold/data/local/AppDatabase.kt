import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ar.edu.unlam.mobile.scaffold.data.local.Converters
import ar.edu.unlam.mobile.scaffold.data.local.StepCounterDao
import ar.edu.unlam.mobile.scaffold.data.local.entity.StepCounterEntity

@Database(entities = [StepCounterEntity::class], version = 1)
@TypeConverters(Converters::class) // Agrega esta línea para usar los convertidores
abstract class AppDatabase : RoomDatabase() {

    abstract fun stepCounterDao(): StepCounterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
