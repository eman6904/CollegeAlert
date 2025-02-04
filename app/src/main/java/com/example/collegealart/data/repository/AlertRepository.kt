package com.example.collegealart.data.repository

import androidx.lifecycle.LiveData
import com.example.collegealart.data.dao.AlertDAO
import com.example.collegealart.data.table.AlertTable

class AlertRepository(private val alertDAO: AlertDAO) {

    val alerts: LiveData<List<AlertTable>> = alertDAO.getAlerts()

    suspend fun insertAlert(alertTable: AlertTable) {
        alertDAO.insertAlert(alertTable)
    }
    suspend fun deleteAlerts(alertsIds: List<Int>) {
        alertDAO.deleteAlert(alertsIds)
    }
    suspend fun updateAlert(alertTable: AlertTable) {
        alertDAO.updateAlert(alertTable)
    }
}