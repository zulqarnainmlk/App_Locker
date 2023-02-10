package models

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_list_usage")
data class ListMonthlyModel (
    @PrimaryKey
    @ColumnInfo(name = "appName")
    val appName: String,
    @ColumnInfo(name = "packageName")
    val packageName: String,
    @ColumnInfo(name = "hour")
    val hour: String,
    @ColumnInfo(name = "mint")
    val mint: String,
    @ColumnInfo(name = "sec")
    val sec: String,
    @ColumnInfo(name = "timeSpent")
    val timeSpent:Int,
)