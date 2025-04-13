package com.bitwin.helperapp.core.routing

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Assistance : Screen("assistance")
    data object Notifications : Screen("notifications")
    data object EditPersonalInfoScreen : Screen("edit_personal_info")

    
    companion object {
        fun fromRoute(route: String): Screen {
            return when {
                route == Home.route -> Home
                route == Profile.route -> Profile
                route == Login.route -> Login
                route == Register.route -> Register
                route == Assistance.route -> Assistance
                route == Notifications.route -> Notifications
                route == EditPersonalInfoScreen.route -> EditPersonalInfoScreen
                else -> Home
            }
        }
    }
}