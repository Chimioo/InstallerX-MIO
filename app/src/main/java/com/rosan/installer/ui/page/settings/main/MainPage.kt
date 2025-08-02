package com.rosan.installer.ui.page.settings.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rosan.installer.R
import com.rosan.installer.ui.page.settings.config.all.AllPage
import com.rosan.installer.ui.page.settings.home.HomePage
import com.rosan.installer.ui.page.settings.preferred.PreferredPage
import com.rosan.installer.ui.theme.exclude
import kotlinx.coroutines.launch

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
                    ) { PreferredPage(it) }
            )

    val pagerState = rememberPagerState(pageCount = { data.size })
    val coroutineScope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage
    fun onPageChanged(page: Int) {
        coroutineScope.launch { pagerState.animateScrollToPage(page) }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLandscapeScreen = maxHeight / maxWidth > 1.4

        val navigationSide =
                if (isLandscapeScreen) WindowInsetsSides.Bottom else WindowInsetsSides.Left

        //        val navigationWindowInsets = WindowInsets.safeDrawing.only(
        //            (if (isLandscapeScreen) WindowInsetsSides.Horizontal
        //            else WindowInsetsSides.Vertical) + navigationSide
        //        )
        val pageWindowInsets = WindowInsets.safeDrawing.exclude(navigationSide)

        Row(modifier = Modifier.fillMaxSize()) {
            //            if (!isLandscapeScreen) {
            //                ColumnNavigation(
            //                    windowInsets = navigationWindowInsets,
            //                    data = data,
            //                    currentPage = currentPage,
            //                    onPageChanged = { onPageChanged(it) }
            //                )
            //            }

            Column(modifier = Modifier.weight(1f).fillMaxSize()) {
                HorizontalPager(state = pagerState, modifier = Modifier.weight(1f).fillMaxSize()) {
                        page ->
                    data[page].content.invoke(pageWindowInsets)
                }
                //                if (isLandscapeScreen) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    FloatingNavigationBar(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            data = data,
                            currentIndex = currentPage,
                            onItemSelected = { onPageChanged(it) },
                            actionButtonIcon = Icons.TwoTone.Add,
                            actionButtonLabel = "Add",
                            onActionButtonClick = {},
                            showActionButton = currentPage == 1,
                            showNavigationBar = true
                    )
                }
                //                }
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

    Row(
            modifier = modifier.padding(20.dp).wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.CenterStart) {
            androidx.compose.animation.AnimatedVisibility(
                    visible =
                            showActionButton &&
                                    actionButtonIcon != null &&
                                    onActionButtonClick != null,
                    enter =
                            fadeIn(animationSpec = tween(300)) +
                                    slideInHorizontally(
                                            animationSpec = tween(300),
                                            initialOffsetX = { it }
                                    ),
                    exit =
                            fadeOut(animationSpec = tween(300)) +
                                    slideOutHorizontally(
                                            animationSpec = tween(300),
                                            targetOffsetX = { it }
                                    )
            ) {
                    SmallFloatingActionButton(
                            onClick = onActionButtonClick!!,
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape
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
                visible = showNavigationBar,
                enter =
                        fadeIn(animationSpec = tween(300)) +
                                slideInVertically(
                                        animationSpec = tween(300),
                                        initialOffsetY = { it }
                                ),
                exit =
                        fadeOut(animationSpec = tween(300)) +
                                slideOutVertically(
                                        animationSpec = tween(300),
                                        targetOffsetY = { it }
                                )
        ) {
            ElevatedCard(
                    modifier = Modifier.wrapContentSize(),
                    shape = RoundedCornerShape(60.dp),
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

                        val cornerRadius by
                                animateDpAsState(
                                        targetValue = if (isSelected) 30.dp else 50.dp,
                                        animationSpec = tween(300),
                                        label = "cornerRadius"
                                )
                        val scale by
                                animateFloatAsState(
                                        targetValue = if (isSelected) 1.05f else 1f,
                                        animationSpec = tween(300),
                                        label = "scale"
                                )
                        val width by
                                animateDpAsState(
                                        targetValue = if (isSelected) 90.dp else 48.dp,
                                        animationSpec = tween(300),
                                        label = "width"
                                )
                        val textAlpha by
                                animateFloatAsState(
                                        targetValue = if (isSelected) 1f else 0f,
                                        animationSpec = tween(300),
                                        label = "textAlpha"
                                )
                        val backgroundColor by
                                animateColorAsState(
                                        targetValue =
                                                if (isSelected)
                                                        MaterialTheme.colorScheme.primaryContainer
                                                else Color.Transparent,
                                        animationSpec = tween(300),
                                        label = "backgroundColor"
                                )
                        val pressedScale by
                                animateFloatAsState(
                                        targetValue = if (isPressed) 0.9f else 1f,
                                        animationSpec = tween(100),
                                        label = "pressedScale"
                                )

                        Card(
                                modifier =
                                        Modifier.height(if (isSelected) 56.dp else 48.dp)
                                                .width(width)
                                                .clickable(
                                                        interactionSource = interactionSource,
                                                        indication = null
                                                ) { onItemSelected(index) },
                                shape = RoundedCornerShape(cornerRadius),
                                colors = CardDefaults.cardColors(containerColor = backgroundColor)
                        ) {
                            Box(
                                    modifier =
                                            Modifier.fillMaxSize()
                                                    .padding(if (isSelected) 8.dp else 12.dp),
                                    contentAlignment = Alignment.Center
                            ) {
                                Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                            imageVector = navData.icon,
                                            contentDescription = navData.label,
                                            modifier = Modifier.size(24.dp),
                                            tint =
                                                    if (isSelected)
                                                            MaterialTheme.colorScheme
                                                                    .onPrimaryContainer
                                                    else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                            text = navData.label,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.alpha(textAlpha)
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
