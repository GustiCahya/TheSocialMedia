package com.gusticahya.thesocialmedia.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// Mock data class for a tweet
data class Tweet(
    val id: Int,
    val author: String,
    val content: String,
    var likes: Int,
    var comments: Int,
    var shares: Int
)

// Example function to get a list of tweets
fun getMockTweets(): List<Tweet> {
    return listOf(
        Tweet(1, "User1", "This is a tweet", 10, 2, 5),
        Tweet(2, "User2", "Another tweet here", 15, 3, 6)
        // Add more tweets as needed
    )
}

@Composable
fun TimelineScreen(navController: NavHostController, innerPadding: PaddingValues) {
    val tweets = remember { mutableStateOf(getMockTweets()) }

    Column(modifier = Modifier.padding(innerPadding)) {
        tweets.value.forEach { tweet ->
            TweetCard(tweet)
        }
    }
}

@Composable
fun TweetCard(tweet: Tweet) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = tweet.author, style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = tweet.content)
            Spacer(modifier = Modifier.height(8.dp))
            TweetActions(tweet)
        }
    }
}

@Composable
fun TweetActions(tweet: Tweet) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Like button
        ActionButton(
            icon = Icons.Default.Favorite,
            text = "${tweet.likes}",
            onClick = { tweet.likes++ },
            description = "Like"
        )

        // Comment button
        ActionButton(
            icon = Icons.Default.Send,
            text = "${tweet.comments}",
            onClick = { tweet.comments++ },
            description = "Comment"
        )

        // Share button
        ActionButton(
            icon = Icons.Default.Share,
            text = "${tweet.shares}",
            onClick = { tweet.shares++ },
            description = "Share"
        )
    }
}

@Composable
fun ActionButton(icon: ImageVector, text: String, onClick: () -> Unit, description: String) {
    // Wrap the Row in a Box and apply the clickable to the Box
    Box(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = description)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text)
        }
    }
}


