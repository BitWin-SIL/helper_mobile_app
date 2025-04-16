package com.bitwin.helperapp.features.assistance.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bitwin.helperapp.core.shared_components.AppBar
import com.bitwin.helperapp.core.theme.*
import com.bitwin.helperapp.features.association_requests.ui.AssistanceRequest
import com.bitwin.helperapp.features.association_requests.ui.AssistanceRequestStatus
import com.bitwin.helperapp.features.association_requests.logic.AssistanceRequestsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistanceScreen(
    navController: NavController,
    viewModel: AssistanceRequestsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val acceptedRequests = uiState.requests.filter { it.status == AssistanceRequestStatus.Accepted }
    
    val statusBarPadding = androidx.compose.foundation.layout.WindowInsets.statusBars
        .only(androidx.compose.foundation.layout.WindowInsetsSides.Top)
        .asPaddingValues()

    val showAddRequestSheet = remember { mutableStateOf(false) }
    val requestEmail = remember { mutableStateOf("") }

    LaunchedEffect(uiState.sentRequestSuccess) {
        if (uiState.sentRequestSuccess) {
            showAddRequestSheet.value = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(statusBarPadding.calculateTopPadding()))
            
            AppBar(
                title = "Assistance Vidéo",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
            
            when {
                uiState.isLoading -> {
                    LoadingView()
                }
                uiState.error != null -> {
                    ErrorView(
                        message = uiState.error ?: "Une erreur est survenue",
                        onRetry = { viewModel.fetchAssistanceRequests() }
                    )
                }
                acceptedRequests.isEmpty() -> {
                    EmptyAssistanceView(navController)
                }
                else -> {
                    AssistanceContactsList(
                        acceptedRequests = acceptedRequests,
                        onCallRequest = { /* Start video call with this contact */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { showAddRequestSheet.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Ajouter demande")
            }
        }
    }

    if (showAddRequestSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showAddRequestSheet.value = false }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nouvelle demande d'assistance", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = requestEmail.value,
                    onValueChange = { requestEmail.value = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.error != null) {
                    Text(text = uiState.error!!, color = Color.Red)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Button(
                    onClick = { viewModel.addRequest(requestEmail.value) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Envoyer")
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Primary)
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Impossible de charger vos contacts",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF6B6E7B),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("Réessayer")
        }
    }
}

@Composable
fun EmptyAssistanceView(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = Primary.copy(alpha = 0.1f),
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(56.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Aucun contact disponible",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Vous n'avez aucune demande d'association acceptée. Envoyez des invitations pour pouvoir utiliser l'assistance vidéo.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF6B6E7B),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { navController.navigate("assistance_requests") },
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Gérer mes contacts")
        }
    }
}

@Composable
fun AssistanceContactsList(
    acceptedRequests: List<AssistanceRequest>,
    onCallRequest: (AssistanceRequest) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mes contacts d'assistance",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        acceptedRequests.forEach { request ->
            AssistanceContactItem(
                request = request,
                onCallRequest = onCallRequest
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF2C2C2C)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Call,
                    contentDescription = "Video",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pas d'appel en cours",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AssistanceContactItem(
    request: AssistanceRequest,
    onCallRequest: (AssistanceRequest) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Primary.copy(alpha = 0.1f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = request.email.first().uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = request.email,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = "Disponible pour assistance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4CAF50)
                )
            }
            
            IconButton(
                onClick = { onCallRequest(request) },
                modifier = Modifier
                    .size(48.dp)
                    .background(Primary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "Appeler",
                    tint = Color.White
                )
            }
        }
    }
}
