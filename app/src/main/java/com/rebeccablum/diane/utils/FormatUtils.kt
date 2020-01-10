package com.rebeccablum.diane.utils

object FormatUtils {

    fun convertSecondsToTimerString(totalSeconds: Int): String {
        val seconds = totalSeconds % 60
        val minutes: Int = totalSeconds / 60
        val secondsString = if (seconds < 10) {
            "0$seconds"
        } else seconds.toString()
        val minutesString = if (minutes < 10) {
            "0$minutes"
        } else minutes.toString()

        return String.format("%s:%s", minutesString, secondsString)
    }
}