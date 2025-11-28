package com.example.sinaliza.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel : ViewModel() {

    private val _status = MutableStateFlow("Map loading...")
    val status: StateFlow<String> = _status
}
