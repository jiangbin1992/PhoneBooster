package com.best.now.autoclick.lifecycle

import android.util.Log
import androidx.lifecycle.*
import com.best.now.autoclick.AutoClickApplication.Companion.appIsOn

class KtxAppLifeObserver : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> Log.d("event", "onCreate")
            Lifecycle.Event.ON_START -> Log.d("event", "onStart")
            Lifecycle.Event.ON_RESUME -> {
                appIsOn = true
                Log.d("event", "onResume")
            }
            Lifecycle.Event.ON_PAUSE -> {
                appIsOn = false
                Log.d("event", "onPause")
            }
            Lifecycle.Event.ON_STOP -> Log.d("event", "onStop")
            Lifecycle.Event.ON_DESTROY -> Log.d("event", "onDestory")
            Lifecycle.Event.ON_ANY->Log.d("event","onAny")
        }
    }
}