package database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import models.AppVault
import models.DbVault

@Dao
interface VaultDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addData(dbVault: DbVault)

    @Query("SELECT * FROM vault ")
    fun getData(): Flow<List<DbVault>>

    @Query("UPDATE vault SET status=:status WHERE pkgName=:pkgInfo")
    suspend fun updateAppStatus(pkgInfo: String,status:Boolean)



    @Query("DELETE FROM vault WHERE pkgName=:pkgInfo")
    suspend fun specificItemRemove(pkgInfo: String)

    @Query("DELETE FROM vault")
    suspend fun deleteAllData()
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAppData(appVault: AppVault)

    @Query("SELECT * FROM all_apps ")
    fun getAllApps(): Flow<List<AppVault>>



}