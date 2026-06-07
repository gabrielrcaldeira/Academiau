package com.academiau.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.academiau.app.data.AcademiauRepository
import com.academiau.app.ui.components.WorkoutHistoryDialog
import com.academiau.app.ui.dashboard.DashboardTab
import com.academiau.app.ui.exercises.ExercisesTab
import com.academiau.app.ui.profile.ProfileTab
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
                
                var showHistoryDialog by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgPrimary
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Scaffold(
                            topBar = { 
                                TopHeader(
                                    workoutStartTime = if (isWorkoutActive) workoutStartTime else 0L,
                                    onShowHistory = { showHistoryDialog = true }
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
                                when (currentTab) {
                                    "dashboard" -> {
                                        DashboardTab(
                                            repository = repository,
                                            isWorkoutActive = isWorkoutActive,
                                            workoutStartTime = workoutStartTime,
                                            onToggleWorkout = { active ->
                                                isWorkoutActive = active
                                                if (active) {
                                                    workoutStartTime = System.currentTimeMillis()
                                                }
                                            }
                                        )
                                    }
                                    "exercises" -> {
                                        ExercisesTab(repository = repository)
                                    }
                                    "profile" -> {
                                        ProfileTab(repository = repository)
                                    }
                                }
                            }
                        }
                    }
                }

                if (showHistoryDialog) {
                    WorkoutHistoryDialog(
                        repository = repository,
                        onDismiss = { showHistoryDialog = false },
                        onHistoryUpdated = { /* No-op here, handled by repo state */ }
                    )
                }
            }
        }
    }
}

@Composable
fun TopHeader(
    workoutStartTime: Long,
    onShowHistory: () -> Unit
) {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var menuExpanded by remember { mutableStateOf(false) }
    
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
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Logo Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(ColorAccent, Color(0xFF7C3AED))
                        )
                    )
            ) {
                // Gym Dumbbell Drawing Logo
                Canvas(modifier = Modifier.size(24.dp)) {
                    val w = size.width
                    val h = size.height
                    val barWidth = w * 0.6f
                    val barHeight = h * 0.15f
                    val plateWidth = w * 0.15f
                    val plateHeight = h * 0.6f
                    
                    // Bar
                    drawRect(Color.White, Offset((w - barWidth)/2, (h - barHeight)/2), Size(barWidth, barHeight))
                    // Left Plates
                    drawRoundRect(Color.White, Offset(w*0.1f, (h - plateHeight)/2), Size(plateWidth, plateHeight), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f))
                    drawRoundRect(Color.White, Offset(w*0.25f, (h - plateHeight*0.8f)/2), Size(plateWidth*0.8f, plateHeight*0.8f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f))
                    // Right Plates
                    drawRoundRect(Color.White, Offset(w*0.75f, (h - plateHeight)/2), Size(plateWidth, plateHeight), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f))
                    drawRoundRect(Color.White, Offset(w*0.65f, (h - plateHeight*0.8f)/2), Size(plateWidth*0.8f, plateHeight*0.8f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f))
                }
            }
            Column {
                Text(
                    text = "Academiau",
                    style = Typography.titleLarge.copy(letterSpacing = 0.5.sp),
                    color = TextPrimary,
                    fontWeight = FontWeight.Black
                )
                if (workoutStartTime > 0) {
                    val durationSeconds = (currentTime - workoutStartTime) / 1000
                    val minutes = durationSeconds / 60
                    val seconds = durationSeconds % 60
                    Text(
                        text = String.format("• ⏱️ %02d:%02d •", minutes, seconds),
                        color = ColorAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                } else {
                    Text(
                        text = "Seu parceiro de treino",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Profile Avatar Widget with Expandable Menu
        Box {
            Surface(
                color = Color(0x0AFFFFFF),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { menuExpanded = true }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFF43F5E), Color(0xFFF59E0B))
                                )
                            )
                    ) {
                        Text(
                            text = "G",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = "Atleta",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (menuExpanded) "▲" else "▼",
                        color = TextMuted,
                        fontSize = 8.sp
                    )
                }
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier
                    .background(BgSecondary)
                    .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
            ) {
                DropdownMenuItem(
                    text = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📊 ", fontSize = 16.sp)
                            Text("Ver Histórico", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    },
                    onClick = {
                        menuExpanded = false
                        onShowHistory()
                    }
                )
                DropdownMenuItem(
                    text = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("👤 ", fontSize = 16.sp)
                            Text("Meu Perfil", color = TextPrimary)
                        }
                    },
                    onClick = { menuExpanded = false }
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
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = dashColor,
                    modifier = Modifier.size(24.dp)
                )
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
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = null,
                    tint = exColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Exercícios",
                    color = exColor,
                    fontSize = 11.sp,
                    fontWeight = if (isExercises) FontWeight.Bold else FontWeight.Normal
                )
            }

            // Profile Nav item
            val isProfile = currentTab == "profile"
            val prColor = if (isProfile) ColorAccent else TextSecondary
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onTabSelected("profile") }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = prColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Perfil",
                    color = prColor,
                    fontSize = 11.sp,
                    fontWeight = if (isProfile) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
