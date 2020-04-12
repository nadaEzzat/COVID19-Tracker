package ITI.covid19_tracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscrib_table")
class subscribeCountry(
    @PrimaryKey(autoGenerate = true) var id: Int,
        @ColumnInfo(name = "country_name") var country_name: String,
        @ColumnInfo(name = "cases") var cases: String,
        @ColumnInfo(name = "new_cases") var new_cases: String,
        @ColumnInfo(name = "total_recovered") var total_recovered: String,
        @ColumnInfo(name = "deaths") var deaths: String,
        @ColumnInfo(name = "new_deaths") var new_deaths: String,
        @ColumnInfo(name = "total_cases_per_1m_population") var total_cases_per_1m_population: String

)