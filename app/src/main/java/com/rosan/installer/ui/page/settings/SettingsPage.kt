package com.rosan.installer.ui.page.settings

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rosan.installer.ui.page.settings.config.apply.ApplyPage
import com.rosan.installer.ui.page.settings.config.edit.EditPage
import com.rosan.installer.ui.page.settings.main.MainPage
import com.rosan.installer.ui.page.settings.thanks.ThanksPage
import com.rosan.installer.ui.theme.none

@Composable
fun SettingsPage() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SettingsScreen.Main.route,
    ) {
        composable(
            route = SettingsScreen.Main.route,
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) { MainPage(navController = navController) }
        composable(
            route = SettingsScreen.EditConfig.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType }),
            enterTransition = {
                slideInVertically(
                    animationSpec =
                        spring(
                            dampingRatio = 0.8f,
                            stiffness = 350f
                        ),
                    initialOffsetY = { fullHeight ->
                        fullHeight
                    }
                ) +
                        fadeIn(
                            animationSpec =
                                spring(
                                    dampingRatio = 0.85f,
                                    stiffness = 400f
                                )
                        ) +
                        scaleIn(
                            animationSpec =
                                spring(
                                    dampingRatio = 0.75f,
                                    stiffness = 300f
                                ),
                            initialScale = 0.95f
                        )
            },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = {
                slideOutVertically(
                    animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = EaseOutQuart
                        ),
                    targetOffsetY = { fullHeight -> fullHeight }
                ) +
                        fadeOut(
                            animationSpec =
                                tween(
                                    durationMillis = 250,
                                    easing = EaseOutCubic
                                )
                        ) +
                        scaleOut(
                            animationSpec =
                                tween(
                                    durationMillis = 300,
                                    easing = EaseOutQuart
                                ),
                            targetScale = 0.97f
                        )
            }
        ) {
            val id = it.arguments?.getLong("id")
            EditPage(navController = navController, id = if (id != -1L) id else null)
        }

        composable(
            route = SettingsScreen.ApplyConfig.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType }),
            enterTransition = {
                slideInVertically(
                    animationSpec =
                        spring(
                            dampingRatio = 0.8f,
                            stiffness = 350f
                        ),
                    initialOffsetY = { fullHeight ->
                        fullHeight
                    }
                ) +
                        fadeIn(
                            animationSpec =
                                spring(
                                    dampingRatio = 0.85f,
                                    stiffness = 400f
                                )
                        ) +
                        scaleIn(
                            animationSpec =
                                spring(
                                    dampingRatio = 0.75f,
                                    stiffness = 300f
                                ),
                            initialScale = 0.95f
                        )
            },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = {
                slideOutVertically(
                    animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = EaseOutQuart
                        ),
                    targetOffsetY = { fullHeight -> fullHeight }
                ) +
                        fadeOut(
                            animationSpec =
                                tween(
                                    durationMillis = 250,
                                    easing = EaseOutCubic
                                )
                        ) +
                        scaleOut(
                            animationSpec =
                                tween(
                                    durationMillis = 300,
                                    easing = EaseOutQuart
                                ),
                            targetScale = 0.97f
                        )
            }
        ) {
            val id = it.arguments?.getLong("id")!!
            ApplyPage(navController = navController, id = id)
        }

        composable(
            route = SettingsScreen.Thanks.route,
            enterTransition = {
                slideInVertically(
                    animationSpec =
                        spring(
                            dampingRatio = 0.8f,
                            stiffness = 350f
                        ),
                    initialOffsetY = { fullHeight ->
                        fullHeight
                    }
                ) +
                        fadeIn(
                            animationSpec =
                                spring(
                                    dampingRatio = 0.85f,
                                    stiffness = 400f
                                )
                        ) +
                        scaleIn(
                            animationSpec =
                                spring(
                                    dampingRatio = 0.75f,
                                    stiffness = 300f
                                ),
                            initialScale = 0.95f
                        )
            },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = {
                slideOutVertically(
                    animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = EaseOutQuart
                        ),
                    targetOffsetY = { fullHeight -> fullHeight }
                ) +
                        fadeOut(
                            animationSpec =
                                tween(
                                    durationMillis = 250,
                                    easing = EaseOutCubic
                                )
                        ) +
                        scaleOut(
                            animationSpec =
                                tween(
                                    durationMillis = 300,
                                    easing = EaseOutQuart
                                ),
                            targetScale = 0.97f
                        )
            }
        ) {
            ThanksPage(navController = navController, windowInsets = WindowInsets.none)
        }
    }

}
