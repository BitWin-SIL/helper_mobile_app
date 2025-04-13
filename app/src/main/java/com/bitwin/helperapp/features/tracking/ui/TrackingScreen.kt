package com.bitwin.helperapp.features.tracking.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bitwin.helperapp.core.shared_components.AppBar
import com.bitwin.helperapp.features.tracking.data.TrackingRequest
import com.bitwin.helperapp.features.tracking.logic.TrackingViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun TrackingScreen(
    modifier: Modifier = Modifier,
    onNotificationsClick: () -> Unit = {}
) {
    val viewModel: TrackingViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.trackPosition(TrackingRequest(latitude = 36.752887, longitude = 3.042048))
    }

    Scaffold(
        topBar = {
            AppBar(
                title = "Localisation et Suivi",
                trailingIcon = Icons.Default.Notifications,
                backgroundColor = Color(0xFFFFFFFF),
                onTrailingIconClick = onNotificationsClick,
                showDot = false
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight() 
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.response != null -> {
                    val trackingResponse = uiState.response!!
                    val latLng = LatLng(trackingResponse.currentLatitude, trackingResponse.currentLongitude)
                    val cameraPosition = CameraPosition.fromLatLngZoom(latLng, 15f)
                    val cameraPositionState = rememberCameraPositionState { position = cameraPosition }
                    
                    Box(modifier = Modifier.fillMaxSize()) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            uiSettings = MapUiSettings(
                                zoomControlsEnabled = true,
                                zoomGesturesEnabled = true,
                                mapToolbarEnabled = false,
                                compassEnabled = true,
                                myLocationButtonEnabled = true
                            )
                        ) {
                            Marker(
                                state = MarkerState(latLng),
                                title = "Current Location"
                            )
                        }
                        
                        Card(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(start = 16.dp, end = 72.dp, bottom = 16.dp)
                                .width(300.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        ) {
                            Text(
                                text = "Suivez en direct la position de [Nom de l'utilisateur] et intervenez si nécessaire.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                uiState.error != null -> {
                    ErrorView(
                        errorMessage = getFormattedErrorMessage(uiState.error!!),
                        onRetry = {
                            viewModel.trackPosition(TrackingRequest(latitude = 36.752887, longitude = 3.042048))
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    Text("Aucune donnée disponible", 
                         modifier = Modifier
                             .align(Alignment.Center)
                             .padding(16.dp),
                         style = MaterialTheme.typography.bodyLarge,
                         textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorView(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        
        Text(
            text = "Erreur de suivi",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Réessayer")
        }
    }
}

private fun getFormattedErrorMessage(error: String): String {
    return when {
        error.contains("timeout", ignoreCase = true) -> 
            "La connexion au serveur a expiré. Veuillez vérifier votre connexion internet et réessayer."
        error.contains("network", ignoreCase = true) -> 
            "Problème de connexion réseau. Veuillez vérifier votre connexion internet et réessayer."
        error.contains("permission", ignoreCase = true) -> 
            "Accès à la localisation refusé. Veuillez activer les permissions de localisation dans les paramètres."
        error.contains("unauthorized", ignoreCase = true) || error.contains("401", ignoreCase = true) -> 
            "Vous n'êtes pas autorisé à accéder à cette fonctionnalité. Veuillez vous reconnecter."
        else -> 
            "Une erreur inattendue s'est produite. Veuillez réessayer ultérieurement ou contacter le support technique."
    }
}
