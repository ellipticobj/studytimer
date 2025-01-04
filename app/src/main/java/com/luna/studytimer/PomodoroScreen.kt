package com.luna.studytimer

// archived

import android.provider.Settings
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun PomodoroScreen() {
    var studyTime by remember { mutableIntStateOf(25 * 60) }
    var breakTime by remember { mutableIntStateOf(5 * 60) }
    var timeLeft by remember { mutableIntStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }
    var isStudySession by remember { mutableStateOf(true) }
    var sessionCount by remember { mutableIntStateOf(0) }
    var showSettings by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            if (timeLeft > 0) {
                timeLeft--
            } else {
                if (isStudySession) {
                    isStudySession = false
                    timeLeft = breakTime
                } else {
                    isStudySession = true
                    timeLeft = studyTime
                    sessionCount++
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showSettings = true }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = if (isStudySession) "Study time" else "Break time",
                style = typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { timeLeft.toFloat() / if (isStudySession) studyTime else breakTime },
                    modifier = Modifier
                        .size(290.dp)
                        .offset(y = (-5).dp)
                    ,
                    color = if (isStudySession) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )

                Text(
                    "${timeLeft / 60}:${(timeLeft % 60).toString().padStart(2, '0')}",
                    style = typography.displayLarge
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { isRunning = !isRunning }) {
                    Text(if (isRunning) "Pause" else "Start")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (isStudySession) {
                            timeLeft = studyTime
                        } else {
                            timeLeft = breakTime
                        }
                        isRunning = false
                    }
                ) {
                    Text("Reset")
                }
            }

            if (showSettings) {
                SettingsBottomSheet(
                    studyTime = studyTime,
                    breakTime = breakTime,
                    onStudyTimeChange = {
                        studyTime = it
                        if (isStudySession) timeLeft = studyTime - timeLeft
                    },
                    onBreakTimeChange =  {
                        breakTime = it
                        if (!isStudySession) timeLeft = breakTime - timeLeft
                    },
                    onClose = { showSettings = false }
                )
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    studyTime: Int,
    breakTime: Int,
    onStudyTimeChange: (Int) -> Unit,
    onBreakTimeChange: (Int) -> Unit,
    onClose: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Settings", style = typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Study time (mins)", fontSize = 14.sp)
                TextField(
                    value = (studyTime / 60).toString(),
                    onValueChange = {
                        val newValue = it.toIntOrNull() ?: 25
                        onStudyTimeChange(newValue*60)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Break time (mins)", fontSize = 14.sp)
                TextField(
                    value = (breakTime / 60).toString(),
                    onValueChange = {
                        val newValue = it.toIntOrNull() ?: 5
                        onBreakTimeChange(newValue * 60)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
        ) {
            Text("Save settings")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PomodoroScreenPreview() {
    PomodoroScreen()
}
