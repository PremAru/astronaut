package com.space.astronaut

import android.app.Application
import com.space.astronaut.di.AppComponent
import com.space.astronaut.di.DaggerAppComponent
import com.squareup.picasso.BuildConfig
import timber.log.Timber

class AstronautApplication : Application() {
    val appComponent: AppComponent by lazy { initializeAppComponent() }

    private fun initializeAppComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}