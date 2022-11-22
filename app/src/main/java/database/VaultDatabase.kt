package database

import android.content.Context
import androidx.room.*
import database.dao.VaultDao
import models.AppVault
import models.DbVault


@Database(
    entities = [DbVault::class,AppVault::class],

//    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//    ],
    version = 2,
    exportSchema = true
)
//@TypeConverters(NoteConverter::class)
abstract class VaultDatabase : RoomDatabase() {

    abstract fun vaultDao(): VaultDao

    companion object {
        @Volatile
        private var INSTANCE: VaultDatabase? = null

        fun getDatabase(context: Context): VaultDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }


        private fun buildDatabase(context: Context): VaultDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                VaultDatabase::class.java,
                "vault_database"
            )

                .build()
        }
    }
}