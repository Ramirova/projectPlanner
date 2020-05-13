package com.example.projectplanner.data.db.models

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import java.util.Date

@Parcelize
@TypeParceler<Color, ColorParceler>
@Entity(tableName = "project")
@Parcelize
@TypeParceler<Color, ColorParceler>
class Project(

    @PrimaryKey(autoGenerate = true)
    val projectId: Long,

    @ColumnInfo(name = "project_title")
    val projectTitle: String,

    @ColumnInfo(name = "project_desc")
    val projectDescription: String?,

    @ColumnInfo(name = "project_start_date")
    val projectStartDate: Date,

    @ColumnInfo(name = "project_end_date")
    val projectEndDate: Date,

    @ColumnInfo(name = "project_color")
    val projectColor: Color

) : Parcelable

object ColorParceler : Parceler<Color> {
    override fun create(parcel: Parcel): Color = Color.valueOf(parcel.readLong())
    override fun Color.write(parcel: Parcel, flags: Int) = parcel.writeLong(pack())
}