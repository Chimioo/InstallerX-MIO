package com.rosan.installer.data.settings.model.room.entity.launcher

import com.rosan.installer.data.settings.model.room.entity.ConfigEntity

interface AuthorizerLauncher {
    fun launchApp(packageName: String,configEntity: ConfigEntity)
}

fun getAuthorizerLauncher(authorizer: ConfigEntity.Authorizer): AuthorizerLauncher {
    return when (authorizer) {
        ConfigEntity.Authorizer.Root -> RootAuthorizerLauncher
        ConfigEntity.Authorizer.Shizuku -> ShizukuAuthorizerLauncher
        else -> NoneAuthorizerLauncher
    }
}
