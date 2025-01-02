package com.luna.studytimer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.luna.studytimer.ui.theme.StudytimerTheme
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudytimerTheme {
                AppContent()
            }
        }
    }
}

enum class Tab {
    Pomodoro, TodoList
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppContent() {
    var currentTab by remember { mutableStateOf(Tab.Pomodoro) }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(currentTab) { tab ->
                currentTab = tab
            }
        }
    ) {
        when (currentTab) {
            Tab.Pomodoro -> PomodoroScreen()
            Tab.TodoList -> TodoListScreen()
        }
    }
}

@Composable
fun TodoListScreen() {
    TODO("Not yet implemented")
}

@Composable
fun PomodoroScreen() {
    var timeLeft by remember { mutableStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            if (timeLeft > 0) {
                timeLeft--
            } else {
                isRunning = false
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("${timeLeft/60}:${(timeLeft % 60).toString().padStart(2, '0')}", style = typography.displayLarge)
        Row {
            Button(onClick = { isRunning = !isRunning }) {
                Text(if (isRunning) "Pause" else "Start")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { timeLeft = 25*60; isRunning = false }) {
                Text("Reset")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(currentTab: Tab, onTabSelected: (Tab) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = currentTab == Tab.Pomodoro,
            onClick = { onTabSelected(Tab.Pomodoro) },
            label = { Text("Pomodoro") },
            icon = { Icon(Icons.Outlined.Add, contentDescription = "Pomodoro Timer")}
        )
        NavigationBarItem(
            selected = currentTab == Tab.Pomodoro,
            onClick = { onTabSelected(Tab.TodoList) },
            label = { Text("Todo List") },
            icon = { Icon(Icons.Default.List, contentDescription = "Todo List") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    StudytimerTheme {
        AppContent()
    }
}