package com.example.collegealart.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.collegealart.data.table.AlertTable

@Dao
public interface AlertDAO {

    @Insert
    suspend fun insertAlert(alertTable: AlertTable)
    @Query("select * from AlertTable ORDER BY timestamp DESC")
    fun getAlerts(): LiveData<List<AlertTable>>
    @Query("delete from AlertTable where id in (:alertsIds)")
    suspend fun deleteAlert(alertsIds: List<Int>)
    @Update
    suspend fun updateAlert(alert: AlertTable)
}