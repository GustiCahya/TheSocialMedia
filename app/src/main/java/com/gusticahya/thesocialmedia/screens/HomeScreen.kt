package com.gusticahya.thesocialmedia.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

data class ListItem(val id: Int, val title: String, val type: String) // "Friend" or "Group"

@Composable
fun HomeScreen(navController: NavHostController, innerPadding: PaddingValues) {
    val listItems = getSampleListItems()

    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(listItems) { item ->
            ListItemView(item) { clickedItem ->
                navController.navigate("directMessage/${clickedItem.title}/${clickedItem.type}")
            }
        }
    }
}

@Composable
fun ListItemView(item: ListItem, onClick: (ListItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(item) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = item.title, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Type: ${item.type}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun getSampleListItems(): List<ListItem> {
    return listOf(
        ListItem(1, "Alice", "Friend"),
        ListItem(2, "Bob", "Friend"),
        ListItem(3, "Charlie", "Friend"),
        ListItem(4, "Delta Group", "Group"),
        ListItem(5, "Echo Group", "Group")
        // Add more items as needed
    )
}
