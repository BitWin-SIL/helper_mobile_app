package com.bitwin.helperapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bitwin.helperapp.core.theme.HelperAppTheme
import com.bitwin.helperapp.core.routing.HelperAppNavHost
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.bitwin.helperapp.core.routing.Screen
import com.bitwin.helperapp.core.session.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun HelperApp(viewModel: MainViewModel) {
    val startDestination by viewModel.startDestination.collectAsState()
    
    HelperAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            HelperAppNavHost(
                navController = navController,
                startDestination = startDestination
            )
        }
    }
}


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager
) : ViewModel() {
    
    private val _startDestination = MutableStateFlow(Screen.Login.route)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()
    
    init {
        checkAuthStatus()
    }
    
    private fun checkAuthStatus() {
        viewModelScope.launch {
            val isLoggedIn = userSessionManager.isLoggedIn()
            _startDestination.value = if (isLoggedIn) {
                Screen.Home.route
            } else {
                Screen.Login.route
            }
        }
    }
}