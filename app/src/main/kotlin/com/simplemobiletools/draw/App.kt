package com.simplemobiletools.draw

import android.app.Application
import com.simplemobiletools.draw.BuildConfig.USE_LEAK_CANARY
import com.simplemobiletools.draw.extensions.config
import com.squareup.leakcanary.LeakCanary
import java.util.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (USE_LEAK_CANARY) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return
            }
            LeakCanary.install(this)
        }

        if (config.useEnglish) {
            val conf = resources.configuration
            conf.locale = Locale.ENGLISH
            resources.updateConfiguration(conf, resources.displayMetrics)
        }
    }
}
