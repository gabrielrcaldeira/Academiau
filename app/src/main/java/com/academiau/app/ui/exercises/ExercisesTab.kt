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
    
    var limit by remember { mutableStateOf(60) }
    
    var showCreateDialog by remember { mutableStateOf(false) }
    var showTipsDialog by remember { mutableStateOf(false) }
    var selectedTipsExercise by remember { mutableStateOf("") }
    
    var dbUpdatedCounter by remember { mutableStateOf(0) } // trigger re-filter on exercise deletion or addition

    val categories = listOf("Todos", "Push", "Pull", "Legs")
    val muscles = listOf("Todos", "peito", "costas", "biceps", "triceps", "ombros", "quadriceps", "posteriores", "panturrilha", "geral")

    // Dynamic filtering
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

    // Coil config for GIFs
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Banco de Exercícios",
                        style = Typography.headlineMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .background(ColorSuccess.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .border(1.dp, ColorSuccess.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${filteredExercises.size} itens",
                            color = ColorSuccess,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "Gerencie seus exercícios para cada tipo de treino.",
                    style = Typography.bodyMedium,
                    color = TextSecondary
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

        // Search textfield
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                limit = 60 // reset limit when search query changes
            },
            placeholder = { Text(text = "Buscar exercício no banco de movimentos...", color = TextMuted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = ColorAccent,
                unfocusedBorderColor = BorderColor,
                focusedContainerColor = BgSecondary,
                unfocusedContainerColor = BgSecondary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Horizontal filter: Category
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            items(categories) { cat ->
                val isSelected = categoryFilter == cat
                val color = when (cat) {
                    "Push" -> ColorPush
                    "Pull" -> ColorPull
                    "Legs" -> ColorLegs
                    else -> ColorAccent
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) color.copy(alpha = 0.15f) else BgSecondary)
                        .border(
                            1.dp,
                            if (isSelected) color else BorderColor,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            categoryFilter = cat
                            limit = 60
                        }
                        .padding(horizontal = 14.dp)
                ) {
                    Text(
                        text = if (cat == "Todos") "Todos Treinos" else cat,
                        style = Typography.bodyMedium,
                        color = if (isSelected) color else TextPrimary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Horizontal filter: Target Muscle
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            items(muscles) { muscle ->
                val isSelected = targetFilter == muscle
                val displayName = if (muscle == "Todos") "Todos Músculos" else muscle.replaceFirstChar { it.uppercase() }
                
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) ColorAccent.copy(alpha = 0.15f) else BgSecondary)
                        .border(
                            1.dp,
                            if (isSelected) ColorAccent else BorderColor,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            targetFilter = muscle
                            limit = 60
                        }
                        .padding(horizontal = 14.dp)
                ) {
                    Text(
                        text = displayName,
                        style = Typography.bodyMedium,
                        color = if (isSelected) ColorAccent else TextPrimary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Exercises Grid list
        if (exercisesToRender.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Nenhum exercício encontrado com os filtros selecionados.", color = TextMuted)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(exercisesToRender) { idx, ex ->
                    val gifPath = repository.getExerciseGifPath(ex.name)
                    
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
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left side: GIF preview box
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(BgCard)
                                    .border(1.dp, BorderColor, RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (gifPath != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = gifPath,
                                            imageLoader = imageLoader
                                        ),
                                        contentDescription = ex.name,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Text(text = "Sem GIF", fontSize = 9.sp, color = TextMuted)
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Middle side: details
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = ex.name,
                                        style = Typography.bodyLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    
                                    // Muscle tag
                                    Box(
                                        modifier = Modifier
                                            .background(ColorAccent.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = ex.target.replaceFirstChar { it.uppercase() },
                                            color = ColorAccent,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                Text(
                                    text = "Categoria: ${ex.category} | Padrão: 3 séries x 6-12 repetições",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                
                                Spacer(modifier = Modifier.height(6.dp))
                                
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Dicas",
                                        color = ColorAccent,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clickable {
                                                selectedTipsExercise = ex.name
                                                showTipsDialog = true
                                            }
                                    )

                                    Text(
                                        text = "Remover",
                                        color = ColorDanger,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .clickable {
                                                repository.deleteExercise(ex.id)
                                                dbUpdatedCounter++
                                            }
                                    )
                                }
                            }
                        }
                    }
                }

                // Load More button
                if (filteredExercises.size > limit) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = "Mostrando $limit de ${filteredExercises.size} exercícios.",
                                fontSize = 12.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            Button(
                                onClick = { limit += 60 },
                                colors = ButtonDefaults.buttonColors(containerColor = BgCard),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                            ) {
                                Text(text = "Carregar Mais", color = TextPrimary)
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal dialogs trigger
    if (showTipsDialog) {
        ExerciseTipsDialog(
            exerciseName = selectedTipsExercise,
            repository = repository,
            onDismiss = { showTipsDialog = false }
        )
    }

    if (showCreateDialog) {
        CreateExerciseDialog(
            repository = repository,
            onSave = {
                dbUpdatedCounter++
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }
}
