package models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_apps")
data class AppVault (
    @PrimaryKey(autoGenerate = true)
    val AppId: Int?=0,
    @ColumnInfo(name = "appTitle")
    val name: String?="",
    @ColumnInfo(name = "pkgName")
    val pkgInfo: String?="",

)