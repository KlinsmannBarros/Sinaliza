package com.example.sinaliza.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {

    private val _username = MutableStateFlow("Anonymous")
    val username: StateFlow<String> = _username
}
