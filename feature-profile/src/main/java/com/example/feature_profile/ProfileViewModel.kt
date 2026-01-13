package com.example.sinaliza.feature.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val name: String = "Random Example",
    val email: String = "random@example.com",
    val reportsCount: Int = 12,
    val upvotes: Int = 34,
    val joinedYear: Int = 2024
)

@Suppress("unused")
class ProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    fun setName(v: String) { _state.value = _state.value.copy(name = v) }
    fun setEmail(v: String) { _state.value = _state.value.copy(email = v) }
}
