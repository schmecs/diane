package com.rebeccablum.diane.utils

import android.app.Application
import com.rebeccablum.diane.data.PostRepository

object Injection {

    fun providePostRepository(application: Application): PostRepository {
        return PostRepository.getInstance(application)
    }
}
