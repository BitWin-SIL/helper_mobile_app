package com.bitwin.helperapp.features.association_requests.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bitwin.helperapp.core.shared_components.AppBar
import com.bitwin.helperapp.core.theme.*
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

enum class AssistanceRequestStatus { Pending, Accepted, Rejected }

data class AssistanceRequest(
    val id: String,
    val email: String,
    val date: Date,
    val status: AssistanceRequestStatus
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistanceRequestsScreen(
    navController: NavHostController,
    viewModel: AssistanceRequestsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedRequest by remember { mutableStateOf<AssistanceRequest?>(null) }
    val scope = rememberCoroutineScope()

    val statusBarPadding = WindowInsets.statusBars
        .only(WindowInsetsSides.Top)
        .asPaddingValues()

    val navigationBarPadding = WindowInsets.navigationBars
        .only(WindowInsetsSides.Bottom)
        .asPaddingValues()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showAddSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(statusBarPadding.calculateTopPadding()))

            AppBar(
                title = "Mes demandes d'association",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )

            // Sample data for testing
            val sampleRequests = remember {
                listOf(
                    AssistanceRequest(
                        id = "1",
                        email = "user1@example.com",
                        date = Date(),
                        status = AssistanceRequestStatus.Pending
                    ),
                    AssistanceRequest(
                        id = "2",
                        email = "accepted@example.com",
                        date = Date(System.currentTimeMillis() - 86400000), // yesterday
                        status = AssistanceRequestStatus.Accepted
                    ),
                    AssistanceRequest(
                        id = "3",
                        email = "rejected@example.com",
                        date = Date(System.currentTimeMillis() - 172800000), // 2 days ago
                        status = AssistanceRequestStatus.Rejected
                    ),
                    AssistanceRequest(
                        id = "4",
                        email = "longEmail12345@verylongdomainname.example.com",
                        date = Date(System.currentTimeMillis() - 259200000), // 3 days ago
                        status = AssistanceRequestStatus.Pending
                    )
                )
            }
            
            // Use sample data instead of actual data from API
            val testRequests = sampleRequests
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = 8.dp),
            ) {
                // Replace requests with the sample data
                if (testRequests.isEmpty()) {
                    EmptyStateView()
                } else {
                    RequestsList(
                        // Use sample data instead of actual data
                        requests = testRequests,
                        onRequestClick = { selectedRequest = it }
                    )
                }
            }
        }

        AnimatedFab(
            onClick = { showAddSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 24.dp,
                    bottom = 24.dp + navigationBarPadding.calculateBottomPadding()
                )
        )

        if (showAddSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                tonalElevation = 12.dp,
                dragHandle = null,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                AddAssociationRequestSheet(
                    onRequestSent = {
                        scope.launch {
                            sheetState.hide()
                            showAddSheet = false
                        }
                    },
                    onDismiss = {
                        scope.launch {
                            sheetState.hide()
                            showAddSheet = false
                        }
                    },
                    viewModel = viewModel,
                    isLoading = uiState.isLoading,
                    error = uiState.error
                )
            }
        }

        if (selectedRequest != null) {
            RequestDetailsDialog(
                request = selectedRequest!!,
                onDismiss = { selectedRequest = null }
            )
        }
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Une erreur est survenue",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2A2C35),
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6B6E7B),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("Réessayer")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssociationRequestSheet(
    onRequestSent: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: AssistanceRequestsViewModel,
    isLoading: Boolean,
    error: String?
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    val isEmailValid = remember(email.text) {
        android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nouvelle demande",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = Color(0xFF2A2C35)
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF0F2F8))
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Fermer",
                    tint = Color(0xFF6B6E7B)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Envoyez une demande d'association à un utilisateur malvoyant.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF6B6E7B),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        error?.let {
            if (it.isNotEmpty()) {
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            tint = Color(0xFFF44336),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Email de l'utilisateur") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Email,
                    contentDescription = null,
                    tint = Primary
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Primary,
                unfocusedBorderColor = Color(0xFFDFE2EB)
            )
        )

        AnimatedVisibility(visible = email.text.isNotEmpty() && !isEmailValid) {
            Text(
                text = "Veuillez saisir une adresse email valide",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFF44336),
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.addRequest(email.text.trim())
                onRequestSent()
            },
            enabled = isEmailValid && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                disabledContainerColor = Primary.copy(alpha = 0.5f)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    "Envoyer l'invitation",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = Primary.copy(alpha = 0.1f),
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Email,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(56.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Aucune demande d'association",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF2A2C35)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Vous n'avez pas encore envoyé de demande d'association à un utilisateur malvoyant.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF6B6E7B),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Appuyez sur le bouton + pour envoyer une nouvelle demande.",
            style = MaterialTheme.typography.bodyLarge,
            color = Primary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RequestsList(
    requests: List<AssistanceRequest>,
    onRequestClick: (AssistanceRequest) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(requests) { index, request ->
            RequestItem(
                request = request,
                onClick = { onRequestClick(request) },
                animationDelay = index * 50
            )
        }
    }
}

