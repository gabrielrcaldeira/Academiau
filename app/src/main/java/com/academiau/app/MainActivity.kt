package com.academiau.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.academiau.app.data.AcademiauRepository
import com.academiau.app.ui.dashboard.DashboardTab
import com.academiau.app.ui.exercises.ExercisesTab
import com.academiau.app.ui.theme.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private lateinit var repository: AcademiauRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = AcademiauRepository(applicationContext)

        setContent {
            AcademiauTheme {
                var currentTab by remember { mutableStateOf("dashboard") }
                var workoutStartTime by remember { mutableLongStateOf(0L) }
                var isWorkoutActive by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgPrimary
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Scaffold(
                            topBar = { 
                                TopHeader(
                                    workoutStartTime = if (isWorkoutActive) workoutStartTime else 0L
                                ) 
                            },
                            bottomBar = {
                                BottomNavBar(
                                    currentTab = currentTab,
                                    onTabSelected = { currentTab = it }
                                )
                            },
                            containerColor = Color.Transparent,
                            modifier = Modifier.fillMaxSize()
                        ) { paddingValues ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            ) {
                                if (currentTab == "dashboard") {
                                    DashboardTab(
                                        repository = repository,
                                        isWorkoutActive = isWorkoutActive,
                                        onToggleWorkout = { active ->
                                            isWorkoutActive = active
                                            if (active) {
                                                workoutStartTime = System.currentTimeMillis()
                                            }
                                        }
                                    )
                                } else {
                                    ExercisesTab(repository = repository)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopHeader(workoutStartTime: Long) {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    
    LaunchedEffect(workoutStartTime) {
        if (workoutStartTime > 0) {
            while (true) {
                delay(1000L)
                currentTime = System.currentTimeMillis()
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Logo Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(ColorAccent, Color(0xFFA855F7))
                        )
                    )
            ) {
                Text(
                    text = "A",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            }
            Column {
                Text(
                    text = "Academiau",
                    style = Typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold
                )
                if (workoutStartTime > 0) {
                    val durationSeconds = (currentTime - workoutStartTime) / 1000
                    val minutes = durationSeconds / 60
                    val seconds = durationSeconds % 60
                    Text(
                        text = String.format("⏱️ %02d:%02d", minutes, seconds),
                        color = ColorAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Profile Avatar Widget
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
                .background(Color(0x06FFFFFF), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFF43F5E), Color(0xFFF59E0B))
                        )
                    )
            ) {
                Text(
                    text = "A",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp
                )
            }
            Column {
                Text(
                    text = "Atleta",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Foco & Constância",
                    color = TextSecondary,
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    Surface(
        color = Color(0xEC0F172A), // Glassmorphism backdrop
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .border(1.dp, BorderColor, RoundedCornerShape(0.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dashboard Nav item
            val isDashboard = currentTab == "dashboard"
            val dashColor = if (isDashboard) ColorAccent else TextSecondary
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onTabSelected("dashboard") }
                    .padding(8.dp)
            ) {
                DashboardIcon(color = dashColor)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Painel",
                    color = dashColor,
                    fontSize = 11.sp,
                    fontWeight = if (isDashboard) FontWeight.Bold else FontWeight.Normal
                )
            }

            // Exercises Nav item
            val isExercises = currentTab == "exercises"
            val exColor = if (isExercises) ColorAccent else TextSecondary
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onTabSelected("exercises") }
                    .padding(8.dp)
            ) {
                ListIcon(color = exColor)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Exercícios",
                    color = exColor,
                    fontSize = 11.sp,
                    fontWeight = if (isExercises) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun DashboardIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val padding = 2.dp.toPx()
        val rw = (w - padding) / 2
        val rh = (h - padding) / 2
        drawRect(color, Offset(0f, 0f), Size(rw, rh))
        drawRect(color, Offset(rw + padding, 0f), Size(rw, rh))
        drawRect(color, Offset(0f, rh + padding), Size(rw, rh))
        drawRect(color, Offset(rw + padding, rh + padding), Size(rw, rh))
    }
}

@Composable
fun ListIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val thickness = 2.dp.toPx()
        val gap = 5.dp.toPx()
        drawLine(color, Offset(0f, 2.dp.toPx()), Offset(w, 2.dp.toPx()), thickness)
        drawLine(color, Offset(0f, 2.dp.toPx() + gap), Offset(w, 2.dp.toPx() + gap), thickness)
        drawLine(color, Offset(0f, 2.dp.toPx() + gap * 2), Offset(w, 2.dp.toPx() + gap * 2), thickness)
    }
}
