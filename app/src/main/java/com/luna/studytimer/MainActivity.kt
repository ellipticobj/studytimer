package com.luna.studytimer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.luna.studytimer.ui.theme.StudytimerTheme

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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppContent() {
    var currentTab by remember { mutableStateOf(Tab.Pomodoro) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when(currentTab) {
                            Tab.Pomodoro -> "Pomodoro"
                            Tab.TodoList -> "Todo List"
                        }
                    )
                }
            )
        },
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
fun BottomNavigationBar(currentTab: Tab, onTabSelected: (Tab) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = currentTab == Tab.Pomodoro,
            onClick = { onTabSelected(Tab.Pomodoro) },
            label = { Text("Pomodoro Timer") },
            icon = { Icon(Icons.Outlined.Add, contentDescription = "Pomodoro Timer")}
        )
        NavigationBarItem(
            selected = currentTab == Tab.TodoList,
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