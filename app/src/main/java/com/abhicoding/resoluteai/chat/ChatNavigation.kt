package com.abhicoding.resoluteai.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abhicoding.resoluteai.chat.screens.chats.ChatsScreen
import com.abhicoding.resoluteai.chat.screens.groups.GroupsScreen
import com.abhicoding.resoluteai.chat.screens.notifications.NotificationsScreen


@Composable
fun ChatNavigation(modifier: Modifier = Modifier) {

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navController = rememberNavController()
    Scaffold (
        bottomBar = {
            NavigationBar{
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination: NavDestination? = navBackStackEntry?.destination

                navItemList.forEachIndexed() { index, navItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(navItem.route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if(index == selectedItemIndex)
                                    navItem.selectedIcon
                                else
                                    navItem.unselectedIcon,
                                contentDescription = navItem.label,
                            )
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )

                }
            }
        }
    ){
        NavHost(
            navController = navController,
            startDestination = Screens.GroupsScreen.name,
            modifier = modifier.padding(it)
        ) {
            composable(route = Screens.GroupsScreen.name) {
                GroupsScreen()
            }
            composable(route = Screens.ChatsScreen.name) {
               ChatsScreen()
            }
            composable(route = Screens.NotificationsScreen.name) {
                NotificationsScreen()
            }
        }
    }
}