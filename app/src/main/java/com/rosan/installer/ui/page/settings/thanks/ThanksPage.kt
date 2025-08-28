package com.rosan.installer.ui.page.settings.thanks

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.kyant.capsule.G2RoundedCornerShape
import com.mikepenz.aboutlibraries.ui.compose.android.rememberLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.rosan.installer.R
import com.rosan.installer.build.Level
import com.rosan.installer.build.RsConfig
import com.rosan.installer.ui.theme.none
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val cardRadius = 18.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThanksPage(
    navController: NavController,
    windowInsets: WindowInsets
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val libraries by rememberLibraries(R.raw.aboutlibraries)

    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(windowInsets)
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.none,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.thanks_link)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LibrariesContainer(
            libraries = libraries,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = paddingValues,
            header = {
                item { StatusWidget() }
            },
            divider = {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        )
    }
}

@Composable
fun StatusWidget() {
    val context = LocalContext.current

    val baseColor = when (RsConfig.LEVEL) {
        Level.STABLE -> MaterialTheme.colorScheme.primaryContainer
        Level.PREVIEW -> MaterialTheme.colorScheme.secondaryContainer
        Level.UNSTABLE -> MaterialTheme.colorScheme.tertiaryContainer
    }
    val onBaseColor = when (RsConfig.LEVEL) {
        Level.STABLE -> MaterialTheme.colorScheme.onPrimaryContainer
        Level.PREVIEW -> MaterialTheme.colorScheme.onSecondaryContainer
        Level.UNSTABLE -> MaterialTheme.colorScheme.onTertiaryContainer
    }
    val levelText = when (RsConfig.LEVEL) {
        Level.STABLE -> stringResource(R.string.stable)
        Level.PREVIEW -> stringResource(R.string.preview)
        Level.UNSTABLE -> stringResource(R.string.unstable)
    }

    val transition = rememberInfiniteTransition(label = "status_widget_transition")

    val primaryFlow by transition.animateColor(
        initialValue = baseColor.copy(alpha = 0.9f),
        targetValue = baseColor.copy(alpha = 0.6f).compositeOver(Color.Magenta.copy(alpha = 0.2f)),
        animationSpec = infiniteRepeatable(
            tween(4500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "primary_flow"
    )

    val secondaryFlow by transition.animateColor(
        initialValue = baseColor.copy(alpha = 0.7f).compositeOver(Color.Cyan.copy(alpha = 0.25f)),
        targetValue = baseColor.copy(alpha = 0.85f).compositeOver(Color.Blue.copy(alpha = 0.15f)),
        animationSpec = infiniteRepeatable(
            tween(3800, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "secondary_flow"
    )

    val accentFlow by transition.animateColor(
        initialValue = baseColor.copy(alpha = 0.6f).compositeOver(Color.Yellow.copy(alpha = 0.1f)),
        targetValue = baseColor.copy(alpha = 0.8f).compositeOver(Color.Green.copy(alpha = 0.18f)),
        animationSpec = infiniteRepeatable(
            tween(5200, easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)),
            repeatMode = RepeatMode.Reverse
        ),
        label = "accent_flow"
    )

    val complementFlow by transition.animateColor(
        initialValue = baseColor.copy(alpha = 0.5f).compositeOver(Color.Red.copy(alpha = 0.12f)),
        targetValue = baseColor.copy(alpha = 0.75f)
            .compositeOver(Color(0xFFFF6B35).copy(alpha = 0.2f)),
        animationSpec = infiniteRepeatable(
            tween(4200, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "complement_flow"
    )

    var timeState by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { frameTimeNanos ->
                timeState = (frameTimeNanos / 1_000_000_000.0).toFloat()
            }
        }
    }

    val time1 = timeState * 0.1f
    val time2 = timeState * 0.133f
    val time3 = timeState * 0.167f
    val microTime = timeState * 0.2f

    val layerAlpha1 by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            tween(8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "layer_alpha_1"
    )

    val layerAlpha2 by transition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            tween(12000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "layer_alpha_2"
    )

    val layerAlpha3 by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "layer_alpha_3"
    )
    ElevatedCard(
        shape = G2RoundedCornerShape(cardRadius)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(baseColor.copy(alpha = 0.15f)),
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(layerAlpha1)
            ) {
                val width = size.width
                val height = size.height
                val centerX = width / 2
                val centerY = height / 2
                val maxRadius = maxOf(width, height)

                val flowCenters = listOf(
                    Offset(
                        x = centerX + width * 0.45f * sin(time1 * 0.8f + 0.5f) + width * 0.15f * cos(
                            time2 * 0.7f
                        ),
                        y = centerY + height * 0.4f * cos(time1 * 0.9f) + height * 0.12f * sin(time2 * 1.1f + 1.2f)
                    ),
                    Offset(
                        x = centerX + width * 0.5f * cos(time1 * 0.6f + 2.1f) + width * 0.18f * sin(
                            time2 * 0.9f + 0.8f
                        ),
                        y = centerY + height * 0.42f * sin(time1 * 0.7f + 1.5f) + height * 0.15f * cos(
                            time2 * 0.8f + 2.0f
                        )
                    ),
                    Offset(
                        x = centerX + width * 0.38f * sin(time1 * 0.75f + 3.8f) + width * 0.2f * cos(
                            microTime * 1.2f
                        ),
                        y = centerY + height * 0.35f * cos(time1 * 0.85f + 2.7f) + height * 0.17f * sin(
                            microTime * 1.0f + 1.1f
                        )
                    )
                )

                val radii = listOf(
                    maxRadius * 0.8f + maxRadius * 0.12f * sin(microTime * 0.8f),
                    maxRadius * 0.75f + maxRadius * 0.15f * cos(microTime * 0.9f + 1.0f),
                    maxRadius * 0.85f + maxRadius * 0.1f * sin(microTime * 1.1f + 2.3f)
                )

                val colors = listOf(primaryFlow, secondaryFlow, accentFlow)

                for (i in flowCenters.indices) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colors[i],
                                colors[i].copy(alpha = colors[i].alpha * 0.6f),
                                Color.Transparent
                            ),
                            center = flowCenters[i],
                            radius = radii[i]
                        ),
                        center = flowCenters[i],
                        radius = radii[i]
                    )
                }
            }

            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(layerAlpha2)
            ) {
                val width = size.width
                val height = size.height
                val centerX = width / 2
                val centerY = height / 2
                val maxRadius = maxOf(width, height)

                val fluidCenters = (0..4).map { i ->
                    val phase = i * PI.toFloat() / 2.5f
                    Offset(
                        x = centerX + width * 0.35f * sin(time2 * 0.6f + phase) +
                                width * 0.2f * cos(time3 * 0.5f + phase * 1.5f) +
                                width * 0.08f * sin(microTime * 1.5f + phase * 0.8f),
                        y = centerY + height * 0.32f * cos(time2 * 0.7f + phase * 1.2f) +
                                height * 0.25f * sin(time3 * 0.6f + phase * 0.7f) +
                                height * 0.1f * cos(microTime * 1.3f + phase * 1.3f)
                    )
                }

                val fluidRadii = (0..4).map { i ->
                    val baseRadius = maxRadius * (0.55f + 0.15f * sin(i.toFloat()))
                    baseRadius + maxRadius * 0.12f * cos(microTime * (0.8f + i * 0.2f))
                }

                val fluidColors = listOf(
                    secondaryFlow.copy(alpha = secondaryFlow.alpha * 0.8f),
                    accentFlow.copy(alpha = accentFlow.alpha * 0.7f),
                    complementFlow.copy(alpha = complementFlow.alpha * 0.9f),
                    primaryFlow.copy(alpha = primaryFlow.alpha * 0.6f),
                    secondaryFlow.copy(alpha = secondaryFlow.alpha * 0.75f)
                )

                for (i in fluidCenters.indices) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                fluidColors[i],
                                fluidColors[i].copy(alpha = fluidColors[i].alpha * 0.4f),
                                fluidColors[i].copy(alpha = fluidColors[i].alpha * 0.1f),
                                Color.Transparent
                            ),
                            center = fluidCenters[i],
                            radius = fluidRadii[i]
                        ),
                        center = fluidCenters[i],
                        radius = fluidRadii[i]
                    )
                }
            }

            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(layerAlpha3)
            ) {
                val width = size.width
                val height = size.height
                val centerX = width / 2
                val centerY = height / 2
                val maxRadius = maxOf(width, height)

                val fastTime = microTime * 2.5f
                val mediumTime = time1 * 1.5f

                val texturePoints = (0..7).map { i ->
                    val angle = i * 2 * PI.toFloat() / 8
                    val dynamicRadius = width * 0.3f + width * 0.2f * sin(fastTime + angle * 1.5f)
                    val turbulentOffset = width * 0.06f * cos(fastTime * 0.8f + angle * 2f)

                    Offset(
                        x = centerX + dynamicRadius * cos(angle + mediumTime * 0.3f) + turbulentOffset,
                        y = centerY + dynamicRadius * sin(angle + mediumTime * 0.3f) +
                                height * 0.05f * sin(fastTime * 1.2f + angle)
                    )
                }

                for (i in texturePoints.indices) {
                    val dynamicRadius = maxRadius * (0.2f + 0.1f * sin(fastTime + i * 0.5f))
                    val opacity = 0.25f + 0.15f * cos(fastTime * 0.7f + i * 0.3f)

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                when (i % 4) {
                                    0 -> primaryFlow.copy(alpha = opacity)
                                    1 -> secondaryFlow.copy(alpha = opacity)
                                    2 -> accentFlow.copy(alpha = opacity)
                                    else -> complementFlow.copy(alpha = opacity)
                                },
                                Color.Transparent
                            ),
                            center = texturePoints[i],
                            radius = dynamicRadius
                        ),
                        center = texturePoints[i],
                        radius = dynamicRadius
                    )
                }
            }

            Canvas(modifier = Modifier.matchParentSize()) {
                val width = size.width
                val height = size.height
                val maxRadius = maxOf(width, height)

                val glowTime = time1 * 0.5f

                val glowCenter = Offset(width / 2, height / 2)
                val glowRadius = maxRadius * (0.75f + 0.15f * sin(glowTime * 0.6f))
                val glowIntensity = 0.08f + 0.04f * cos(glowTime * 0.8f)

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            baseColor.copy(alpha = glowIntensity),
                            baseColor.copy(alpha = glowIntensity * 0.5f),
                            Color.Transparent
                        ),
                        center = glowCenter,
                        radius = glowRadius
                    ),
                    center = glowCenter,
                    radius = glowRadius
                )
            }

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.Transparent,
                    contentColor = onBaseColor
                ),
                elevation = CardDefaults.cardElevation(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.secondary) {
                        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Image(
                                modifier = Modifier.size(56.dp),
                                painter = rememberDrawablePainter(
                                    ContextCompat.getDrawable(context, R.mipmap.ic_launcher)
                                ),
                                contentDescription = stringResource(R.string.app_name)
                            )
                        }
                    }

                    ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
                        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Text(
                                text = stringResource(R.string.app_name),
                                color = onBaseColor
                            )
                        }
                    }

                    Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(
                            text = "$levelText [${RsConfig.versionName} (${RsConfig.versionCode})]",
                            style = MaterialTheme.typography.bodyMedium,
                            color = onBaseColor
                        )
                    }
                }
            }
        }
    }
}