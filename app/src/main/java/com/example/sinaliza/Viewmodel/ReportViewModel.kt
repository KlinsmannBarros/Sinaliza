package com.example.sinaliza.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReportViewModel : ViewModel() {

    private val _message = MutableStateFlow("Fill out the report")
    val message: StateFlow<String> = _message
}
