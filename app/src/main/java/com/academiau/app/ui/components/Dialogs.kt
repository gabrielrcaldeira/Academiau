package com.academiau.app.ui.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.academiau.app.data.Exercise
import com.academiau.app.data.AcademiauRepository
import com.academiau.app.data.WorkoutSession
import com.academiau.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExerciseTipsDialog(
    exerciseName: String,
    repository: AcademiauRepository,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val key = exerciseName.lowercase(Locale.ROOT).trim()

    // 1. Get metadata from mapping
    val gifPath = repository.getExerciseGifPath(exerciseName)
    val metadata = repository.exerciseGifMap.values.find { meta ->
        val namePtMatch = meta.name_pt?.lowercase(Locale.ROOT)?.trim() == key
        val nameEnMatch = meta.name.lowercase(Locale.ROOT).trim() == key
        namePtMatch || nameEnMatch
    } ?: repository.exerciseGifMap[exerciseName.lowercase(Locale.ROOT).trim() + ".gif"]

    // 2. Parse Portuguese execution tips
    val ptTips = remember(exerciseName) {
        val lower = key
        when {
            lower.contains("supino") || lower.contains("bench press") || lower.contains("chest press") || 
            lower.contains("fly") || lower.contains("push-up") || lower.contains("push up") || 
            lower.contains("crossover") || lower.contains("cross-over") -> listOf(
                "Deite-se no banco mantendo os pés firmes no chão.",
                "Desça o peso devagar até a linha do peito.",
                "Empurre a barra/halteres estendendo os braços verticalmente.",
                "Mantenha as escápulas retraídas (ombros colados atrás)."
            )
            lower.contains("desenvolvimento") || lower.contains("overhead press") || lower.contains("shoulder press") -> listOf(
                "Mantenha o abdômen contraído para proteger a coluna.",
                "Suba o peso estendendo os braços verticalmente sem travar os cotovelos.",
                "Desça o peso controladamente até a altura do queixo."
            )
            lower.contains("elevação lateral") || lower.contains("elevacao lateral") || lower.contains("lateral raise") || 
            lower.contains("rear delt") || lower.contains("elevação frontal") || lower.contains("elevacao frontal") || 
            lower.contains("front raise") -> listOf(
                "Fique de pé com os joelhos levemente flexionados.",
                "Suba os braços para as laterais até a linha do ombro.",
                "Evite projetar os braços muito para trás ou balançar o corpo."
            )
            lower.contains("tríceps") || lower.contains("triceps") || lower.contains("kickback") || 
            lower.contains("testa") || lower.contains("coice") -> listOf(
                "Mantenha os cotovelos colados ao tronco e totalmente imóveis.",
                "Estenda completamente o antebraço contraindo o tríceps.",
                "Retorne à posição inicial controlando o peso."
            )
            lower.contains("puxada") || lower.contains("pulldown") || lower.contains("pull down") || lower.contains("lat pull") -> listOf(
                "Puxe a barra em direção ao peitoral, inclinando o tronco levemente para trás.",
                "Concentre a força nos cotovelos e contraia as costas.",
                "Suba a barra controladamente até estender as costas."
            )
            lower.contains("remada") || lower.contains("row") || lower.contains("deadlift") || 
            lower.contains("levantamento terra") || lower.contains("encolhimento") || lower.contains("shrug") -> listOf(
                "Mantenha o peitoral aberto e a coluna totalmente reta.",
                "Puxe em direção ao abdômen apertando as costas atrás.",
                "Retorne controladamente o movimento na descida."
            )
            lower.contains("rosca") || lower.contains("curl") || lower.contains("martelo") || lower.contains("hammer") -> listOf(
                "Mantenha os cotovelos fixos ao lado do corpo.",
                "Suba o peso flexionando os antebraços em direção aos ombros.",
                "Evite balançar os ombros ou usar impulso do quadril."
            )
            lower.contains("agachamento") || lower.contains("squat") || lower.contains("leg press") || 
            lower.contains("afundo") || lower.contains("lunge") || lower.contains("búlgaro") || 
            lower.contains("bulgaro") || lower.contains("stiff") || lower.contains("hip thrust") || 
            lower.contains("adductor") || lower.contains("abductor") -> listOf(
                "Mantenha os pés afastados na largura dos ombros.",
                "Desça projetando o quadril para trás como se fosse sentar.",
                "Mantenha o peito aberto e joelhos alinhados com as pontas dos pés."
            )
            lower.contains("panturrilha") || lower.contains("calf") || lower.contains("calves") -> listOf(
                "Suba na ponta dos pés o máximo possível contraindo a panturrilha.",
                "Desça alongando o músculo abaixo do nível de suporte.",
                "Realize o movimento de forma lenta e controlada."
            )
            else -> listOf(
                "Mantenha a postura e a coluna alinhada.",
                "Realize o movimento de forma lenta e controlada.",
                "Concentre a mente na contração do músculo-alvo."
            )
        }
    }

    // Coil ImageLoader config for GIFs
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

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = exerciseName,
                        style = Typography.titleLarge,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Text(text = "✕", color = TextSecondary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // GIF display (Compact and Proportional)
                    if (gifPath != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BgCard)
                                .border(1.dp, BorderColor, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = gifPath,
                                    imageLoader = imageLoader
                                ),
                                contentDescription = exerciseName,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize().padding(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Metadata badges
                    if (metadata != null) {
                        Text(
                            text = "FOCO & EQUIPAMENTO",
                            style = Typography.labelMedium,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            metadata.target?.let { t ->
                                Box(
                                    modifier = Modifier
                                        .background(ColorAccent.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                        .border(1.dp, ColorAccent.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = t.uppercase(Locale.ROOT),
                                        style = Typography.labelMedium,
                                        color = ColorAccent
                                    )
                                }
                            }
                            metadata.equipment?.let { eq ->
                                Box(
                                    modifier = Modifier
                                        .background(BgCard, RoundedCornerShape(4.dp))
                                        .border(1.dp, BorderColor, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = eq.uppercase(Locale.ROOT),
                                        style = Typography.labelMedium,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Portuguese execution tips
                    Text(
                        text = "DICAS DE EXECUÇÃO",
                        style = Typography.labelMedium,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ptTips.forEachIndexed { i, tip ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "• ",
                                color = ColorAccent,
                                style = Typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = tip,
                                color = TextSecondary,
                                style = Typography.bodyMedium
                            )
                        }
                    }

                    // English instructions steps
                    val enSteps = metadata?.instruction_steps_en
                    if (!enSteps.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "PASSO A PASSO (INGLÊS)",
                            style = Typography.labelMedium,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        enSteps.forEachIndexed { idx, step ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${idx + 1}. ",
                                    color = ColorAccent,
                                    style = Typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = step,
                                    color = TextSecondary,
                                    style = Typography.bodyMedium
                                )
                            }
                        }
                    } else if (!metadata?.instructions.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "PASSO A PASSO (INGLÊS)",
                            style = Typography.labelMedium,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = metadata?.instructions ?: "",
                            color = TextSecondary,
                            style = Typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Footer button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = ColorAccent),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Entendido", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SubstituteExerciseDialog(
    exerciseNameToReplace: String,
    category: String,
    repository: AcademiauRepository,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Find the current exercise target muscle
    val currentExercise = repository.exercises.find { it.name.lowercase(Locale.ROOT).trim() == exerciseNameToReplace.lowercase(Locale.ROOT).trim() }
    val currentTarget = currentExercise?.target
    
    val currentList = repository.getRoutineList(category)

    // Filter alternatives: same category, not already in routine
    val alternatives = remember(category) {
        repository.exercises
            .filter { it.category == category && !currentList.contains(it.name) }
            .sortedWith { a, b ->
                // Sort same target muscle first
                val aSame = a.target == currentTarget
                val bSame = b.target == currentTarget
                when {
                    aSame && !bSame -> -1
                    !aSame && bSame -> 1
                    else -> a.name.compareTo(b.name)
                }
            }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Substituir Exercício",
                    style = Typography.titleLarge,
                    color = TextPrimary
                )
                Text(
                    text = "Substituindo: $exerciseNameToReplace",
                    style = Typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (alternatives.isEmpty()) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Nenhum exercício alternativo disponível", color = TextMuted)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(alternatives) { ex ->
                            val isSameFocus = ex.target == currentTarget
                            val focusLabel = if (isSameFocus) "[Mesmo Foco - ${ex.target.replaceFirstChar { it.uppercase() }}]" else "[Alternativa]"
                            val focusColor = if (isSameFocus) ColorSuccess else TextMuted
                            
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BgCard, RoundedCornerShape(8.dp))
                                    .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                                    .clickable {
                                        onConfirm(ex.name)
                                    }
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = ex.name,
                                    style = Typography.bodyLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$focusLabel (${ex.target.replaceFirstChar { it.uppercase() }})",
                                    style = Typography.bodyMedium,
                                    color = focusColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AFFFFFF)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cancelar", color = TextPrimary)
                }
            }
        }
    }
}

