package com.example.my_tv_application.Ui_layer

import android.content.Context
import android.content.pm.PackageManager
import com.example.my_tv_application.AppInfo

fun getApprovedApps(context: Context): List<AppInfo> {
    val approvedPackages = listOf(
        "com.google.android.youtube.tv",

        "com.google.android.tvlauncher",
        "com.plexapp.android",
        "com.hbo.hbonow",
        "tv.twitch.android.app",
    )

    val pm = context.packageManager
    return pm.getInstalledApplications(PackageManager.GET_META_DATA)
        .filter { it.packageName in approvedPackages }
        .map {
            AppInfo(
                name = it.loadLabel(pm).toString(),
                icon = it.loadIcon(pm),
                packageName = it.packageName
            )
        }
}