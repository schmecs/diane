package com.rebeccablum.diane.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rebeccablum.diane.TestCoroutineRule
import com.rebeccablum.diane.media.PlaybackManager
import com.rebeccablum.diane.models.Post
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PostItemViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var sut: PostItemViewModel
    private lateinit var playbackManager: PlaybackManager

    companion object {
        private const val POST_CONTENT = "content"
        private const val POST_FILENAME = "filename"
        private const val POST_DURATION = 3000
        private const val POST_DURATION_FORMATTED = "00:03"
    }

    @Before
    fun setup() {
        playbackManager = mockk()
        coEvery { playbackManager.getTrackDuration(POST_FILENAME) } returns(POST_DURATION)
    }

    @Test
    fun onInit_isNotPlaying() {
        sut = PostItemViewModel(Post(POST_CONTENT, POST_FILENAME), playbackManager)
        assert(!sut.isPlaying.get())
    }

    @Test
    fun onInit_setsTrackDuration() {
        sut = PostItemViewModel(Post(POST_CONTENT, POST_FILENAME), playbackManager)
        assert(sut.trackDuration.get() == POST_DURATION_FORMATTED)
    }

    @Test
    fun onPlaybackToggled_givenNotPlaying_startsPlayback() {
        sut = PostItemViewModel(Post(POST_CONTENT, POST_FILENAME), playbackManager)
        sut.togglePlayback()

        // TODO this doesn't return anything. how do i stub?
        coEvery { playbackManager.startPlayback(POST_FILENAME, any(), any()) }

        assert(sut.isPlaying.get())

        coVerify { playbackManager.startPlayback(POST_FILENAME, any(), any()) }
    }

    @Test
    fun onPlaybackToggled_givenPlaying_stopsPlayback() {

    }
}