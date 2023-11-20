package com.amber.lib.thread_tracker

object UserPackage {

    @JvmStatic
    val packageList = ArrayList<String>()

    @JvmStatic
    fun buildPackageList() {
        packageList.add("--")
    }
}