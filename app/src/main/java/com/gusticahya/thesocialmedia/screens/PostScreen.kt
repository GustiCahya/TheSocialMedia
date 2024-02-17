package com.gusticahya.thesocialmedia.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gusticahya.thesocialmedia.database.SQLiteHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(innerPadding: PaddingValues, dbHelper: SQLiteHelper) {
    // State for message input
    val message = remember { mutableStateOf("") }
    val alertVisibleState = remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(innerPadding)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = message.value,
                onValueChange = { message.value = it },
                label = { Text("Message") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Call the addPost function when the "Send" button is clicked
                    val userId =  SQLiteHelper.UserCache.getUserId()
                    val content = message.value
                    if (content.isNotBlank()) {
                        val success = userId?.let { dbHelper.addPost(it, content) }
                        if (success == true) {
                            alertVisibleState.value = true
                            message.value = ""
                        } else {
                            alertVisibleState.value = true
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Send")
            }
        }

        if (alertVisibleState.value) {
            AlertDialog(
                onDismissRequest = { alertVisibleState.value = false },
                title = { Text("Success") },
                text = { Text("Post sent") },
                confirmButton = {
                    Button(
                        onClick = {
                            alertVisibleState.value = false
                            message.value = ""
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}