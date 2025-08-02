package com.rosan.installer.data.settings.model.room.entity.launcher

import com.rosan.installer.App
import com.rosan.installer.data.recycle.util.useUserService
import com.rosan.installer.data.settings.model.room.entity.ConfigEntity

private const val TAG = "ShizukuAuthorizerLauncher"

object ShizukuAuthorizerLauncher : AuthorizerLauncher {
    override fun launchApp(packageName: String, configEntity: ConfigEntity) {
        val context = App.context
        val intent = context.packageManager?.getLaunchIntentForPackage(packageName)
        val activity = intent?.component?.flattenToString() ?: return
        val cmd = "am start -n $activity"

        useUserService(configEntity) { userService ->
            userService.privileged.execArr(arrayOf("/system/bin/sh","-c",cmd))
        }
    }
}