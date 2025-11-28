package com.example.sinaliza

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    // simple state flow with text
    private val _text = MutableStateFlow("Welcome to Home")
    val text: StateFlow<String> = _text.asStateFlow()
}
