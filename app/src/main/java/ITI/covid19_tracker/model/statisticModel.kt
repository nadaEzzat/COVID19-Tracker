package ITI.covid19_tracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Statistic_table")
class statisticModel(
    // @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "new_cases") var new_cases: String,
    @ColumnInfo(name = "new_deaths") var new_deaths: String,
    @ColumnInfo(name = "total_recovered") var total_recovered : String,
    @ColumnInfo(name = "total_deaths") var total_deaths: String,
    @PrimaryKey() var  total_cases: String,
    @ColumnInfo(name = "statistic_taken_at") var statistic_taken_at: String
)