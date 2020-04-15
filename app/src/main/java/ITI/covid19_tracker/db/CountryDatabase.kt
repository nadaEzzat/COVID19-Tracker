package ITI.covid19_tracker.db


import ITI.covid19_tracker.db.statisticdb.statisticDao
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.model.statisticModel
import ITI.covid19_tracker.model.subscribeCountry
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities =  [Country::class , statisticModel::class], version = 1)
 abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun statisticDao() : statisticDao



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
    }
}