package com.example.sinaliza.feature.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _text = MutableStateFlow("Welcome to Home (feature module)")
    val text: StateFlow<String> = _text.asStateFlow()
}
