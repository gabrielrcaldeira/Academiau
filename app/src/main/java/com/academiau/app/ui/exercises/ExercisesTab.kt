package com.academiau.app.ui.exercises

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.academiau.app.data.Exercise
import com.academiau.app.data.AcademiauRepository
import com.academiau.app.ui.components.CreateExerciseDialog
import com.academiau.app.ui.components.ExerciseTipsDialog
import com.academiau.app.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesTab(
    repository: AcademiauRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var categoryFilter by remember { mutableStateOf("Todos") }
    var targetFilter by remember { mutableStateOf("Todos") }
    
    var limit by remember { mutableStateOf(40) }
    
    var showCreateDialog by remember { mutableStateOf(false) }
    var showTipsDialog by remember { mutableStateOf(false) }
    var selectedTipsExercise by remember { mutableStateOf("") }
    
    var exerciseToDelete by remember { mutableStateOf<Exercise?>(null) }
    
    var dbUpdatedCounter by remember { mutableStateOf(0) } 

    val categories = listOf("Todos", "Push", "Pull", "Legs")
    val muscles = listOf("Todos", "peito", "costas", "biceps", "triceps", "ombros", "quadriceps", "posteriores", "panturrilha", "geral")

    val filteredExercises = remember(searchQuery, categoryFilter, targetFilter, dbUpdatedCounter, repository.exercises) {
        repository.exercises.filter { ex ->
            val matchesSearch = searchQuery.isBlank() || ex.name.lowercase(Locale.ROOT).contains(searchQuery.lowercase(Locale.ROOT))
            val matchesCategory = categoryFilter == "Todos" || ex.category == categoryFilter
            val matchesTarget = when (targetFilter) {
                "Todos" -> true
                "geral" -> !listOf("peito", "costas", "biceps", "triceps", "ombros", "quadriceps", "posteriores", "panturrilha").contains(ex.target.lowercase(Locale.ROOT))
                else -> ex.target.lowercase(Locale.ROOT) == targetFilter.lowercase(Locale.ROOT)
            }
            matchesSearch && matchesCategory && matchesTarget
        }
    }

    val exercisesToRender = remember(filteredExercises, limit) {
        filteredExercises.take(limit)
    }

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Header
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        ) {
            Column {
                Text(
                    text = "Biblioteca",
                    style = Typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${filteredExercises.size} exercícios encontrados",
                    style = Typography.bodySmall,
                    color = ColorAccent,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = ColorAccent),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = "+ Criar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // Search
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it; limit = 40 },
            placeholder = { Text(text = "Buscar exercício...", color = TextMuted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = ColorAccent,
                unfocusedBorderColor = BorderColor,
                focusedContainerColor = BgSecondary,
                unfocusedContainerColor = BgSecondary
            ),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        // Filters
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 6.dp)) {
            items(categories) { cat ->
                val isSelected = categoryFilter == cat
                val color = when (cat) { "Push" -> ColorPush; "Pull" -> ColorPull; "Legs" -> ColorLegs; else -> ColorAccent }
                FilterChip(isSelected, if (cat == "Todos") "Todos" else cat, color) { categoryFilter = cat; limit = 40 }
            }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 12.dp)) {
            items(muscles) { muscle ->
                val isSelected = targetFilter == muscle
                FilterChip(isSelected, if (muscle == "Todos") "Músculos" else muscle.replaceFirstChar { it.uppercase() }, ColorAccent) { targetFilter = muscle; limit = 40 }
            }
        }

        // List with GIF on the Left
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(exercisesToRender) { ex ->
                val gifPath = repository.getExerciseGifPath(ex.name)
                Card(
                    colors = CardDefaults.cardColors(containerColor = BgSecondary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(85.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // GIF on the Left
                        Box(
                            modifier = Modifier
                                .size(85.dp)
                                .background(BgCard),
                            contentAlignment = Alignment.Center
                        ) {
                            if (gifPath != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = gifPath, imageLoader = imageLoader),
                                    contentDescription = ex.name,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize().padding(6.dp)
                                )
                            } else {
                                Text("GIF", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                        
                        // Info Section on the Right
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = ex.name, 
                                style = Typography.bodyLarge, 
                                color = TextPrimary, 
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = ex.target.replaceFirstChar { it.uppercase() }, 
                                color = TextSecondary, 
                                fontSize = 12.sp
                            )
                            
                            Spacer(modifier = Modifier.weight(1f))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📖 Dicas",
                                    color = ColorAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { selectedTipsExercise = ex.name; showTipsDialog = true }
                                )
                                Text(
                                    text = "🗑️ Excluir",
                                    color = ColorDanger,
                                    fontSize = 12.sp,
                                    modifier = Modifier.clickable { exerciseToDelete = ex }
                                )
                            }
                        }
                    }
                }
            }

            if (filteredExercises.size > limit) {
                item {
                    Button(
                        onClick = { limit += 40 },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorAccent.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .border(1.dp, ColorAccent.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    ) {
                        Text(text = "Carregar Mais", color = ColorAccent, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showTipsDialog) ExerciseTipsDialog(selectedTipsExercise, repository) { showTipsDialog = false }
    if (showCreateDialog) CreateExerciseDialog(repository, { dbUpdatedCounter++; showCreateDialog = false }) { showCreateDialog = false }

    if (exerciseToDelete != null) {
        AlertDialog(
            onDismissRequest = { exerciseToDelete = null },
            containerColor = BgSecondary,
            title = { Text("Excluir Exercício?", color = TextPrimary) },
            text = { Text("Tem certeza que deseja excluir '${exerciseToDelete?.name}' da biblioteca? Esta ação é irreversível.", color = TextSecondary) },
            confirmButton = {
                TextButton(onClick = {
                    exerciseToDelete?.let { repository.deleteExercise(it.id) }
                    exerciseToDelete = null
                    dbUpdatedCounter++
                }) {
                    Text("Excluir", color = ColorDanger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { exerciseToDelete = null }) {
                    Text("Cancelar", color = TextMuted)
                }
            }
        )
    }
}

@Composable
fun FilterChip(isSelected: Boolean, label: String, color: Color, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) color.copy(alpha = 0.15f) else BgSecondary)
            .border(1.dp, if (isSelected) color else BorderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp)
    ) {
        Text(
            text = label,
            style = Typography.bodyMedium,
            color = if (isSelected) color else TextPrimary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
