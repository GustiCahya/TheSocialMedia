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
import com.gusticahya.thesocialmedia.database.SQLiteHelper

// Mock data class for a tweet
data class Tweet(
    val id: Int,
    val author: String,
    val content: String,
    var likes: Int = 0,
    var comments: Int = 0,
    var shares: Int = 0
)

// Example function to get a list of tweets
fun getMockTweets(): List<Tweet> {
    return listOf(
        Tweet(1, "User1", "This is a tweet", 10, 2, 5),
        Tweet(2, "User2", "Another tweet here", 15, 3, 6)
        // Add more tweets as needed
    )
}

// Function to fetch posts from the database
fun getPostsFromDatabase(dbHelper: SQLiteHelper): List<SQLiteHelper.Post> {
    val postsFromDb = dbHelper.getAllPosts()
    val posts = mutableListOf<SQLiteHelper.Post>()

    // Fetch username for each post's author
    for (post in postsFromDb) {
        val user = dbHelper.getUserInfo(post.userId)
        val authorName = user?.username
        if (authorName != null) {
            val postItem = SQLiteHelper.Post(post.id, post.userId, post.content, authorName)
            posts.add(postItem)
        }
    }

    println(posts)
    return posts
}


@Composable
fun TimelineScreen(navController: NavHostController, innerPadding: PaddingValues, dbHelper: SQLiteHelper) {
    val posts = remember { mutableStateOf(getPostsFromDatabase(dbHelper)) }

    Column(modifier = Modifier.padding(innerPadding)) {
        posts.value.forEach { tweet ->
            TweetCard(tweet)
        }
    }
}

@Composable
fun TweetCard(post: SQLiteHelper.Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.username, style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = post.content)
            Spacer(modifier = Modifier.height(8.dp))
            TweetActions(post)
        }
    }
}

@Composable
fun TweetActions(post: SQLiteHelper.Post) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Like button
        ActionButton(
            icon = Icons.Default.Favorite,
            text = "",
            onClick = {},
            description = "Like"
        )

        // Comment button
        ActionButton(
            icon = Icons.Default.Send,
            text = "",
            onClick = {},
            description = "Comment"
        )

        // Share button
        ActionButton(
            icon = Icons.Default.Share,
            text = "",
            onClick = {},
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


