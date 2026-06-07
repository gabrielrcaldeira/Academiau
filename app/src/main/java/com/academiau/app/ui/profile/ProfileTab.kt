package com.academiau.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.academiau.app.data.AcademiauRepository
import com.academiau.app.ui.components.HistoryDetailItem
import com.academiau.app.ui.theme.*
import java.util.Locale

@Composable
fun ProfileTab(
    repository: AcademiauRepository,
    modifier: Modifier = Modifier
) {
    var historyUpdatedCounter by remember { mutableStateOf(0) }
    val history = remember(historyUpdatedCounter, repository.history) { repository.history }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Profile Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
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
                    fontSize = 32.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Atleta Academiau",
                style = Typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Foco e Constância",
                style = Typography.bodyMedium,
                color = TextMuted
            )
        }

        // Stats Summary
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val totalWorkouts = history.size
            val totalMin = history.sumOf { it.durationMillis } / 60000
            
            StatItem("Treinos", totalWorkouts.toString(), Modifier.weight(1f))
            StatItem("Minutos", totalMin.toString(), Modifier.weight(1f))
        }

        // History Section
        Text(
            text = "Meu Histórico",
            style = Typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Nenhum treino registrado ainda.", color = TextMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(history) { session ->
                    HistoryDetailItem(
                        session = session,
                        onDelete = {
                            repository.deleteWorkoutFromHistory(session.id)
                            historyUpdatedCounter++
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = BgSecondary),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.border(1.dp, BorderColor, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 24.sp, color = ColorAccent, fontWeight = FontWeight.Black)
            Text(text = label, fontSize = 12.sp, color = TextMuted, fontWeight = FontWeight.Bold)
        }
    }
}
