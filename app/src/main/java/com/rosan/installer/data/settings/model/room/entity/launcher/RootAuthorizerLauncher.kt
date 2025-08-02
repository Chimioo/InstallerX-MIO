package com.rosan.installer.data.settings.model.room.entity.launcher

import android.util.Log
import com.rosan.installer.App
import com.rosan.installer.data.settings.model.room.entity.ConfigEntity
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "RootLauncher"

object RootAuthorizerLauncher : AuthorizerLauncher {
    override fun launchApp(packageName: String,configEntity: ConfigEntity) {
        val context = App.context
        val intent = context.packageManager?.getLaunchIntentForPackage(packageName)
        val activity = intent?.component?.flattenToString() ?: return

        CoroutineScope(Dispatchers.IO).launch {
            val cmd = "am start -n $activity"
            runCatching { Shell.cmd(cmd).exec() }
                    .onSuccess {
                        Log.d(
                                TAG,
                                if (it.isSuccess) "Launched $packageName with root"
                                else "Launch failed: ${it.out}"
                        )
                    }
                    .onFailure { Log.e(TAG, "Root launch error", it) }
        }
    }
}
