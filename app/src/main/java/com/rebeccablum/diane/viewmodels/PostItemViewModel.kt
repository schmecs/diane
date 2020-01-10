package com.rebeccablum.diane.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebeccablum.diane.media.PlaybackManager
import com.rebeccablum.diane.models.Post
import com.rebeccablum.diane.utils.FormatUtils.convertSecondsToTimerString
import kotlinx.coroutines.launch

class PostItemViewModel(val post: Post, private val playbackManager: PlaybackManager) :
    ViewModel() {

    companion object {
        private val TAG = PostItemViewModel::class.java.simpleName

        private const val DURATION_NOT_LOADED = "--:--"
        private const val CURRENT_POSITION_NOT_LOADED = "00:00"
        private const val MS_DIV = 1000
    }

    val isPlaying = ObservableBoolean(false)
    val currentPosition = ObservableField(CURRENT_POSITION_NOT_LOADED)
    val trackDuration = ObservableField(DURATION_NOT_LOADED)

    init {
        viewModelScope.launch {
            val durationMs = playbackManager.getTrackDuration(post.fileName)
            setDurationStringFromSeconds(durationMs / MS_DIV)
        }
    }

    // TODO extract playback functionality to be shared
    fun togglePlayback() {
        val setPlaying = !isPlaying.get()
        this.isPlaying.set(setPlaying)

        if (setPlaying) {
            startPlayback()
        } else {
            stopPlayback()
        }
    }

    private fun startPlayback() {
        viewModelScope.launch {
            playbackManager.startPlayback(post.fileName, ::onStopped) { currentPositionMs ->
                setPositionStringFromSeconds(currentPositionMs / 1000)
            }
        }
    }

    private fun stopPlayback() {
        playbackManager.stopPlayback()
        onStopped()
    }

    private fun onStopped() {
        isPlaying.set(false)
        currentPosition.set(CURRENT_POSITION_NOT_LOADED)
    }

    private fun setDurationStringFromSeconds(durationSeconds: Int) {
        if (durationSeconds > 0) {
            trackDuration.set(convertSecondsToTimerString(durationSeconds))
        }
    }

    private fun setPositionStringFromSeconds(positionSeconds: Int) {
        if (positionSeconds > 0) {
            currentPosition.set(convertSecondsToTimerString(positionSeconds))
        }
    }
}
