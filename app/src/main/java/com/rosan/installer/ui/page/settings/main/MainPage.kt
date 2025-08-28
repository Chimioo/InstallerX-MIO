package com.rosan.installer.ui.page.settings.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.RoomPreferences
import androidx.compose.material.icons.twotone.SettingsSuggest
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kyant.capsule.G2RoundedCornerShape
import com.rosan.installer.R
import com.rosan.installer.ui.page.settings.SettingsScreen
import com.rosan.installer.ui.page.settings.config.all.AllPage
import com.rosan.installer.ui.page.settings.home.HomePage
import com.rosan.installer.ui.page.settings.preferred.PreferredPage
import com.rosan.installer.ui.theme.exclude
import kotlinx.coroutines.launch

val cardRadius = 18.dp

@Composable
fun MainPage(navController: NavController) {
    val data =
        arrayOf(
            NavigationData(
                icon = Icons.TwoTone.Home,
                label = stringResource(R.string.home)
            ) { HomePage(navController, it) },
            NavigationData(
                icon = Icons.TwoTone.RoomPreferences,
                label = stringResource(R.string.config)
            ) { AllPage(navController, it) },
            NavigationData(
                icon = Icons.TwoTone.SettingsSuggest,
                label = stringResource(R.string.preferred)
            ) { PreferredPage(navController,it) }
        )

    val pagerState = rememberPagerState(pageCount = { data.size })
    val coroutineScope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage
    fun onPageChanged(page: Int) {
        coroutineScope.launch { pagerState.animateScrollToPage(page) }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isPortraitScreen =
            this@BoxWithConstraints.maxHeight / this@BoxWithConstraints.maxWidth > 1.4

        val navigationSide =
            if (isPortraitScreen) WindowInsetsSides.Left else WindowInsetsSides.Bottom


        //        val navigationWindowInsets = WindowInsets.safeDrawing.only(
        //            (if (isLandscapeScreen) WindowInsetsSides.Horizontal
        //            else WindowInsetsSides.Vertical) + navigationSide
        //        )
        val pageWindowInsets = WindowInsets.safeDrawing.exclude(navigationSide)
        val navigationBarInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)


        Row(modifier = Modifier.fillMaxSize()) {
            //            if (!isLandscapeScreen) {
            //                ColumnNavigation(
            //                    windowInsets = navigationWindowInsets,
            //                    data = data,
            //                    currentPage = currentPage,
            //                    onPageChanged = { onPageChanged(it) }
            //                )
            //            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                    ) { page ->
                        data[page].content.invoke(pageWindowInsets)
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .windowInsetsPadding(navigationBarInsets)
                    ) {
                        FloatingNavigationBar(
                            data = data,
                            currentIndex = currentPage,
                            onItemSelected = { onPageChanged(it) },
                            actionButtonIcon = Icons.TwoTone.Add,
                            actionButtonLabel = "Add",
                            onActionButtonClick = {
                                navController.navigate(
                                    SettingsScreen.Builder.EditConfig(null).route
                                )
                            },
                            showActionButton = currentPage == 1,
                            showNavigationBar = true
                        )
                    }
                }
            }
        }
    }
}

//
// @Composable
// fun RowNavigation(
//    windowInsets: WindowInsets,
//    data: Array<NavigationData>,
//    currentPage: Int,
//    onPageChanged: (Int) -> Unit
// ) {
//    NavigationBar(
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentSize(),
//        windowInsets = windowInsets
//    ) {
//        data.forEachIndexed { index, navigationData ->
//            NavigationBarItem(
//                selected = currentPage == index,
//                onClick = { onPageChanged(index) },
//                icon = {
//                    Icon(
//                        imageVector = navigationData.icon,
//                        contentDescription = navigationData.label
//                    )
//                },
//                label = {
//                    Text(text = navigationData.label)
//                },
//                alwaysShowLabel = false
//            )
//        }
//    }
// }
//
// @Composable
// fun ColumnNavigation(
//    windowInsets: WindowInsets,
//    data: Array<NavigationData>,
//    currentPage: Int,
//    onPageChanged: (Int) -> Unit
// ) {
//    NavigationRail(
//        modifier = Modifier
//            .fillMaxHeight()
//            .wrapContentSize(),
//        windowInsets = windowInsets
//    ) {
//        Spacer(
//            modifier = Modifier
//                .weight(1f)
//        )
//        data.forEachIndexed { index, navigationData ->
//            NavigationRailItem(
//                selected = currentPage == index,
//                onClick = { onPageChanged(index) },
//                icon = {
//                    Icon(
//                        imageVector = navigationData.icon,
//                        contentDescription = navigationData.label
//                    )
//                },
//                label = {
//                    Text(text = navigationData.label)
//                },
//                alwaysShowLabel = false
//            )
//        }
//    }
// }


