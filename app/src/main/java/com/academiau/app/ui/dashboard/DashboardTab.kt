package com.academiau.app.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.academiau.app.data.AcademiauRepository
import com.academiau.app.ui.components.*
import com.academiau.app.ui.theme.*
import java.util.Calendar
import java.util.Locale

@Composable
fun DashboardTab(
    repository: AcademiauRepository,
    isWorkoutActive: Boolean,
    onToggleWorkout: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var lastCompleted by remember { mutableStateOf(repository.lastCompletedWorkout) }
    val suggestedWorkout = remember(lastCompleted) { repository.getSuggestedWorkout() }
    
    var activeTab by remember { mutableStateOf(suggestedWorkout) }
    val activeExercises = remember(activeTab, repository.routines) { repository.getRoutineList(activeTab) }

    // Modal Dialog states
    var showAddDialog by remember { mutableStateOf(false) }
    var showTipsDialog by remember { mutableStateOf(false) }
    var selectedTipsExercise by remember { mutableStateOf("") }
    
    var showSubstituteDialog by remember { mutableStateOf(false) }
    var substituteIndex by remember { mutableStateOf(-1) }
    var exerciseToSubstitute by remember { mutableStateOf("") }

    var routineUpdatedCounter by remember { mutableStateOf(0) } // trigger re-compositions on routine change

    // Re-bind active list when counter changes
    val routineList = remember(activeTab, routineUpdatedCounter, repository.routines) {
        repository.getRoutineList(activeTab)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // 1. SUGGESTÃO DO DIA CARD
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BgSecondary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "OTIMIZADOR DIÁRIO (SEG-SEX)",
                            style = Typography.labelMedium,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Recomendação: Treino $suggestedWorkout",
                            style = Typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Com base no seu histórico rolável, este é o próximo treino do seu ciclo ABC.",
                            style = Typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    activeTab = suggestedWorkout
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = if (isWorkoutActive) Color.Gray else ColorAccent),
                                enabled = !isWorkoutActive,
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(text = "Ver Treino", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Button(
                                onClick = {
                                    if (!isWorkoutActive) {
                                        onToggleWorkout(true)
                                    } else {
                                        repository.saveLastCompletedWorkout(activeTab)
                                        lastCompleted = activeTab
                                        onToggleWorkout(false)
                                        Toast.makeText(context, "Treino $activeTab concluído! 🔥", Toast.LENGTH_SHORT).show()
                                        activeTab = repository.getSuggestedWorkout()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = if (isWorkoutActive) ColorSuccess else BgCard),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .height(36.dp)
                                    .border(1.dp, if (isWorkoutActive) ColorSuccess else BorderColor, RoundedCornerShape(8.dp))
                            ) {
                                Text(
                                    text = if (isWorkoutActive) "Finalizar Treino 🏁" else "Iniciar Treino ⚡",
                                    color = if (isWorkoutActive) Color.White else TextPrimary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(0.8f)
                            .height(130.dp)
                            .padding(start = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        MuscleMap(activeSplit = suggestedWorkout)
                    }
                }
            }
        }

        // 2. PLANNER CARD
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BgSecondary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Cronograma de Segunda a Sexta",
                        style = Typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Acompanhe o fluxo rolável do seu ABC de segunda a sexta. Planejados para esta semana:",
                        style = Typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Projection Logic
                    val days = listOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta")
                    val calendar = Calendar.getInstance()
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                    val todayIndex = when (dayOfWeek) {
                        Calendar.MONDAY -> 0
                        Calendar.TUESDAY -> 1
                        Calendar.WEDNESDAY -> 2
                        Calendar.THURSDAY -> 3
                        Calendar.FRIDAY -> 4
                        else -> -1
                    }

                    var tempSplit = lastCompleted
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        days.forEachIndexed { index, day ->
                            // Roll projected sequence forward
                            tempSplit = when (tempSplit) {
                                "Push" -> "Pull"
                                "Pull" -> "Legs"
                                else -> "Push"
                            }

                            val isToday = index == todayIndex
                            val splitLabel = when (tempSplit) {
                                "Push" -> "Push (A)"
                                "Pull" -> "Pull (B)"
                                else -> "Legs (C)"
                            }

                            val splitColor = when (tempSplit) {
                                "Push" -> ColorPush
                                "Pull" -> ColorPull
                                else -> ColorLegs
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isToday) splitColor.copy(alpha = 0.12f) else Color(0x06FFFFFF))
                                    .border(
                                        1.dp,
                                        if (isToday) splitColor else BorderColor,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = day.substring(0, 3),
                                        style = Typography.labelMedium,
                                        color = if (isToday) splitColor else TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = when (tempSplit) {
                                            "Push" -> "A"
                                            "Pull" -> "B"
                                            else -> "C"
                                        },
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isToday) "Hoje ⚡" else "Planejado",
                                        fontSize = 8.sp,
                                        color = if (isToday) splitColor else TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. WORKOUT HEADER & ROUTINE TAB SELECTOR
        item {
            Column {
                Text(
                    text = "Foco do Treino",
                    style = Typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val tabs = listOf("Push", "Pull", "Legs")
                    tabs.forEach { tab ->
                        val isSelected = activeTab == tab
                        val tabColor = when (tab) {
                            "Push" -> ColorPush
                            "Pull" -> ColorPull
                            else -> ColorLegs
                        }
                        val btnBg = if (isSelected) tabColor.copy(alpha = 0.15f) else BgSecondary
                        val btnBorder = if (isSelected) tabColor else BorderColor
                        
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(btnBg)
                                .border(1.dp, btnBorder, RoundedCornerShape(8.dp))
                                .clickable { activeTab = tab }
                        ) {
                            Text(
                                text = when (tab) {
                                    "Push" -> "Treino A (Push)"
                                    "Pull" -> "Treino B (Pull)"
                                    else -> "Treino C (Legs)"
                                },
                                style = Typography.bodyMedium,
                                color = if (isSelected) tabColor else TextPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // 4. EXERCISE LIST
        if (routineList.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = BgSecondary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(text = "Nenhum exercício neste treino. Adicione abaixo!", color = TextMuted)
                    }
                }
            }
        } else {
            itemsIndexed(routineList) { index, name ->
                val template = repository.exercises.find { it.name.lowercase(Locale.ROOT).trim() == name.lowercase(Locale.ROOT).trim() }
                val targetMuscle = template?.target ?: "geral"

                Card(
                    colors = CardDefaults.cardColors(containerColor = BgSecondary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1.5f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = name,
                                    style = Typography.bodyLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                // Target muscle badge
                                Box(
                                    modifier = Modifier
                                        .background(
                                            when (targetMuscle) {
                                                "peito" -> ColorPush.copy(alpha = 0.1f)
                                                "costas" -> ColorPull.copy(alpha = 0.1f)
                                                "quadriceps" -> ColorLegs.copy(alpha = 0.1f)
                                                "posteriores" -> ColorLegs.copy(alpha = 0.1f)
                                                "panturrilha" -> ColorLegs.copy(alpha = 0.1f)
                                                else -> ColorAccent.copy(alpha = 0.1f)
                                            },
                                            RoundedCornerShape(4.dp)
                                        )
                                        .border(
                                            1.dp,
                                            when (targetMuscle) {
                                                "peito" -> ColorPush.copy(alpha = 0.3f)
                                                "costas" -> ColorPull.copy(alpha = 0.3f)
                                                "quadriceps" -> ColorLegs.copy(alpha = 0.3f)
                                                "posteriores" -> ColorLegs.copy(alpha = 0.3f)
                                                "panturrilha" -> ColorLegs.copy(alpha = 0.3f)
                                                else -> ColorAccent.copy(alpha = 0.3f)
                                            },
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = targetMuscle.replaceFirstChar { it.uppercase() },
                                        color = when (targetMuscle) {
                                            "peito" -> ColorPush
                                            "costas" -> ColorPull
                                            "quadriceps" -> ColorLegs
                                            "posteriores" -> ColorLegs
                                            "panturrilha" -> ColorLegs
                                            else -> ColorAccent
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        selectedTipsExercise = name
                                        showTipsDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BgCard),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    modifier = Modifier
                                        .height(28.dp)
                                        .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
                                ) {
                                    Text(text = "Dicas", color = TextPrimary, fontSize = 11.sp)
                                }

                                Button(
                                    onClick = {
                                        exerciseToSubstitute = name
                                        substituteIndex = index
                                        showSubstituteDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BgCard),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    modifier = Modifier
                                        .height(28.dp)
                                        .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
                                ) {
                                    Text(text = "Substituir", color = TextPrimary, fontSize = 11.sp)
                                }

                                Button(
                                    onClick = {
                                        repository.removeExerciseFromRoutine(activeTab, index)
                                        routineUpdatedCounter++
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ColorDanger.copy(alpha = 0.1f)),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    modifier = Modifier
                                        .height(28.dp)
                                        .border(1.dp, ColorDanger.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                ) {
                                    Text(text = "Remover", color = ColorDanger, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Right side: sets/reps trigger timer
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .weight(0.5f)
                        ) {
                            Text(
                                text = "3 séries",
                                style = Typography.bodyLarge,
                                color = ColorAccent,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "6-12 reps",
                                style = Typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // 5. ADD TO ROUTINE ACTION BUTTON
        item {
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = BgCard),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = "+ Adicionar Exercício ao Treino",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        // 6. BOTTOM ROUTINE FOCUS MAP WIDGET
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BgSecondary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Músculos Alvo",
                        style = Typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier.height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        MuscleMap(activeSplit = activeTab)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Os músculos alvos do treino ativo (${activeTab}) estão destacados acima.",
                        style = Typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    // Dialog sheets management
    if (showTipsDialog) {
        ExerciseTipsDialog(
            exerciseName = selectedTipsExercise,
            repository = repository,
            onDismiss = { showTipsDialog = false }
        )
    }

    if (showSubstituteDialog) {
        SubstituteExerciseDialog(
            exerciseNameToReplace = exerciseToSubstitute,
            category = activeTab,
            repository = repository,
            onConfirm = { newName ->
                repository.substituteExercise(activeTab, substituteIndex, newName)
                showSubstituteDialog = false
                routineUpdatedCounter++
            },
            onDismiss = { showSubstituteDialog = false }
        )
    }

    if (showAddDialog) {
        AddExerciseToRoutineDialog(
            category = activeTab,
            repository = repository,
            onConfirm = { name ->
                repository.addExerciseToRoutine(activeTab, name)
                showAddDialog = false
                routineUpdatedCounter++
            },
            onDismiss = { showAddDialog = false }
        )
    }
}
