package com.rosan.installer.data.settings.model.room.entity.launcher

import android.content.Intent
import com.rosan.installer.App
import com.rosan.installer.data.settings.model.room.entity.ConfigEntity

object NoneAuthorizerLauncher : AuthorizerLauncher {
    override fun launchApp(packageName: String,configEntity: ConfigEntity) {
        val context = App.context
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