@Composable
fun AnimatedFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    FloatingActionButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(8.dp, CircleShape),
        containerColor = Primary,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Ajouter",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }

    DisposableEffect(isPressed) {
        onDispose {
            isPressed = false
        }
    }
}

@Composable
fun RequestItem(
    request: AssistanceRequest,
    onClick: () -> Unit,
    animationDelay: Int = 0
) {
    val density = LocalDensity.current
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        kotlinx.coroutines.delay(animationDelay.toLong())
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(300)) + slideInVertically(
            initialOffsetY = { with(density) { 50.dp.roundToPx() } },
            animationSpec = tween(300)
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.25.dp,
                pressedElevation = 1.dp
            ),
            border = BorderStroke(1.dp, Color(0xFFDFE2EB))

                    ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusIndicator(status = request.status)

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = request.email,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2A2C35),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF9EA1B0),
                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = dateFormat.format(request.date),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9EA1B0)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        StatusChip(status = request.status)
                    }
                }

                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = "Voir détails",
                    tint = Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(status: AssistanceRequestStatus) {
    val (color, icon) = when (status) {
        AssistanceRequestStatus.Pending -> Pair(Accent, Icons.Rounded.MailOutline)
        AssistanceRequestStatus.Accepted -> Pair(Color(0xFF4CAF50), Icons.Rounded.CheckCircle)
        AssistanceRequestStatus.Rejected -> Pair(Color(0xFFF44336), Icons.Rounded.Close)
    }

    Surface(
        shape = CircleShape,
        color = color.copy(alpha = 0.15f),
        modifier = Modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun StatusChip(status: AssistanceRequestStatus) {
    val (text, color) = when (status) {
        AssistanceRequestStatus.Pending -> Pair("En attente", Accent)
        AssistanceRequestStatus.Accepted -> Pair("Acceptée", Color(0xFF4CAF50))
        AssistanceRequestStatus.Rejected -> Pair("Rejetée", Color(0xFFF44336))
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f),
        modifier = Modifier.height(20.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = color
            )
        }
    }
}

@Composable
fun RequestDetailsDialog(
    request: AssistanceRequest,
    onDismiss: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text(
                    "Fermer",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }
        },
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val (statusColor, statusIcon, statusText) = when (request.status) {
                    AssistanceRequestStatus.Pending -> Triple(
                        Accent,
                        Icons.Rounded.MailOutline,
                        "Demande en attente"
                    )
                    AssistanceRequestStatus.Accepted -> Triple(
                        Color(0xFF4CAF50),
                        Icons.Rounded.CheckCircle,
                        "Demande acceptée"
                    )
                    AssistanceRequestStatus.Rejected -> Triple(
                        Color(0xFFF44336),
                        Icons.Rounded.Close,
                        "Demande rejetée"
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = statusColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Détails de la demande",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color(0xFF2A2C35)
                )

                Spacer(modifier = Modifier.height(16.dp))

                DetailItem(
                    label = "Email",
                    value = request.email,
                    icon = Icons.Rounded.Email
                )

                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0xFFEEEFF4),
                    thickness = 1.dp
                )

                DetailItem(
                    label = "Date d'envoi",
                    value = dateFormat.format(request.date),
                    icon = Icons.Rounded.DateRange
                )
            }
        }
    )
}

@Composable
fun DetailItem(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF6B6E7B)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF2A2C35)
            )
        }
    }
}