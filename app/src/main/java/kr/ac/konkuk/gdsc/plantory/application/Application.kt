package kr.ac.konkuk.gdsc.plantory.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kr.ac.konkuk.gdsc.plantory.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {

    init {
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
