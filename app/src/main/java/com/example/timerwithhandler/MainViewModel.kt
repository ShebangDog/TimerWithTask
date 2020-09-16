package com.example.timerwithhandler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    @ExperimentalCoroutinesApi
    val time = TimerOnFlow
        .timeFlow(1000L)
        .asLiveData()

    fun stopTime() = viewModelScope.launch {
        TimerOnFlow.stop()
    }

    fun startTime() = viewModelScope.launch {
        TimerOnFlow.start()
    }

    fun setBaseTime(startTime: Long) {
        TimerOnFlow.setBase(startTime)
    }
}