// 这是一坨非常大的shi，来自一个不遵守开发规范的人
// 上面这些注释掉的以后可能会加回，到时候可能给个开关（我也不记得有没有删什么东西）
@Composable
fun FloatingNavigationBar(
    modifier: Modifier = Modifier,
    data: Array<NavigationData>,
    currentIndex: Int,
    onItemSelected: (Int) -> Unit,
    actionButtonIcon: ImageVector? = null,
    actionButtonLabel: String? = null,
    onActionButtonClick: (() -> Unit)? = null,
    showActionButton: Boolean = false,
    showNavigationBar: Boolean = true
) {
    if (!showNavigationBar && !showActionButton) return

    val bounceAnimationSpec =
        spring<Dp>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )

    val smoothAnimationSpec =
        spring<Float>(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )

    val colorAnimationSpec =
        spring<Color>(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow
        )

    Box(
        modifier = modifier
            .padding(20.dp)
            .wrapContentSize()
    ) {
        androidx.compose.animation.AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-86).dp),
            visible =
                showActionButton &&
                        actionButtonIcon != null &&
                        onActionButtonClick != null,
            enter =
                fadeIn(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                        slideInHorizontally(
                            animationSpec =
                                spring(
                                    dampingRatio =
                                        Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                ),
                            initialOffsetX = { it }
                        ) +
                        scaleIn(
                            animationSpec =
                                spring(
                                    dampingRatio =
                                        Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                            initialScale = 0.7f
                        ),
            exit =
                fadeOut(animationSpec = tween(300, easing = FastOutLinearInEasing)) +
                        slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { it }
                        ) +
                        scaleOut(animationSpec = tween(300), targetScale = 0.7f)
        ) {
            var isPressed by remember { mutableStateOf(false) }
            val buttonScale by
            animateFloatAsState(
                targetValue = if (isPressed) 0.92f else 1f,
                animationSpec =
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessHigh
                    ),
                label = "buttonScale"
            )

            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .scale(buttonScale)
            ) {
                FilledIconButton(
                    onClick = { onActionButtonClick!!() },
                    modifier =
                        Modifier
                            .size(64.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = CircleShape,
                                ambientColor =
                                    MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.1f
                                    ),
                                spotColor =
                                    MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.25f
                                    )
                            )
                            .pointerInput(key1 = Unit) {
                                detectTapGestures(
                                    onPress = {
                                        isPressed = true
                                        tryAwaitRelease()
                                        isPressed = false
                                    }
                                )
                            },
                    colors =
                        IconButtonDefaults.filledIconButtonColors(
                            containerColor =
                                MaterialTheme.colorScheme.primaryContainer
                        ),
                ) {
                    Icon(
                        imageVector = actionButtonIcon!!,
                        contentDescription = actionButtonLabel,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        androidx.compose.animation.AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterStart),
            visible = showNavigationBar,
            enter =
                fadeIn(animationSpec = tween(500, easing = FastOutSlowInEasing)) +
                        slideInVertically(
                            animationSpec =
                                spring(
                                    dampingRatio =
                                        Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                ),
                            initialOffsetY = { it / 2 }
                        ) +
                        scaleIn(
                            animationSpec =
                                spring(
                                    dampingRatio =
                                        Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                            initialScale = 0.8f
                        ),
            exit =
                fadeOut(animationSpec = tween(350, easing = FastOutLinearInEasing)) +
                        slideOutVertically(
                            animationSpec = tween(350),
                            targetOffsetY = { it / 2 }
                        ) +
                        scaleOut(animationSpec = tween(350), targetScale = 0.8f)
        ) {
            ElevatedCard(
                modifier = Modifier.wrapContentSize(),
                shape = G2RoundedCornerShape(60.dp),
                elevation =
                    CardDefaults.elevatedCardElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    data.forEachIndexed { index, navData ->
                        val isSelected = index == currentIndex
                        val interactionSource = remember { MutableInteractionSource() }
                        val isPressed by interactionSource.collectIsPressedAsState()
                        val isHovered by interactionSource.collectIsHoveredAsState()

                        val textMeasurer = rememberTextMeasurer()
                        val textLayoutResult = remember(navData.label) {
                            textMeasurer.measure(
                                text = navData.label,
                                style = androidx.compose.ui.text.TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                        val textWidth = with(LocalDensity.current) {
                            textLayoutResult.size.width.toDp()
                        }

                        val calculatedWidth = 24.dp + 8.dp + textWidth + 24.dp + 8.dp
                        val minSelectedWidth = 88.dp
                        val maxSelectedWidth = 180.dp

                        val cornerRadius by
                        animateDpAsState(
                            targetValue = if (isSelected) 28.dp else 50.dp,
                            animationSpec = bounceAnimationSpec,
                            label = "cornerRadius"
                        )

                        val width by
                        animateDpAsState(
                            targetValue =
                                when {
                                    isSelected -> calculatedWidth.coerceIn(
                                        minSelectedWidth,
                                        maxSelectedWidth
                                    )

                                    isHovered -> 52.dp
                                    else -> 48.dp
                                },
                            animationSpec = bounceAnimationSpec,
                            label = "width"
                        )

                        val height by
                        animateDpAsState(
                            targetValue =
                                when {
                                    isSelected -> 56.dp
                                    isPressed -> 46.dp
                                    isHovered -> 50.dp
                                    else -> 48.dp
                                },
                            animationSpec = bounceAnimationSpec,
                            label = "height"
                        )

                        val textAlpha by
                        animateFloatAsState(
                            targetValue = if (isSelected) 1f else 0f,
                            animationSpec = smoothAnimationSpec,
                            label = "textAlpha"
                        )

                        val iconScale by
                        animateFloatAsState(
                            targetValue =
                                when {
                                    isPressed -> 0.85f
                                    isSelected -> 1.1f
                                    isHovered -> 1.05f
                                    else -> 1f
                                },
                            animationSpec =
                                spring(
                                    dampingRatio =
                                        Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessHigh
                                ),
                            label = "iconScale"
                        )

                        val backgroundColor by
                        animateColorAsState(
                            targetValue =
                                when {
                                    isSelected ->
                                        MaterialTheme.colorScheme
                                            .primaryContainer

                                    isHovered ->
                                        MaterialTheme.colorScheme.surfaceVariant
                                            .copy(alpha = 0.3f)

                                    else -> Color.Transparent
                                },
                            animationSpec = colorAnimationSpec,
                            label = "backgroundColor"
                        )

                        val iconTint by
                        animateColorAsState(
                            targetValue =
                                when {
                                    isSelected ->
                                        MaterialTheme.colorScheme
                                            .onPrimaryContainer

                                    isHovered -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.onSurface
                                },
                            animationSpec = colorAnimationSpec,
                            label = "iconTint"
                        )

                        val rippleAlpha by
                        animateFloatAsState(
                            targetValue = if (isPressed) 0.12f else 0f,
                            animationSpec = tween(150),
                            label = "rippleAlpha"
                        )

                        Card(
                            modifier =
                                Modifier
                                    .height(height)
                                    .width(width)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) { onItemSelected(index) },
                            shape = G2RoundedCornerShape(cornerRadius),
                            colors = CardDefaults.cardColors(containerColor = backgroundColor),
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(
                                                alpha = rippleAlpha
                                            ),
                                            RoundedCornerShape(cornerRadius)
                                        )
                                        .padding(
                                            horizontal = 12.dp,
                                            vertical = 12.dp
                                        ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = navData.icon,
                                        contentDescription = navData.label,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .scale(iconScale),
                                        tint = iconTint
                                    )

                                    androidx.compose.animation.AnimatedVisibility(
                                        visible = isSelected,
                                        enter =
                                            fadeIn(
                                                animationSpec =
                                                    tween(
                                                        durationMillis = 300,
                                                        delayMillis = 100,
                                                        easing =
                                                            FastOutSlowInEasing
                                                    )
                                            ) +
                                                    slideInHorizontally(
                                                        animationSpec =
                                                            spring(
                                                                dampingRatio =
                                                                    Spring.DampingRatioMediumBouncy,
                                                                stiffness =
                                                                    Spring.StiffnessMedium
                                                            ),
                                                        initialOffsetX = { it / 4 }
                                                    ),
                                        exit =
                                            fadeOut(animationSpec = tween(200)) +
                                                    slideOutHorizontally(
                                                        animationSpec = tween(200),
                                                        targetOffsetX = { it / 4 }
                                                    )
                                    ) {
                                        Text(
                                            modifier = Modifier.alpha(textAlpha),
                                            text = navData.label,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color =
                                                MaterialTheme.colorScheme
                                                    .onPrimaryContainer,
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}