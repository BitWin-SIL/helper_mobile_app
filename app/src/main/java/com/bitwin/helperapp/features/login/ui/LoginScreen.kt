package com.bitwin.helperapp.features.login.ui

import android.widget.Toast
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bitwin.helperapp.R
import com.bitwin.helperapp.core.routing.Screen
import com.bitwin.helperapp.core.theme.Primary
import com.bitwin.helperapp.core.theme.Typography
import com.bitwin.helperapp.core.theme.White
import com.bitwin.helperapp.features.login.data.LoginRequest
import com.bitwin.helperapp.features.login.logic.LoginViewModel

sealed class LoginScreenState {
    object Idle : LoginScreenState()
    object Loading : LoginScreenState()
    object Success : LoginScreenState()
    data class Error(val message: String) : LoginScreenState()
}

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val screenState = when {
        uiState.isLoading -> LoginScreenState.Loading
        uiState.isSuccess -> LoginScreenState.Success
        uiState.error != null -> LoginScreenState.Error(uiState.error!!)
        else -> LoginScreenState.Idle
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    fun validate(): Boolean {
        var valid = true
        if (email.isBlank()) {
            emailError = "L'adresse e-mail ne peut pas être vide"
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Adresse e-mail invalide"
            valid = false
        } else {
            emailError = ""
        }
        if (password.isBlank()) {
            passwordError = "Le mot de passe ne peut pas être vide"
            valid = false
        } else if (password.length < 6) {
            passwordError = "Le mot de passe doit contenir au moins 6 caractères"
            valid = false
        } else {
            passwordError = ""
        }
        return valid
    }

    val context = LocalContext.current
    LaunchedEffect(screenState) {
        when (screenState) {
            is LoginScreenState.Success -> {
                val toast = Toast.makeText(context, "Connexion réussie !", Toast.LENGTH_LONG)
                toast.view?.setBackgroundColor(AndroidColor.parseColor("#4CAF50"))
                toast.show()
                navController.navigate(Screen.Home.route)
            }
            is LoginScreenState.Error -> {
                val toast = Toast.makeText(context, screenState.message, Toast.LENGTH_LONG)
                toast.view?.setBackgroundColor(AndroidColor.parseColor("#F44336"))
                toast.show()
            }
            else -> { }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.White, Color(0xFFF5F5F5))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(70.dp))
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo de l'application",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Se connecter",
                style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Primary
            )
            Text(
                text = "Entrez vos identifiants pour continuer",
                style = Typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                isError = emailError.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Icône E-mail",
                        tint = if (emailError.isBlank()) Primary else MaterialTheme.colorScheme.error
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                supportingText = {
                    if (emailError.isNotBlank()) {
                        Text(text = emailError, color = MaterialTheme.colorScheme.error)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Primary,
                    cursorColor = Primary
                )
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                isError = passwordError.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Icône Cadenas",
                        tint = if (passwordError.isBlank()) Primary else MaterialTheme.colorScheme.error
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password),
                supportingText = {
                    if (passwordError.isNotBlank()) {
                        Text(text = passwordError, color = MaterialTheme.colorScheme.error)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Primary,
                    cursorColor = Primary
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (validate()) {
                        val request = LoginRequest(email = email, password = password)
                        viewModel.loginUser(request, context as? android.app.Activity)
                    }
                },
                enabled = screenState !is LoginScreenState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                if (screenState is LoginScreenState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "SE CONNECTER",
                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = White)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Pas de compte ? Inscrivez-vous",
                modifier = Modifier.clickable { navController.navigate(Screen.Register.route) }
            )
        }
    }
}
