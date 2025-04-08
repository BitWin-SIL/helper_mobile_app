package com.bitwin.helperapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bitwin.helperapp.core.theme.HelperAppTheme
import com.bitwin.helperapp.ui.navigation.HelperAppNavHost

@Composable
fun HelperApp() {
    HelperAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            HelperAppNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}