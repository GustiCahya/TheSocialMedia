package com.gusticahya.thesocialmedia.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectMessageScreen(innerPadding: PaddingValues, userName: String, isGroup: Boolean) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(
        Pair("Alice", "Hello"),
        Pair("Bob", "How are you?")
    )) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(messages) { (sender, message) ->
                MessageBubble(
                    message = message,
                    isGroup = isGroup,
                    senderName = sender
                )
            }
        }

        MessageInputField(
            messageText = messageText,
            onMessageChanged = { messageText = it },
            onSendClicked = {
                if (messageText.isNotBlank()) {
                    // Here, replace "You" with the actual sender's name in a real scenario
                    messages = messages + Pair("You", messageText)
                    messageText = ""
                }
            }
        )
    }
}

@Composable
fun MessageBubble(message: String, isGroup: Boolean, senderName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        if (isGroup) {
            Text(
                text = senderName,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
            )
        }

        Card(
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.Start),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInputField(
    messageText: String,
    onMessageChanged: (String) -> Unit,
    onSendClicked: () -> Unit
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageChanged,
            label = { Text("Type a message") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onSendClicked,
            enabled = messageText.isNotBlank()
        ) {
            Text("Send")
        }
    }
}
