package com.rebeccablum.diane.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rebeccablum.diane.utils.Injection
import com.rebeccablum.diane.data.PostRepository

class ViewModelFactory private constructor(
    private val application: Application,
    private val postRepository: PostRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            return INSTANCE ?: synchronized(
                ViewModelFactory::class.java) {
                INSTANCE
                    ?: ViewModelFactory(
                        application,
                        Injection.providePostRepository(application)
                    )
                    .also { INSTANCE = it }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(postRepository)
                isAssignableFrom(AddPostViewModel::class.java) ->
                    AddPostViewModel(application)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
