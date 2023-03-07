package com.best.now.autoclick

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.best.now.autoclick.lifecycle.KtxAppLifeObserver
import com.best.now.myad.utils.AppOpenManager
import com.best.now.myad.utils.initOkGo
import com.gm.phonecleaner.PhoneCleanerApp

/**
author:zhoujingjin
date:2022/11/27
 */
class AutoClickApplication: PhoneCleanerApp() {
    companion object {
        var appOpenManager: AppOpenManager? = null
        var appIsOn = true
    }
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        initOkGo(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(KtxAppLifeObserver())
        appOpenManager = AppOpenManager(this)
    }
}