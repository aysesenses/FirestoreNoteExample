package com.android.firestorenoteexample

import android.app.Application
import timber.log.Timber

class NoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}