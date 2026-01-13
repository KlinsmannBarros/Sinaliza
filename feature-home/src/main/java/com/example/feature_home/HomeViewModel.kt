package com.example.sinaliza.feature.home

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.ReportEntity
import com.example.core.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

data class HomeReport(
    val id: String,
    val title: String,
    val description: String,
    val category: String = "Pothole",
    val date: String = "2025-10-30"
)

data class HomeUiState(
    val query: String = "",
    val selectedCategory: String? = null,
    val reports: List<HomeReport> = emptyList()
)

class HomeViewModel(
    private val repo: ReportRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_QUERY = "home_query"
        private const val KEY_CATEGORY = "home_category"
    }

    private val _query = MutableStateFlow(savedStateHandle.get<String>(KEY_QUERY) ?: "")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedCategory = MutableStateFlow(savedStateHandle.get<String?>(KEY_CATEGORY))
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Defer obtaining reports Flow until first use to avoid triggering Room initialization in the constructor
    private val reportsFlow by lazy { repo.getReports() }

    // Combine query and category with reports to produce filteredReports
    val uiState = combine(reportsFlow, _query, _selectedCategory) { reports, q, cat ->
        val filtered = reports.filter { r ->
            val matchesQuery = q.isBlank() || r.title.contains(q, ignoreCase = true) || r.description.contains(q, ignoreCase = true)
            // ReportEntity doesn't have a category field; keep category filtering as a future enhancement
            val matchesCategory = true
            matchesQuery && matchesCategory
        }
        HomeUiState(query = q, selectedCategory = cat, reports = filtered.map { entity ->
            HomeReport(id = entity.id, title = entity.title, description = entity.description)
        })
    }.stateIn(viewModelScope, SharingStarted.Lazily, HomeUiState())

    fun setQuery(q: String) {
        _query.value = q
        savedStateHandle.set(KEY_QUERY, q)
    }

    fun setSelectedCategory(cat: String?) {
        _selectedCategory.value = cat
        savedStateHandle.set(KEY_CATEGORY, cat)
    }

    // optional helper to add report from some UI (not used currently)
    fun addSampleReport(report: HomeReport) {
        viewModelScope.launch {
            try {
                repo.addReport(com.example.core.Report(id = report.id, title = report.title, description = report.description, latitude = 0.0, longitude = 0.0))
            } catch (_: Exception) {
            }
        }
    }
}

// Factory for compatibility when only Application is available
class HomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val repo = ReportRepository(application)
            val savedStateHandle = SavedStateHandle()
            return HomeViewModel(repo, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
