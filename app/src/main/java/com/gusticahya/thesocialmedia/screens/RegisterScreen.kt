package com.gusticahya.thesocialmedia.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    // You can replicate the structure of LoginScreen and modify it for registration purposes
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    // Additional states for registration, e.g., email, confirm password, etc.

    Column(
        modifier = Modifier
            .padding(top = 65.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)  // Increased top padding
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("home") }) {
            Text("Register")
        }
        TextButton(onClick = { navController.navigate("login") }) {
            Text("I already have an account")
        }
    }
}
