package database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import models.AppVault
import models.DbVault
import models.ListTodayModel
import models.ListWeeklyModel

@Dao
interface WeeklyUsageListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addData(listweekly: ListWeeklyModel)

    @Query("SELECT * FROM weekly_list_usage ")
    fun getData(): Flow<List<ListWeeklyModel>>


    @Query("SELECT * FROM weekly_list_usage ")
    fun getWeeklyUsageList(): Flow<List<ListWeeklyModel>>



}