package com.example.wastebank.presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wastebank.presentation.ui.theme.YellowMain

@Composable
fun AdminBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Maps,
        BottomNavItem.Market,
        BottomNavItem.Article,
        BottomNavItem.Profile
    )

    // daftar screen yang menampilkan BottomNav
    val sectionRoutes = mapOf(
        "home_screen" to listOf("home_screen"),
        "maps_screen" to listOf("maps_screen", "request_screen"),
        "marketplace_screen" to listOf("marketplace_screen", "donate_screen"),
        "article_screen" to listOf("article_screen"),
        "profile_screen" to listOf("profile_screen", "edit_profile_screen")
    )

    // screen yang tidak menampilkan BottomNav
    val hiddenScreens = listOf(
        "splash_screen",
        "admin_login_screen",
        "user_login_screen",
        "register_screen",
        "forgot_password_screen",
        "set_new_password_screen",
        "cart_screen",
        "donation_detail_screen",
        "payment_screen",
        "product_detail_screen",
        "edit_profile_screen"
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    // apakah screen perlu menampilkan BottomNav
    val shouldShowBottomNav = !hiddenScreens.any { hiddenRoute ->
        currentRoute.startsWith(hiddenRoute)
    }

    // tampilkan BottomNav jika tidak berada di list hidden screen
    if (shouldShowBottomNav) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .border(
                    width = 1.dp,
                    color = YellowMain,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(1.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                ),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            color = Color.White,
            shadowElevation = 10.dp

        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = sectionRoutes[item.route]?.contains(currentRoute) == true

                    Box(
                        modifier = Modifier.size(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                if (!isSelected) {
                                    navController.navigate(item.route) {
                                        popUpTo(item.route)
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                painter = painterResource(id = if (isSelected) item.icon else item.iconOff),
                                contentDescription = item.title,
                                tint = YellowMain
                            )
                        }
                    }
                }
            }
        }
    }
}


//@Preview(showBackground = false)
//@Composable
//fun AdminBottomNavigationPreview() {
//    val navController = rememberNavController()
//    BottomNavigation(navController = navController)
//}