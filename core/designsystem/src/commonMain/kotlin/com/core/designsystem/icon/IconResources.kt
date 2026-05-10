package com.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Centralized icon resources for the app.
 * Uses Material Icons (ImageVector) for multiplatform compatibility.
 */
object IconResources {
    val ArrowBack: ImageVector = Icons.AutoMirrored.Filled.ArrowBack
    val ErrorOutline: ImageVector = Icons.Outlined.ErrorOutline
    val Search: ImageVector = Icons.Filled.Search
    val LocationOn: ImageVector = Icons.Filled.LocationOn
    val Language: ImageVector = Icons.Filled.Language
    val Info: ImageVector = Icons.Filled.Info
    val CalendarToday: ImageVector = Icons.Filled.CalendarToday
    val StarFilled: ImageVector = Icons.Filled.Star
    val StarOutline: ImageVector = Icons.Outlined.StarOutline
    val HomeFilled: ImageVector = Icons.Filled.Home
    val HomeOutline: ImageVector = Icons.Outlined.Home
}
