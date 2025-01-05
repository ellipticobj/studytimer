@file:OptIn(ExperimentalMaterial3Api::class)

package com.luna.studytimer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luna.studytimer.ui.theme.StudytimerTheme
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppContent() {
    var studyTime by remember { mutableIntStateOf(25 * 60) }
    var breakTime by remember { mutableIntStateOf(5 * 60) }
    var timeLeft by remember { mutableIntStateOf(25 * 60) }
    var totalTime by remember { mutableIntStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var isStudySession by remember { mutableStateOf(true) }
    var sessionCount by remember { mutableIntStateOf(1) }
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
                    totalTime = breakTime
                } else {
                    isStudySession = true
                    timeLeft = studyTime
                    totalTime = studyTime
                    sessionCount++
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(if (isStudySession) "Study time" else "Break time") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Session $sessionCount",
                style = typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { if (totalTime > 0) timeLeft.toFloat() / totalTime else 0f },
                    modifier = Modifier
                        .size(290.dp),
                    color = if (isStudySession) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    strokeWidth = 12.dp
                )

                Text(
                    "${timeLeft / 60}:${(timeLeft % 60).toString().padStart(2, '0')}",
                    style = typography.displayLarge
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text("Total time: ${totalTime / 60} minutes")
            Spacer(modifier = Modifier.height(30.dp))

            if (isRunning) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row {
                        Button(
                            onClick = {
                                isRunning = false
                                isPaused = true
                            },
                            modifier = Modifier
                                .fillMaxWidth(.65f)
                                .padding(start = 30.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 4.dp,
                                bottomStart = 4.dp,
                                bottomEnd = 4.dp
                            )
                        ) {
                            Text("Pause")
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Button(
                            onClick = {
                                sessionCount++
                                timeLeft = studyTime
                                totalTime = studyTime
                                isStudySession = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 30.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 16.dp,
                                bottomStart = 4.dp,
                                bottomEnd = 4.dp
                            )
                        ) {
                            // Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                            Text("Next session")
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                isRunning = false

                                if (isStudySession) {
                                    timeLeft = studyTime
                                    totalTime = studyTime
                                } else {
                                    timeLeft = breakTime
                                    totalTime = breakTime
                                }

                                isPaused = false
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .padding(start = 30.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 4.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 4.dp
                            ),
                        ) {
                            Text("Reset")
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Button(
                            onClick = {
                                timeLeft += 60
                                totalTime += 60
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 30.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 4.dp,
                                bottomStart = 4.dp,
                                bottomEnd = 16.dp
                            )
                        ) {
                            Text("+1 min")
                        }
                    }
                }
            } else if (isPaused) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row {
                        Button(
                            onClick = {
                                isRunning = true
                                isPaused = false
                            },
                            modifier = Modifier
                                .fillMaxWidth(.65f)
                                .padding(start = 30.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 4.dp,
                                bottomStart = 4.dp,
                                bottomEnd = 4.dp
                            )
                        ) {
                            Text("Resume")
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Button(
                            onClick = {
                                sessionCount++
                                timeLeft = studyTime
                                totalTime = studyTime
                                isStudySession = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 30.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 16.dp,
                                bottomStart = 4.dp,
                                bottomEnd = 4.dp
                            )
                        ) {
                            // Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                            Text("Next session")
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            isRunning = false

                            if (isStudySession) {
                                timeLeft = studyTime
                                totalTime = studyTime
                            } else {
                                timeLeft = breakTime
                                totalTime = studyTime
                            }

                            isPaused = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 4.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        ),
                    ) {
                        Text("Reset")
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row {
                        Button(
                            onClick = {
                                isRunning = true
                                isPaused = false
                            },
                            modifier = Modifier
                                .fillMaxWidth(.65f)
                                .padding(start = 30.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 4.dp,
                                bottomStart = 4.dp,
                                bottomEnd = 4.dp
                            )
                        ) {
                            Text("Start")
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Button(
                            onClick = {
                                sessionCount++
                                timeLeft = studyTime
                                totalTime = studyTime
                                isStudySession = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 30.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 16.dp,
                                bottomStart = 4.dp,
                                bottomEnd = 4.dp
                            )
                        ) {
                            // Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                            Text("Next session")
                        }

                    }


                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { showSettings = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 4.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        ),
                    ) {
                        Text("Settings")
                    }
                }
            }
        }

        if (showSettings) {
            SettingsBottomSheet(
                studyTime = studyTime,
                breakTime = breakTime,
                onStudyTimeChange = {
                    studyTime = it
                    if (isStudySession) {
                        timeLeft = studyTime
                        totalTime = studyTime
                    }
                },
                onBreakTimeChange = {
                    breakTime = it
                    if (!isStudySession) {
                        timeLeft = breakTime
                        totalTime = breakTime
                    }
                },
                onClose = { showSettings = false },
                onResetAll = {
                    studyTime = 25 * 60
                    breakTime = 5 * 60
                    timeLeft = 25 * 60
                    totalTime = 25 * 60
                    sessionCount = 1
                    isRunning = false
                    isPaused = false
                    isStudySession = true
                    showSettings = false
                }
            )
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
    onClose: () -> Unit,
    onResetAll: () -> Unit
) {
    var studyTimeInput by remember { mutableStateOf((studyTime / 60).toString()) }
    var breakTimeInput by remember { mutableStateOf((breakTime / 60).toString()) }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Settings", style = typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Study time (mins)", fontSize = 14.sp)
                TextField(
                    value = studyTimeInput,
                    onValueChange = {
                        studyTimeInput = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Break time (mins)", fontSize = 14.sp)
                TextField(
                    value = breakTimeInput,
                    onValueChange = {
                        breakTimeInput = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(

            ) {
                Button(
                    onClick = {
                        val studyTimeValue = studyTimeInput.toIntOrNull()
                        val breakTimeValue = breakTimeInput.toIntOrNull()

                        if (studyTimeValue != null && studyTimeValue > 0) {
                            onStudyTimeChange(studyTimeValue * 60)
                        }

                        if (breakTimeValue != null && breakTimeValue > 0) {
                            onBreakTimeChange(breakTimeValue * 60)
                        }

                        onClose()
                    },
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 4.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 4.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(.65f)
                        .padding(start = 30.dp)
                        .height(60.dp)
                ) {
                    Text("Confirm")
                }

                Spacer(modifier = Modifier.width(4.dp))

                OutlinedButton(
                    onClick = {
                        onResetAll()
                    },
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 16.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 16.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 30.dp)
                        .height(60.dp)
                ) {
                    Text(
                        "Reset all",
                        color = Color.Red,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    StudytimerTheme {
        AppContent()
    }
}