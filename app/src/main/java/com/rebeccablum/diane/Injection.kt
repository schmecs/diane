package com.rebeccablum.diane

import android.app.Application

object Injection {

    fun providePostRepository(application: Application): PostRepository {
        return PostRepository.getInstance(application)
    }
}
