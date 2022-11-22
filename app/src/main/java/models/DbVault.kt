package models

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "vault", indices = [Index(value = ["pkgName"], unique = true)])
data class DbVault (
    @PrimaryKey(autoGenerate = true)
    val AppId: Int?=0,
    @ColumnInfo(name="status")
    var status:Boolean?=false,
    @ColumnInfo(name = "appTitle")
    val name: String?="",
    @ColumnInfo(name = "pkgName")
    val pkgInfo: String?="",
    @ColumnInfo(name = "icon")
    val icon: String?="",
)