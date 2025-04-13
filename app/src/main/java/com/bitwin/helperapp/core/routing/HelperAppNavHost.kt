package com.bitwin.helperapp.core.routing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bitwin.helperapp.features.home.ui.HomeScreen
import com.bitwin.helperapp.features.login.ui.LoginScreen
import com.bitwin.helperapp.features.profile.ui.EditPersonalInfoScreen
import com.bitwin.helperapp.features.profile.ui.ProfileScreen
import com.bitwin.helperapp.features.register.ui.RegisterScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HelperAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(
            route = Screen.EditPersonalInfoScreen.route + "/{firstName}/{lastName}/{email}",
            arguments = listOf(
                navArgument("firstName") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
            val lastName = backStackEntry.arguments?.getString("lastName") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            
            EditPersonalInfoScreen(
                onBackClick = { navController.popBackStack() },
                firstName = firstName,
                lastName = lastName, 
                email = email
            )
        }
    }
}