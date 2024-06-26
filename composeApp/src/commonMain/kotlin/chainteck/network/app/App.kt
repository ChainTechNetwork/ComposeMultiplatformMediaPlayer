package chainteck.network.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import chainteck.network.app.font.FontType
import chainteck.network.app.font.MediaFont
import chainteck.network.app.navigation.LocalNavigation
import chainteck.network.app.navigation.LocalScreenContainer
import chainteck.network.app.navigation.NavigationProvider
import chainteck.network.app.navigation.ScreenContainerProvider
import chainteck.network.app.theme.MyApplicationTheme
import chainteck.network.app.ui.screens.tabsview.HomeTab
import chainteck.network.app.ui.screens.tabsview.MusicTab
import chainteck.network.app.ui.screens.tabsview.ReelsTab
import chainteck.network.app.utility.BottomNavigationBarHeight
import chainteck.network.app.utility.getSafeAreaSize
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun MainView() {
    MyApplicationTheme {
        val navigation = remember { NavigationProvider() }
        val screenContainer = remember { ScreenContainerProvider() }
        CompositionLocalProvider(
            LocalScreenContainer provides screenContainer,
            LocalNavigation provides navigation,
        ) {
            TabNavigator(
                HomeTab,
                tabDisposable = {
                    TabDisposable(
                        navigator = it,
                        tabs = listOf(HomeTab, ReelsTab, MusicTab)
                    )
                }
            ) {
                Navigator(HomeScreen()) {
                    navigation.initialize()
                    SlideTransition(it)
                }
            }
        }
    }
}

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        Scaffold(
            modifier = Modifier
                .background(color = MyApplicationTheme.colors.bottomTabBarColor)
                .padding(bottom = getSafeAreaSize().bottom.dp),
            scaffoldState = rememberScaffoldState(),
            backgroundColor = MyApplicationTheme.colors.bottomTabBarColor,
            bottomBar = {
                BottomNavigation(
                    modifier = Modifier
                        .height(BottomNavigationBarHeight),
                    contentColor = MyApplicationTheme.colors.bottomTabBarColor,
                    backgroundColor = MyApplicationTheme.colors.bottomTabBarColor,
                ) {
                    TabNavigationItem(tab = HomeTab)
                    TabNavigationItem(tab = ReelsTab)
                    TabNavigationItem(tab = MusicTab)
                }
            },
        ) {
            CurrentTab()
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val title = tab.options.title
    BottomNavigationItem(
        modifier = Modifier
            .background(MyApplicationTheme.colors.bottomTabBarColor),
        alwaysShowLabel = true,
        label = {
            Text(
                modifier = Modifier
                    .padding(bottom = 4.sdp),
                text = title,
                style = MediaFont.lexendDeca(
                    size = FontType.Small,
                    type = MediaFont.LexendDeca.Regular
                ),
            )
        },
        selected = tabNavigator.current.key == tab.key,
        onClick = { tabNavigator.current = tab },
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = tab.options.title,
                    modifier = Modifier
                        .padding(top = 10.sdp, bottom = 5.sdp)
                        .size(20.sdp)
                )
            }
        },
        selectedContentColor = Color.White,
        unselectedContentColor = MyApplicationTheme.colors.secondaryText
    )
}