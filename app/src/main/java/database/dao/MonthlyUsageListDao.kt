package database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import models.*

@Dao
interface MonthlyUsageListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addData(listmonthly: ListMonthlyModel)

    @Query("SELECT * FROM monthly_list_usage ")
    fun getData(): Flow<List<ListMonthlyModel>>


    @Query("SELECT * FROM monthly_list_usage ")
    fun getMonthlyUsageList(): Flow<List<ListMonthlyModel>>



}