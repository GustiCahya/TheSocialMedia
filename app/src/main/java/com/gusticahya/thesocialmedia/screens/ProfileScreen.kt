package com.gusticahya.thesocialmedia.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gusticahya.thesocialmedia.database.SQLiteHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, innerPadding: PaddingValues, dbHelper: SQLiteHelper) {
    // State for profile inputs
    val nameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val bioState = remember { mutableStateOf("") }

    val userId = getUserIdFromCache() // Retrieve user ID from cache
    val userInfo = remember { mutableStateOf<SQLiteHelper.User?>(null) }

    val alertVisibleState = remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        if (userId != null) {
            val user = dbHelper.getUserInfo(userId)
            userInfo.value = user
            // Set initial values for profile inputs if user information is not null
            if (user != null) {
                nameState.value = user.name
                emailState.value = user.email
                bioState.value = user.bio
            }
        }
    }

    Box(modifier = Modifier.padding(innerPadding)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = bioState.value,
                onValueChange = { bioState.value = it },
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val userId = getUserIdFromCache() // Retrieve user ID from cache
                    if (userId != null) {
                        val success = dbHelper.updateProfile(userId, nameState.value, emailState.value, bioState.value)
                        if (success) {
                            alertVisibleState.value = true
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Submit")
            }
        }

        if (alertVisibleState.value) {
            AlertDialog(
                onDismissRequest = { alertVisibleState.value = false },
                title = { Text("Success") },
                text = { Text("Profile updated successfully!") },
                confirmButton = {
                    Button(
                        onClick = {
                            alertVisibleState.value = false
                            navController.navigate("profile")
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

    }
}

fun getUserIdFromCache(): Int? {
    return SQLiteHelper.UserCache.getUserId()
}
