package ITI.covid19_tracker.db

import ITI.covid19_tracker.model.Country
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities =  [Country::class], version = 1)
 abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao

    companion object {
        @Volatile
        private var INSTANCE: CountryDatabase? = null
        fun getDatabase(context: Context): CountryDatabase? {
            if (INSTANCE == null) {
                synchronized(CountryDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            CountryDatabase::class.java, "country_database2"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
        // private val LOCK = Any()
/*
        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            CountryDatabase::class.java, "country_database.db")
            .build()
    }

 */
        /*
    companion object{

    private val DB_NAME = "country_database"
    private var INSTANCE: CountryDatabase? = null

        fun getInstance(context: Context?):CountryDatabase? {
            if (CountryDatabase.INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context!!, CountryDatabase::class.java , CountryDatabase.DB_NAME)
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }

*/
    }
}