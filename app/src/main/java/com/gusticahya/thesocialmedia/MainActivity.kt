package com.gusticahya.thesocialmedia

import RegisterScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gusticahya.thesocialmedia.ui.theme.TheSocialMediaTheme
import com.gusticahya.thesocialmedia.screens.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gusticahya.thesocialmedia.database.SQLiteHelper

class MainActivity : ComponentActivity() {

    private lateinit var dbHelper: SQLiteHelper

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TheSocialMediaTheme {

                val navController = rememberNavController()
                val dbHelper = SQLiteHelper(this) // Initialize SQLiteHelper

                // Pass dbHelper to Screen
                RegisterScreen(navController = navController, dbHelper = dbHelper)
                LoginScreen(navController = navController, dbHelper = dbHelper )

                val showBottomBar = remember { mutableStateOf(true) }
                val currentRoute = remember { mutableStateOf(navController.currentDestination?.route) }

                // Listen to navigation changes
                LaunchedEffect(navController) {
                    navController.addOnDestinationChangedListener { _, destination, _ ->
                        showBottomBar.value = destination.route !in listOf("login", "register")
                        currentRoute.value = destination.route
                    }
                }

                Scaffold(
                    topBar = {
                        if (showBottomBar.value) AppBar(currentRoute.value, navController)
                    },
                    bottomBar = {
                        if (showBottomBar.value) BottomNavigationBar(navController)
                    }
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController, dbHelper) }
                        composable("register") { RegisterScreen(navController, dbHelper) }
                        composable("home") { HomeScreen(navController, innerPadding) }
                        composable("directMessage/{userName}/{type}") { backStackEntry ->
                            DirectMessageScreen(innerPadding, userName = backStackEntry.arguments?.getString("userName") ?: "Unknown", isGroup = backStackEntry.arguments?.getString("type") == "Group")
                        }
                        composable("timeline") { TimelineScreen(navController, innerPadding) }  // Add this line
                        composable("post") { PostScreen(innerPadding) }
                        composable("profile") { ProfileScreen(innerPadding) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(route: String?, navController: NavHostController) {

    val title = when {
        route?.startsWith("directMessage") == true -> {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val userName = backStackEntry?.arguments?.getString("userName")
            "$userName"
        }
        else -> route ?: ""
    }

    TopAppBar(
        title = { Text(title) },
        actions = {
            // Include a logout button if not on the login screen
            if (route != "login" && route != "register") {
                IconButton(onClick = { navController.navigate("login") }) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val currentRoute = navController.currentDestination?.route

        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = { navigateToScreen(navController, "home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Timeline") },
            label = { Text("Timeline") },
            selected = currentRoute == "timeline",
            onClick = { navigateToScreen(navController, "timeline") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Post") },
            label = { Text("Post") },
            selected = currentRoute == "post",
            onClick = { navigateToScreen(navController, "post") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == "profile",
            onClick = { navigateToScreen(navController, "profile") }
        )
    }
}


private fun navigateToScreen(navController: NavHostController, route: String) {
    navController.popBackStack(navController.graph.startDestinationId, inclusive = false)
    navController.navigate(route)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    val dbHelper = remember { SQLiteHelper(context) }

    CompositionLocalProvider(LocalContext provides context) {
        TheSocialMediaTheme {
            LoginScreen(navController = rememberNavController(), dbHelper = dbHelper)
        }
    }
}

