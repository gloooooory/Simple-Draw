package com.simplemobiletools.draw

import android.app.Application
import com.simplemobiletools.commons.extensions.checkUseEnglish
import com.simplemobiletools.draw.BuildConfig.USE_LEAK_CANARY
import com.squareup.leakcanary.LeakCanary

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (USE_LEAK_CANARY) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return
            }
            LeakCanary.install(this)
        }

        checkUseEnglish()
    }
}