@Composable
fun AddExerciseToRoutineDialog(
    category: String,
    repository: AcademiauRepository,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val currentList = repository.getRoutineList(category)

    val available = remember(category) {
        repository.exercises
            .filter { it.category == category && !currentList.contains(it.name) }
            .sortedBy { it.name }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Adicionar ao Treino",
                    style = Typography.titleLarge,
                    color = TextPrimary
                )
                Text(
                    text = "Escolha um movimento para incluir na rotina ativa",
                    style = Typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (available.isEmpty()) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Nenhum exercício disponível para adicionar", color = TextMuted)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(available) { ex ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BgCard, RoundedCornerShape(8.dp))
                                    .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                                    .clickable {
                                        onConfirm(ex.name)
                                    }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ex.name,
                                        style = Typography.bodyLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Foco: ${ex.target.replaceFirstChar { it.uppercase() }}",
                                        style = Typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                                Text(
                                    text = "+",
                                    color = ColorAccent,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AFFFFFF)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cancelar", color = TextPrimary)
                }
            }
        }
    }
}

@Composable
fun CreateExerciseDialog(
    repository: AcademiauRepository,
    onSave: (Exercise) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Push") }
    
    val categories = listOf("Push", "Pull", "Legs")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Criar Exercício",
                    style = Typography.titleLarge,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Name Input
                Text(
                    text = "Nome do Exercício",
                    style = Typography.labelMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(text = "Ex: Supino Reto com Halteres", color = TextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = ColorAccent,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = BgCard,
                        unfocusedContainerColor = BgCard
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Selector
                Text(
                    text = "Treino Relacionado (Categoria)",
                    style = Typography.labelMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { cat ->
                        val isSelected = category == cat
                        val color = when (cat) {
                            "Push" -> ColorPush
                            "Pull" -> ColorPull
                            else -> ColorLegs
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) color.copy(alpha = 0.2f) else BgCard)
                                .border(
                                    1.dp,
                                    if (isSelected) color else BorderColor,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { category = cat }
                        ) {
                            Text(
                                text = cat,
                                style = Typography.bodyMedium,
                                color = if (isSelected) color else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Actions
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AFFFFFF)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancelar", color = TextPrimary)
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                val newEx = repository.addCustomExercise(name.trim(), category)
                                onSave(newEx)
                            }
                        },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorAccent,
                            disabledContainerColor = ColorAccent.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Salvar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
