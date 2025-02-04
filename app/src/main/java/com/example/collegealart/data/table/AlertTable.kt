package com.example.collegealart.data.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlertTable(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    var alertTitle:String,
    var aboutAlert:String? = null,
    var location:String? = null,
    var date:String,
    var time:String ,
    var imagePath:String? = null,
    var soundPath:String? = null ,
    val timestamp: Long = System.currentTimeMillis()
)