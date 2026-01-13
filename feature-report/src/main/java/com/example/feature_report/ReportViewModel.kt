package com.example.sinaliza.feature.report

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.Report
import com.example.core.ReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewModel(
    private val repo: ReportRepository
) : ViewModel() {

    private val _saveState = MutableStateFlow<SaveStatus>(SaveStatus.Idle)
    val saveState: StateFlow<SaveStatus> = _saveState.asStateFlow()

    private val _events = MutableSharedFlow<ReportEvent>()
    val events = _events.asSharedFlow()

    fun saveReport(report: Report) {
        viewModelScope.launch(Dispatchers.IO) {
            _saveState.value = SaveStatus.Saving
            runCatching { repo.addReport(report) }
                .onSuccess {
                    _saveState.value = SaveStatus.Success
                    try {
                        _events.emit(ReportEvent.Saved(report))
                    } catch (_: Exception) {
                    }
                }
                .onFailure { t ->
                    Log.w("ReportViewModel", "saveReport failed", t)
                    _saveState.value = SaveStatus.Error(t)
                    try {
                        _events.emit(ReportEvent.Error(t.message ?: "Unknown error"))
                    } catch (_: Exception) {
                    }
                }
        }
    }

    sealed class SaveStatus {
        object Idle : SaveStatus()
        object Saving : SaveStatus()
        object Success : SaveStatus()
        data class Error(val throwable: Throwable) : SaveStatus()
    }

    sealed class ReportEvent {
        data class Saved(val report: Report) : ReportEvent()
        data class Error(val message: String) : ReportEvent()
    }
}

// Simple factory for places that only have an Application available (keeps backwards compatibility)
class ReportViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val repo = ReportRepository(application)
            return ReportViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
