package com.academiau.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.academiau.app.data.AcademiauRepository
import com.academiau.app.data.WorkoutSession
import com.academiau.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WorkoutHistoryDialog(
    repository: AcademiauRepository,
    onDismiss: () -> Unit,
    onHistoryUpdated: () -> Unit
) {
    var sessionToDelete by remember { mutableStateOf<WorkoutSession?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = BgPrimary),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .border(1.dp, BorderColor, RoundedCornerShape(24.dp))
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Histórico Detalhado",
                            style = Typography.headlineSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${repository.history.size} treinos registrados",
                            style = Typography.bodySmall,
                            color = TextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Text("✕", color = TextSecondary, fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatBox("Frequência", "${repository.history.size}x", Modifier.weight(1f))
                    val totalDurationMin = repository.history.sumOf { it.durationMillis } / 60000
                    StatBox("Tempo Total", "${totalDurationMin}min", Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // History List
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (repository.history.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            Text("Nenhum treino ainda.", color = TextMuted)
                        }
                    } else {
                        repository.history.forEach { session ->
                            HistoryDetailItem(
                                session = session,
                                onDelete = { sessionToDelete = session }
                            )
                        }
                    }
                }
            }
        }
    }

    if (sessionToDelete != null) {
        AlertDialog(
            onDismissRequest = { sessionToDelete = null },
            containerColor = BgSecondary,
            title = { Text("Apagar Treino?", color = TextPrimary) },
            text = { Text("Tem certeza que deseja apagar este registro do histórico? Esta ação é irreversível.", color = TextSecondary) },
            confirmButton = {
                TextButton(onClick = {
                    repository.deleteWorkoutFromHistory(sessionToDelete!!.id)
                    sessionToDelete = null
                    onHistoryUpdated()
                }) {
                    Text("Apagar", color = ColorDanger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { sessionToDelete = null }) {
                    Text("Cancelar", color = TextMuted)
                }
            }
        )
    }
}

@Composable
fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        color = BgSecondary,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderColor),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
            Text(text = value, fontSize = 16.sp, color = ColorAccent, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun HistoryDetailItem(session: WorkoutSession, onDelete: () -> Unit) {
    val dateStr = SimpleDateFormat("dd 'de' MMMM, HH:mm", Locale("pt", "BR")).format(Date(session.timestamp))
    val durationMin = session.durationMillis / 60000
    val color = when (session.type) {
        "Push" -> ColorPush
        "Pull" -> ColorPull
        else -> ColorLegs
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = BgSecondary),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, BorderColor, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = session.type, color = color, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    Text(text = dateStr, color = TextMuted, fontSize = 11.sp)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Text("🗑️", fontSize = 14.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⏱️ ${durationMin} min", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(12.dp))
                val totalWeight = session.exercises.sumOf { it.sets.sumOf { s -> s.weight * s.reps } }
                if (totalWeight > 0) {
                    Text("🏋️ ${totalWeight.toInt()} Kg total", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
