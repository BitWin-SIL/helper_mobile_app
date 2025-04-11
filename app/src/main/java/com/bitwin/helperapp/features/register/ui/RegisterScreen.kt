package com.bitwin.helperapp.features.register.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.bitwin.helperapp.R
import com.bitwin.helperapp.core.theme.Primary
import com.bitwin.helperapp.core.theme.Typography
import com.bitwin.helperapp.features.register.data.RegisterRequest
import com.bitwin.helperapp.features.register.logic.RegisterViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.bitwin.helperapp.core.routing.Screen
import com.bitwin.helperapp.core.theme.White
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.withStyle
import android.graphics.Color as AndroidColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController, viewModel: RegisterViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val screenState = when {
        uiState.isLoading -> RegisterScreenState.Loading
        uiState.isSuccess -> RegisterScreenState.Success
        uiState.error != null -> RegisterScreenState.Error(uiState.error!!)
        else -> RegisterScreenState.Idle
    }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    fun validate(): Boolean {
        var valid = true

        // Enhanced validators for firstName
        if (firstName.isBlank()) {
            firstNameError = "Le prénom ne peut pas être vide"
            valid = false
        } else if (firstName.length < 2) {
            firstNameError = "Le prénom doit contenir au moins 2 caractères"
            valid = false
        } else if (!firstName.matches(Regex("^[a-zA-ZÀ-ÿ\\-\\s]+$"))) {
            firstNameError = "Le prénom ne doit contenir que des lettres"
            valid = false
        } else {
            firstNameError = ""
        }

        // Enhanced validators for lastName
        if (lastName.isBlank()) {
            lastNameError = "Le nom ne peut pas être vide"
            valid = false
        } else if (lastName.length < 2) {
            lastNameError = "Le nom doit contenir au moins 2 caractères"
            valid = false
        } else if (!lastName.matches(Regex("^[a-zA-ZÀ-ÿ\\-\\s]+$"))) {
            lastNameError = "Le nom ne doit contenir que des lettres"
            valid = false
        } else {
            lastNameError = ""
        }

        // Enhanced validators for email
        if (email.isBlank()) {
            emailError = "L'adresse e-mail ne peut pas être vide"
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Adresse e-mail invalide"
            valid = false
        } else {
            emailError = ""
        }

        // Enhanced validators for password
        if (password.isBlank()) {
            passwordError = "Le mot de passe ne peut pas être vide"
            valid = false
        } else if (password.length < 6) {
            passwordError = "Le mot de passe doit contenir au moins 6 caractères"
            valid = false
        } else if (!password.matches(Regex(".*[A-Z].*"))) {
            passwordError = "Le mot de passe doit contenir au moins une lettre majuscule"
            valid = false
        } else if (!password.matches(Regex(".*[0-9].*"))) {
            passwordError = "Le mot de passe doit contenir au moins un chiffre"
            valid = false
        } else {
            passwordError = ""
        }

        return valid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFF5F5F5)
                    )
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
                text = "Créer un Compte",
                style = Typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary
            )

            Text(
                text = "Remplissez vos coordonnées pour commencer",
                style = Typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current

            LaunchedEffect(screenState) {
                when (screenState) {
                    is RegisterScreenState.Success -> {
                        val toast = Toast.makeText(context, "Inscription réussie ! Redirection...", Toast.LENGTH_LONG)
                        toast.view?.setBackgroundColor(AndroidColor.parseColor("#4CAF50"))
                        toast.show()
                        navController.navigate(Screen.Home.route)
                    }
                    is RegisterScreenState.Error -> {
                        val toast = Toast.makeText(context, screenState.message, Toast.LENGTH_LONG)
                        toast.view?.setBackgroundColor(AndroidColor.parseColor("#F44336"))
                        toast.show()
                    }
                    else -> { }
                }
            }

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Prénom") },
                isError = firstNameError.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icône Personne",
                        tint = if (firstNameError.isBlank()) Primary else MaterialTheme.colorScheme.error
                    )
                },
                supportingText = {
                    if (firstNameError.isNotBlank()) {
                        Text(
                            text = firstNameError,
                            color = MaterialTheme.colorScheme.error
                        )
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
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nom") },
                isError = lastNameError.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icône Personne",
                        tint = if (lastNameError.isBlank()) Primary else MaterialTheme.colorScheme.error
                    )
                },
                supportingText = {
                    if (lastNameError.isNotBlank()) {
                        Text(
                            text = lastNameError,
                            color = MaterialTheme.colorScheme.error
                        )
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
                supportingText = {
                    if (emailError.isNotBlank()) {
                        Text(
                            text = emailError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                supportingText = {
                    if (passwordError.isNotBlank()) {
                        Text(
                            text = passwordError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
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
                        val request = RegisterRequest(
                            email = email,
                            firstName = firstName,
                            lastName = lastName,
                            password = password
                        )
                        viewModel.registerUser(request)
                    }
                },
                enabled = screenState != RegisterScreenState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                )
            ) {
                if (screenState is RegisterScreenState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "CRÉER UN COMPTE",
                        style = Typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    append("Vous avez déjà un compte ? ")
                    withStyle(
                        style = SpanStyle(
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Connectez-vous")
                    }
                },
                modifier = Modifier
                    .clickable { navController.navigate(Screen.Login.route) }
                    .padding(8.dp),
                style = Typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

sealed class RegisterScreenState {
    data object Idle : RegisterScreenState()
    data object Loading : RegisterScreenState()
    data object Success : RegisterScreenState()
    data class Error(val message: String) : RegisterScreenState()
}