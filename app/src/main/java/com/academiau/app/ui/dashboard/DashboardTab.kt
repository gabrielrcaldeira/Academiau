package com.academiau.app.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.academiau.app.data.AcademiauRepository
import com.academiau.app.ui.components.*
import com.academiau.app.ui.theme.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.academiau.app.data.ExerciseSession
import com.academiau.app.data.SetRecord
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardTab(
    repository: AcademiauRepository,
    isWorkoutActive: Boolean,
    onToggleWorkout: (Boolean) -> Unit,
    workoutStartTime: Long = 0L,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var lastCompleted by remember { mutableStateOf(repository.lastCompletedWorkout) }
    
    var activeTab by remember { mutableStateOf(
        when(lastCompleted) {
            "Push" -> "Pull"
            "Pull" -> "Legs"
            else -> "Push"
        }
    ) }
    
    // Modal Dialog states
    var showAddDialog by remember { mutableStateOf(false) }
    var showTipsDialog by remember { mutableStateOf(false) }
    var selectedTipsExercise by remember { mutableStateOf("") }
    
    var showSubstituteDialog by remember { mutableStateOf(false) }
    var substituteIndex by remember { mutableStateOf(-1) }
    var exerciseToSubstitute by remember { mutableStateOf("") }

    var routineUpdatedCounter by remember { mutableStateOf(0) } 
    var historyUpdatedCounter by remember { mutableStateOf(0) }
    
    var showFullHistory by remember { mutableStateOf(false) }

    // State for weight/reps tracking
    // Map of ExerciseName -> List of Sets (Weight, Reps)
    val workoutProgress = remember(activeTab) {
        val initialMap = mutableStateMapOf<String, MutableList<SetRecord>>()
        repository.getRoutineList(activeTab).forEach { name ->
            val saved = repository.lastWeights[name]
            if (saved != null) {
                initialMap[name] = saved.toMutableList()
            } else {
                initialMap[name] = mutableListOf(SetRecord(0.0, 12), SetRecord(0.0, 12), SetRecord(0.0, 12))
            }
        }
        initialMap
    }

    val routineList = remember(activeTab, routineUpdatedCounter, repository.routines) {
        repository.getRoutineList(activeTab)
    }

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (android.os.Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // 1. WORKOUT CALENDAR (New)
        item {
            Text(
                text = "Cronograma de Treinos",
                style = Typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            val futureWorkouts = remember(lastCompleted) {
                val sequence = listOf("Push", "Pull", "Legs")
                var lastIdx = sequence.indexOf(lastCompleted)
                val result = mutableListOf<Pair<String, String>>()
                val calendar = Calendar.getInstance()
                
                for (i in 0..6) {
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                    val isWeekend = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
                    
                    val dayName = when(i) {
                        0 -> "Hoje"
                        1 -> "Amanhã"
                        else -> SimpleDateFormat("E", Locale("pt", "BR")).format(calendar.time).replaceFirstChar { it.uppercase() }
                    }
                    
                    val type = if (isWeekend) {
                        "Descanso"
                    } else {
                        lastIdx = (lastIdx + 1) % sequence.size
                        sequence[lastIdx]
                    }
                    
                    result.add(dayName to type)
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
                result
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(futureWorkouts) { _, (day, type) ->
                    val color = when (type) {
                        "Push" -> ColorPush
                        "Pull" -> ColorPull
                        "Legs" -> ColorLegs
                        else -> TextMuted
                    }
                    val isToday = day == "Hoje"
                    Surface(
                        color = if (isToday) color.copy(alpha = 0.2f) else BgSecondary,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isToday) color else BorderColor),
                        modifier = Modifier.width(85.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = day, color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = type, color = color, fontSize = 14.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // 2. START WORKOUT CARD
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
                            text = if (isWorkoutActive) "TREINO EM ANDAMENTO" else "PRONTO PARA COMEÇAR?",
                            style = Typography.labelMedium,
                            color = if (isWorkoutActive) ColorAccent else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = activeTab,
                            style = Typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Button(
                            onClick = {
                                if (!isWorkoutActive) {
                                    onToggleWorkout(true)
                                } else {
                                    val duration = System.currentTimeMillis() - workoutStartTime
                                    val sessionExercises = workoutProgress.map { (name, sets) ->
                                        ExerciseSession(name, sets.toList())
                                    }
                                    repository.saveLastCompletedWorkout(activeTab)
                                    repository.addWorkoutToHistory(activeTab, duration, sessionExercises)
                                    
                                    lastCompleted = activeTab
                                    onToggleWorkout(false)
                                    historyUpdatedCounter++
                                    Toast.makeText(context, "Treino $activeTab concluído! 🔥", Toast.LENGTH_SHORT).show()
                                    activeTab = when(activeTab) {
                                        "Push" -> "Pull"
                                        "Pull" -> "Legs"
                                        else -> "Push"
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isWorkoutActive) ColorSuccess else ColorAccent),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                        ) {
                            Text(
                                text = if (isWorkoutActive) "FINALIZAR TREINO 🏁" else "INICIAR TREINO AGORA ⚡",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(0.8f)
                            .height(110.dp)
                            .padding(start = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        MuscleMap(activeSplit = activeTab)
                    }
                }
            }
        }

        // 3. WORKOUT HEADER & ROUTINE TAB SELECTOR
        item {
            Column {
                Text(
                    text = "Plano de Treino",
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
                                .height(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(btnBg)
                                .border(1.dp, btnBorder, RoundedCornerShape(10.dp))
                                .clickable { activeTab = tab }
                        ) {
                            Text(
                                text = tab,
                                style = Typography.bodyMedium,
                                color = if (isSelected) tabColor else TextPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                if (activeTab != when(lastCompleted) { "Push" -> "Pull"; "Pull" -> "Legs"; else -> "Push" } && !isWorkoutActive) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            repository.setAsNextWorkout(activeTab)
                            lastCompleted = repository.lastCompletedWorkout
                            Toast.makeText(context, "Próximo treino definido como $activeTab! ✅", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorAccent.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .border(1.dp, ColorAccent.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "⭐ Definir como Próximo Treino", color = ColorAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 4. EXERCISE LIST WITH PROMINENT GIFS
        if (routineList.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = BgSecondary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                        Text(text = "Nenhum exercício. Adicione abaixo!", color = TextMuted)
                    }
                }
            }
        } else {
            itemsIndexed(routineList) { index, name ->
                val template = repository.exercises.find { it.name.lowercase(Locale.ROOT).trim() == name.lowercase(Locale.ROOT).trim() }
                val targetMuscle = template?.target ?: "geral"
                val gifPath = repository.getExerciseGifPath(name)

                Card(
                    colors = CardDefaults.cardColors(containerColor = BgSecondary),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // GIF on the Left
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(BgCard),
                                contentAlignment = Alignment.Center
                            ) {
                                if (gifPath != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = gifPath, imageLoader = imageLoader),
                                        contentDescription = name,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxSize().padding(8.dp)
                                    )
                                } else {
                                    Text(text = "GIF", color = TextMuted, fontSize = 10.sp)
                                }
                            }

                            // Info and Actions on the Right
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = name,
                                            style = Typography.bodyLarge,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.ExtraBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = targetMuscle.replaceFirstChar { it.uppercase() },
                                            color = when (targetMuscle) {
                                                "peito" -> ColorPush
                                                "costas" -> ColorPull
                                                "quadriceps" -> ColorLegs
                                                else -> ColorAccent
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(
                                            text = "📖 Dicas",
                                            color = ColorAccent,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.clickable {
                                                selectedTipsExercise = name
                                                showTipsDialog = true
                                            }
                                        )
                                        Text(
                                            text = "🔄 Trocar",
                                            color = ColorPull,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.clickable {
                                                exerciseToSubstitute = name
                                                substituteIndex = index
                                                showSubstituteDialog = true
                                            }
                                        )
                                        Text(
                                            text = "🗑️",
                                            color = ColorDanger,
                                            fontSize = 12.sp,
                                            modifier = Modifier.clickable {
                                                repository.removeExerciseFromRoutine(activeTab, index)
                                                routineUpdatedCounter++
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // WORKOUT PROGRESS SECTION (Sets, Weights, Reps)
                        var isExpanded by remember { mutableStateOf(false) }
                        
                        HorizontalDivider(color = BorderColor.copy(alpha = 0.5f), thickness = 1.dp)
                        
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isExpanded = !isExpanded }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val currentSets = workoutProgress[name] ?: emptyList<SetRecord>()
                                val setsText = if (currentSets.isEmpty()) "Sem séries" else "${currentSets.size} séries registradas"
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🏋️", fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
                                    Text(
                                        text = setsText,
                                        style = Typography.bodySmall,
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Text(
                                    text = if (isExpanded) "Recolher ▲" else "Ajustar Cargas ▼",
                                    color = ColorAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (isExpanded) {
                                Column(modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, bottom = 12.dp)) {
                                    val currentSets = workoutProgress[name] ?: mutableListOf<SetRecord>().also {
                                        it.add(SetRecord(0.0, 12))
                                        it.add(SetRecord(0.0, 12))
                                        it.add(SetRecord(0.0, 12))
                                        workoutProgress[name] = it
                                    }

                                    currentSets.forEachIndexed { sIndex, record ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "S${sIndex + 1}",
                                                color = TextSecondary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.width(24.dp)
                                            )
                                            
                                            // Weight Input
                                            OutlinedTextField(
                                                value = if (record.weight == 0.0) "" else if (record.weight % 1 == 0.0) record.weight.toInt().toString() else record.weight.toString(),
                                                onValueChange = { input ->
                                                    val clean = input.filter { it.isDigit() || it == '.' }
                                                    val newWeight = clean.toDoubleOrNull() ?: 0.0
                                                    currentSets[sIndex] = record.copy(weight = newWeight)
                                                    workoutProgress[name] = currentSets
                                                    repository.saveWeights(name, currentSets.toList())
                                                },
                                                placeholder = { Text("0", fontSize = 12.sp) },
                                                modifier = Modifier.weight(1f).height(48.dp),
                                                textStyle = Typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold),
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                                singleLine = true,
                                                shape = RoundedCornerShape(8.dp),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = ColorAccent,
                                                    unfocusedBorderColor = BorderColor,
                                                    unfocusedContainerColor = BgCard
                                                ),
                                                prefix = { Text("Kg ", fontSize = 10.sp, color = TextMuted) }
                                            )

                                            // Reps Input
                                            OutlinedTextField(
                                                value = if (record.reps == 0) "" else record.reps.toString(),
                                                onValueChange = { input ->
                                                    val clean = input.filter { it.isDigit() }
                                                    val filtered = if (clean.length > 1 && clean.startsWith("0")) clean.substring(1) else clean
                                                    val newReps = filtered.toIntOrNull() ?: 0
                                                    currentSets[sIndex] = record.copy(reps = newReps)
                                                    workoutProgress[name] = currentSets
                                                    repository.saveWeights(name, currentSets.toList())
                                                },
                                                placeholder = { Text("0", fontSize = 12.sp) },
                                                modifier = Modifier.weight(1f).height(48.dp),
                                                textStyle = Typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold),
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                singleLine = true,
                                                shape = RoundedCornerShape(8.dp),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = ColorAccent,
                                                    unfocusedBorderColor = BorderColor,
                                                    unfocusedContainerColor = BgCard
                                                ),
                                                suffix = { Text(" reps", fontSize = 10.sp, color = TextMuted) }
                                            )

                                            // Delete Set
                                            if (currentSets.size > 1) {
                                                IconButton(
                                                    onClick = {
                                                        currentSets.removeAt(sIndex)
                                                        workoutProgress[name] = currentSets
                                                        repository.saveWeights(name, currentSets.toList())
                                                    },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Text("✕", color = ColorDanger, fontSize = 12.sp)
                                                }
                                            }
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            val last = currentSets.lastOrNull() ?: SetRecord(0.0, 12)
                                            currentSets.add(last.copy())
                                            workoutProgress[name] = currentSets
                                            repository.saveWeights(name, currentSets.toList())
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ColorAccent.copy(alpha = 0.1f)),
                                        contentPadding = PaddingValues(0.dp),
                                        modifier = Modifier.fillMaxWidth().height(32.dp).padding(top = 4.dp),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text("+ Adicionar Série", color = ColorAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. ADD ACTION
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
                Text(text = "+ Adicionar Exercício ao Treino", color = TextPrimary, fontWeight = FontWeight.Bold)
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

    if (showFullHistory) {
        WorkoutHistoryDialog(
            repository = repository,
            onDismiss = { showFullHistory = false },
            onHistoryUpdated = { historyUpdatedCounter++ }
        )
    }
}
