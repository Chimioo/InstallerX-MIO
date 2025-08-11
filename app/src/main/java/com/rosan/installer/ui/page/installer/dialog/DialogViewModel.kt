package com.rosan.installer.ui.page.installer.dialog

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosan.installer.data.installer.model.entity.ProgressEntity
import com.rosan.installer.data.app.util.InstalledAppInfo
import com.rosan.installer.data.installer.repo.InstallerRepo
import com.rosan.installer.data.recycle.util.useUserService
import com.rosan.installer.data.settings.model.room.entity.ConfigEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class DialogViewModel(private var repo: InstallerRepo) : ViewModel(), KoinComponent {
    private val context by inject<Context>()

    var state by mutableStateOf<DialogViewState>(DialogViewState.Ready)
        private set

    private val installedSnapshot: MutableMap<String, Pair<Long, String>> = mutableMapOf()

    fun setInstalledSnapshot(packageName: String, versionCode: Long, versionName: String) {
        installedSnapshot[packageName] = Pair(versionCode, versionName)
    }

    fun getInstalledSnapshot(packageName: String): Pair<Long, String>? = installedSnapshot[packageName]

    fun dispatch(action: DialogViewAction) {
        when (action) {
            is DialogViewAction.CollectRepo -> collectRepo(action.repo)
            is DialogViewAction.Close -> close()
            is DialogViewAction.Analyse -> analyse()
            is DialogViewAction.InstallChoice -> installChoice()
            is DialogViewAction.InstallPrepare -> installPrepare()
            is DialogViewAction.Install -> install()
            is DialogViewAction.Background -> background()
            is DialogViewAction.LaunchApp -> launchApp(action.packageName)
        }
    }

    private fun launchApp(packageName: String) {
        val intent = context.packageManager
            .getLaunchIntentForPackage(packageName)
        if (repo.config.useAuthorizerLauncher) {

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    intent?.let {
                        if (repo.config.authorizer == ConfigEntity.Authorizer.Root ||
                            repo.config.authorizer == ConfigEntity.Authorizer.Shizuku
                        ) {
                            val activity = intent.component?.flattenToString()
                            val cmd = "am start -n $activity"
                            useUserService(repo.config) { userService ->
                                userService.privileged.execArr(arrayOf("/system/bin/sh", "-c", cmd))
                                dispatch(DialogViewAction.Close)
                            }
                            Timber.d("Authorizer launched $activity")
                        }
                    }
                } catch (e: Exception) {
                    Timber.e("Error launching app", e)
                    dispatch(DialogViewAction.Close)
                }
            }
        } else {
            context.startActivity(intent)
            dispatch(DialogViewAction.Close)
        }
    }

    private var collectRepoJob: Job? = null

    private fun collectRepo(repo: InstallerRepo) {
        this.repo = repo
        collectRepoJob?.cancel()
        collectRepoJob =
            viewModelScope.launch {
                repo.progress.collect { progress ->
                    state =
                        when (progress) {
                            is ProgressEntity.Ready -> DialogViewState.Ready
                            is ProgressEntity.Resolving -> DialogViewState.Resolving
                            is ProgressEntity.ResolvedFailed ->
                                DialogViewState.ResolveFailed

                            is ProgressEntity.Analysing -> DialogViewState.Analysing
                            is ProgressEntity.AnalysedFailed ->
                                DialogViewState.AnalyseFailed

                            is ProgressEntity.AnalysedSuccess ->
                                if (repo.entities
                                        .filter { it.selected }
                                        .groupBy { it.app.packageName }
                                        .size != 1
                                )
                                    DialogViewState.InstallChoice
                                else DialogViewState.InstallPrepare

                            is ProgressEntity.Installing -> DialogViewState.Installing
                            is ProgressEntity.InstallFailed -> DialogViewState.InstallFailed
                            is ProgressEntity.InstallSuccess ->
                                DialogViewState.InstallSuccess

                            else -> DialogViewState.Ready
                        }
                }
            }
    }

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun toast(@StringRes resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
    }

    private fun close() {
        repo.close()
    }

    private fun analyse() {
        repo.analyse()
    }

    private fun installChoice() {
        state = DialogViewState.InstallChoice
    }

    private fun installPrepare() {
        state = DialogViewState.InstallPrepare
    }

    private fun install() {
        // Take a snapshot of currently installed version before installation
        val packageName = repo.entities
            .filter { it.selected }
            .map { it.app.packageName }
            .firstOrNull()
        if (packageName != null) {
            val installed = InstalledAppInfo.buildByPackageName(packageName)
            if (installed != null) {
                setInstalledSnapshot(packageName, installed.versionCode, installed.versionName)
            }
        }
        repo.install()
    }

    private fun background() {
        repo.background(true)
    }

}
