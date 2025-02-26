package com.example.dividepie

import android.net.http.HttpResponseCache.install
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


val supabase = createSupabaseClient(
    supabaseUrl = "https://pvvktwohnmssuiftxkzf.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB2dmt0d29obm1zc3VpZnR4a3pmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDA1MjM2MzQsImV4cCI6MjA1NjA5OTYzNH0.d-2sCoHjTQgeHYiwd17Um017yH2hVyaBTfdQuumXaV0"
) {
    install(Postgrest)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserList()
                }
            }
        }
    }
}

@Serializable
data class User(
    @SerialName("id") val id: String,  // Fixed incorrect 'integer' type
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("phone") val phone: String,
    @SerialName("created_at") val created_at: String
)


@Composable
fun UserList() {
    val users = remember { mutableStateListOf<User>() }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                println("Fetching users from Supabase...")  // 🟢 Debugging step 1
                val results = supabase.from("users").select().decodeList<User>()
                println("Fetched users: $results")  // 🟢 Debugging step 2
                users.addAll(results)
            } catch (e: Exception) {
                println("Error fetching users: ${e.message}")  // 🔴 Catch and log errors
            }
        }
    }

    LazyColumn {
        items(users) { user ->
            println("Rendering user: ${user.name}")  // 🟢 Debugging step 3
            ListItem(headlineContent = { Text(text = user.name) })
        }
    }
}

