package com.rebeccablum.diane.viewmodels

import android.app.Application
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import java.io.IOException
import java.util.*

class AddPostViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = AddPostViewModel::class.java.simpleName
    }

    val currentText = ObservableField("")
    val isRecording = ObservableBoolean(false)
    val isPlaying = ObservableBoolean(false)

    private val directory = application.applicationContext.filesDir.absolutePath
    val fileName = directory + UUID.randomUUID().toString()

    val startRecording = "Start Recording"
    val stopRecording = "Stop Recording"
    val startPlayback = "Start Playback"
    val stopPlayback = "Stop Playback"

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    fun onTextChanged(s: CharSequence) {
        currentText.set(s.toString())
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

    fun onStop() {
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }

    // TODO catch additional exceptions for these methods

    private fun startPlayback() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlayback() {
        player?.release()
        player = null
    }

    private fun startRecording() {
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
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }
}