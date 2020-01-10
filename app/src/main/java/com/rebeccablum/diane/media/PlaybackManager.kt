package com.rebeccablum.diane.media

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import kotlin.concurrent.fixedRateTimer

class PlaybackManager private constructor(val application: Application) {

    companion object {
        @Volatile
        private var INSTANCE: PlaybackManager? = null

        fun getInstance(application: Application): PlaybackManager {
            return INSTANCE ?: synchronized(PlaybackManager::class.java) {
                INSTANCE ?: PlaybackManager(application)
                    .also { INSTANCE = it }
            }
        }
    }

    // TODO what's the right way to group a given mediaplayer & its UI completion callback
    private var player: MediaPlayer? = null
    private var timer: Timer? = null
    private var onCompletion: () -> Unit = {}

    suspend fun startPlayback(
        fileName: String,
        onCompletion: () -> Unit = {},
        onPositionUpdated: (ms: Int) -> Unit = {}
    ) {
        if (player != null) {
            clearPlayer()
        }

        this.onCompletion = onCompletion

        player = getMediaPlayerForFile(fileName).apply {
            setOnCompletionListener {
                clearPlayer()
                onCompletion()
            }
            start()
            startTimer(onPositionUpdated)
        }
    }

    private fun startTimer(onPositionUpdated: (ms: Int) -> Unit) {
        timer = fixedRateTimer(period = 1000) {
            val position = player?.currentPosition ?: -1
            onPositionUpdated(position)
        }
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    // TODO handle pause
    fun stopPlayback() {
        if (player?.isPlaying == true) {
            player?.stop()
        }
        clearPlayer()
    }

    suspend fun getTrackDuration(fileName: String): Int {
        getMediaPlayerForFile(fileName).apply {
            val duration = duration
            release()
            return duration
        }
    }

    // TODO is this the right way?
    private suspend fun getMediaPlayerForFile(fileName: String): MediaPlayer {
        return coroutineScope {
            withContext(Dispatchers.IO) {
                val uri = Uri.fromFile(File(fileName))
                MediaPlayer.create(
                    application.applicationContext,
                    uri
                )
            }
        }
    }

    private fun clearPlayer() {
        player?.reset()
        player?.release()
        player = null
        stopTimer()
    }
}