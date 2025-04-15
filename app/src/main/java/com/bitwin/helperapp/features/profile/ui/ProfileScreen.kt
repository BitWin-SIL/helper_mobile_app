package com.bitwin.helperapp.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bitwin.helperapp.core.routing.Screen
import com.bitwin.helperapp.core.shared_components.AppBar
import com.bitwin.helperapp.core.theme.Gray
import com.bitwin.helperapp.core.theme.LightGray
import com.bitwin.helperapp.features.profile.logic.ProfileViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { AppBar(title = "Profil") },
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = paddingValues.calculateTopPadding()),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp)
                    )
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = uiState.error ?: "Erreur lors du chargement du profil",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                else -> {
                    // Success state - show profile content
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column {
                                        Text(
                                            text = "${uiState.userInfo?.firstName} ${uiState.userInfo?.lastName}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .padding(vertical = 8.dp)
                                ) {
                                    ProfileMenuItem(
                                        icon = Icons.Outlined.Person,
                                        title = "Mon compte",
                                        subtitle = "changer mes infos personnelles",
                                        showAlert = true,
                                        onClick = { 
                                            uiState.userInfo?.let { userInfo ->
                                                val firstName = URLEncoder.encode(userInfo.firstName ?: "", StandardCharsets.UTF_8.toString())
                                                val lastName = URLEncoder.encode(userInfo.lastName ?: "", StandardCharsets.UTF_8.toString())
                                                val email = URLEncoder.encode(userInfo.email ?: "", StandardCharsets.UTF_8.toString())
                                                
                                                navController.navigate("edit_personal_info/$firstName/$lastName/$email")
                                            } ?: run {
                                                navController.navigate("edit_personal_info")
                                            }
                                        }
                                    )

                                    Divider(
                                        modifier = Modifier.padding(start = 56.dp, end = 16.dp),
                                        thickness = 0.5.dp,
                                        color = LightGray
                                    )

                                    ProfileMenuItem(
                                        icon = Icons.Outlined.Lock,
                                        title = "Gérer les utilisateurs suivis",
                                        subtitle = null,
                                        showAlert = false,
                                        onClick = { navController.navigate(Screen.AssistanceRequests.route) }
                                    )

                                    Divider(
                                        modifier = Modifier.padding(start = 56.dp, end = 16.dp),
                                        thickness = 0.5.dp,
                                        color = LightGray
                                    )

                                    ProfileMenuItem(
                                        icon = Icons.Outlined.Settings,
                                        title = "Préférences",
                                        subtitle = "Configurer alertes et notifications",
                                        showAlert = false,
                                        onClick = { navController.navigate("preferences") }
                                    )

                                    Divider(
                                        modifier = Modifier.padding(start = 56.dp, end = 16.dp),
                                        thickness = 0.5.dp,
                                        color = LightGray
                                    )

                                    ProfileMenuItem(
                                        icon = Icons.Outlined.ExitToApp,
                                        title = "Déconnexion",
                                        subtitle = "Se déconnecter",
                                        showAlert = false,
                                        onClick = {
                                            viewModel.logout()
                                            navController.navigate("login") {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Plus",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .padding(vertical = 8.dp)
                                ) {
                                    ProfileMenuItem(
                                        icon = Icons.Outlined.Info,
                                        title = "Aide & Support",
                                        subtitle = null,
                                        showAlert = false,
                                        onClick = { navController.navigate("help_support") }
                                    )

                                    Divider(
                                        modifier = Modifier.padding(start = 56.dp, end = 16.dp),
                                        thickness = 0.5.dp,
                                        color = LightGray
                                    )

                                    ProfileMenuItem(
                                        icon = Icons.Outlined.Info,
                                        title = "À propos de l'application",
                                        subtitle = null,
                                        showAlert = false,
                                        onClick = { navController.navigate("about") }
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier
                                    .height(16.dp)
                                    .padding(bottom = paddingValues.calculateBottomPadding())
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    showAlert: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Gray
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray
                )
            }
        }

        if (showAlert) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
            )

            Spacer(modifier = Modifier.width(12.dp))
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Gray
        )
    }
}