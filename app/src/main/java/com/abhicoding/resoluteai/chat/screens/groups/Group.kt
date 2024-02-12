package com.abhicoding.resoluteai.chat.screens.groups

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class Group(
    val groupName: String? = "Default Name",
    val groupImage: ImageVector = Icons.Filled.Home,
    val groupDescription: String? = "Default Description"
)

val mockGroupList =
    listOf(
        Group("Group 1",
            Icons.Filled.Home,
            "Group 1 Description"),
        Group("Group 2",
            Icons.Filled.Home,
            "Group 2 Description"),
        Group("Group 3",
            Icons.Filled.Home,
            "Group 3 Description")
    )
