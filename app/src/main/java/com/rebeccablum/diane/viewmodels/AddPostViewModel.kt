package com.rebeccablum.diane.viewmodels

import android.app.Application
import android.media.MediaRecorder
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rebeccablum.diane.media.PlaybackManager
import com.rebeccablum.diane.utils.FormatUtils.convertSecondsToTimerString
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.concurrent.fixedRateTimer

// TODO extract MediaRecorder as well?
class AddPostViewModel(application: Application, private val playbackManager: PlaybackManager) :
    AndroidViewModel(application) {

    companion object {
        private val TAG = AddPostViewModel::class.java.simpleName

        private const val ERROR_EMPTY_POST_TEXT = "Post text is empty."
        private const val STARTING_TIMER_VALUE = "00:00"
    }

    val currentText = ObservableField("")
    val timerString = ObservableField(STARTING_TIMER_VALUE)
    val isRecording = ObservableBoolean(false)
    val isPlaying = ObservableBoolean(false)

    private val directory = application.applicationContext.filesDir.absolutePath

    val startRecording = "Start Recording"
    val stopRecording = "Stop Recording"
    val startPlayback = "Start Playback"
    val stopPlayback = "Stop Playback"

    private var recorder: MediaRecorder? = null
    private var timer: Timer? = null
    private var fileName = setFileName()

    private lateinit var onPostSaved: (postText: String, fileName: String) -> Unit
    private lateinit var onSaveError: (error: Throwable) -> Unit

    fun onTextChanged(s: CharSequence) {
        currentText.set(s.toString())
    }

    fun setCallbacks(
        onPostSaved: (postText: String, fileName: String) -> Unit,
        onSaveError: (error: Throwable) -> Unit
    ) {
        this.onPostSaved = onPostSaved
        this.onSaveError = onSaveError
    }

    fun onSave() {
        timerString.set(STARTING_TIMER_VALUE)
        currentText.get()?.let {
            onPostSaved.invoke(it, fileName)
        } ?: run {
            onSaveError.invoke(IllegalStateException(ERROR_EMPTY_POST_TEXT))
        }
    }

    fun onCancel() {
        deleteFileIfExists()
        stopTimer()
        timerString.set(STARTING_TIMER_VALUE)
    }

    fun toggleRecording(setRecording: Boolean) {
        this.isRecording.set(setRecording)

        if (setRecording) {
            startRecording()
        } else {
            stopRecording()
        }
    }

    fun togglePlayback(setPlaying: Boolean) {
        this.isPlaying.set(setPlaying)

        if (setPlaying) {
            startPlayback()
        } else {
            stopPlayback()
        }
    }

    fun onDismiss() {
        stopTimer()
        fileName = setFileName()
        stopRecording()
        stopPlayback()
    }

    // TODO catch additional exceptions for these methods

    private fun setFileName(): String {
        return directory + UUID.randomUUID().toString()
    }

    private fun startPlayback() {
        timerString.set(STARTING_TIMER_VALUE)

        viewModelScope.launch {
            playbackManager.startPlayback(fileName, ::onStopped) { positionMs ->
                setPositionStringFromSeconds(positionMs / 1000)
            }
        }
    }

    private fun onStopped() {
        isPlaying.set(false)
        timerString.set(STARTING_TIMER_VALUE)
    }

    private fun stopPlayback() {
        playbackManager.stopPlayback()
    }

    private fun startRecording() {
        timerString.set(STARTING_TIMER_VALUE)
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")
            }

            start()
            startTimer()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            reset()
            release()
            stopTimer()
        }
        recorder = null
    }

    private fun startTimer() {
        var int = 0
        timer = fixedRateTimer(period = 1000, initialDelay = 1000) {
            int++
            updateDurationString(int)
        }
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    private fun updateDurationString(totalSeconds: Int) {
        timerString.set(convertSecondsToTimerString(totalSeconds))
    }

    private fun setPositionStringFromSeconds(positionSeconds: Int) {
        if (positionSeconds > 0) {
            timerString.set(convertSecondsToTimerString(positionSeconds))
        }
    }

    private fun deleteFileIfExists() {
        val file = File(fileName)
        if (file.exists()) {
            file.delete()
            Log.d(TAG, "Deleted $fileName")
        }
    }
}