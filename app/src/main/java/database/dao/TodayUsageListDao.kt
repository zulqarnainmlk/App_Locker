package database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import models.ListTodayModel

@Dao
interface TodayUsageListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addData(listToday: ListTodayModel)

    @Query("SELECT * FROM today_list_usage")
    fun getData(): Flow<List<ListTodayModel>>

//    @Query("UPDATE vault SET status=:status WHERE pkgName=:pkgInfo")
//    suspend fun updateAppStatus(pkgInfo: String,status:Boolean)
//
//
//
//    @Query("DELETE FROM vault WHERE pkgName=:pkgInfo")
//    suspend fun specificItemRemove(pkgInfo: String)
//
//    @Query("DELETE FROM vault")
//    suspend fun deleteAllData()
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun addAppData(appVault: AppVault)

    @Query("SELECT * FROM today_list_usage")
    fun getTodayUsageList(): Flow<List<ListTodayModel>>



}