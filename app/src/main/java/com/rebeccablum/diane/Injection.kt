package com.rebeccablum.diane

import android.app.Application
import android.content.Context

object Injection {

    fun providePostRepository(application: Application): PostRepository {
        return PostRepository.getInstance(application)
    }
}
