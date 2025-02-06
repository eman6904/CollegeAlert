package com.example.collegealart.data.viewModel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.collegealart.data.database.AlertDatabase
import com.example.collegealart.data.repository.AlertRepository
import com.example.collegealart.data.sharedPreference.PreferencesManager
import com.example.collegealart.data.table.AlertTable
import com.example.collegealart.ui.screens.isEventPassed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel(application: Application) : AndroidViewModel(application)  {

    private val repository: AlertRepository
    val alerts: LiveData<List<AlertTable>> get() = repository.alerts

    private val _selectedIds = MutableLiveData<ArrayList<Int>>()
    val selectedIds: LiveData<ArrayList<Int>> get() = _selectedIds

    private val _selectedAlertToEdit = MutableLiveData<AlertTable>()
    val selectedAlertToEdit: LiveData<AlertTable> get() = _selectedAlertToEdit

    private val _selectedAlertToDisplay = MutableLiveData<AlertTable>()
    val selectedAlertToDisplay: LiveData<AlertTable> get() = _selectedAlertToDisplay

    private val _sharedPrefManager = MutableLiveData<PreferencesManager>()
    val sharedPrefManager: LiveData<PreferencesManager> get() = _sharedPrefManager

    init {
        val dao = AlertDatabase.getDatabase(application).alertDao()
        repository = AlertRepository(dao)
        _selectedIds.value = ArrayList()
        _sharedPrefManager.value = PreferencesManager(application)
    }
    fun insertAlert(alert: AlertTable) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAlert(alert)
    }
    fun deleteAlert(alertsId: List<Int>) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAlerts(alertsId)
    }

    fun updateAlert(alert: AlertTable) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateAlert(alert)
    }
    fun addToSelectedIds(id:Int){

        if(!_selectedIds.value!!.contains(id)) {
            val list = _selectedIds.value
            list!!.add(id)
            _selectedIds.value = list!!
        }

    }
    fun setSelectedAlertToEdit(alert:AlertTable){

        _selectedAlertToEdit.value = alert
    }
    fun setSelectedAlertToDisplay(alert:AlertTable){

        _selectedAlertToDisplay.value = alert
    }
    fun clearSelectedIds(){
        _selectedIds.value = ArrayList()
    }

}