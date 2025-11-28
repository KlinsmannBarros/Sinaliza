package com.example.sinaliza.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _text = MutableStateFlow("Welcome to Home")
    val text: StateFlow<String> = _text
}
