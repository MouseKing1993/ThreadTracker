package com.amber.lib.thread_tracker

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.amber.lib.thread_tracker.proxy.AsyncTaskHook

class ThreadTrackerInitializer : Initializer<Any> {
    override fun create(context: Context): Any {
        AsyncTaskHook.hook()
        Log.d(LOG_TAG, "ThreadTracker Initialize")
        UserPackage.buildPackageList()
        UserPackage.packageList.removeAt(0)
        Log.d(LOG_TAG, "package list:")
        UserPackage.packageList.forEach {
            Log.d(LOG_TAG, it)
        }
        return Unit
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}