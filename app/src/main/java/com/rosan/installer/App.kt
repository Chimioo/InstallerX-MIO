package com.rosan.installer

import android.app.Application
import android.content.Context
import com.rosan.installer.di.init.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        CrashHandler.init()
        super.onCreate()
        
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {

            // Koin Android Logger
            androidLogger()
            // Koin Android Context
            androidContext(this@App)
            // use modules
            modules(appModules)
        }
    }

    // 全局获取Context
    companion object {
        private var instance: App? = null
        val context: Context
            get() =
                    instance?.applicationContext
                            ?: throw IllegalStateException("App not initialized")
    }
}
