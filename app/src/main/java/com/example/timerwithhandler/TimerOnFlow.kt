package com.example.timerwithhandler

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

object TimerOnFlow {
    private var isStarting = true
    private var differenceTime = 0L
    private var baseTime = 0L

    fun stop() {
        isStarting = false
    }

    fun start() {
        isStarting = true
    }

    fun setBase(base: Long) {
        baseTime = base
    }

    @ExperimentalCoroutinesApi
    fun timeFlow(startTime: Long) = callbackFlow<Long> {
        val timer = Timer()
        var time = startTime

        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    try {
                        if (isStarting) offer(time - differenceTime)
                    } catch (e: Exception) {
                    }

                    time++
                    if (!isStarting) differenceTime++
                }
            },
            0,
            1000L
        )

        awaitClose { timer.cancel() }
    }
}