package com.academiau.app.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.Locale

class AcademiauRepository(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("academiau_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val defaultExercises = listOf(
        // Push - Principais
        Exercise("push_1", "Supino reto com barra", "Push", "peito"),
        Exercise("push_2", "Supino inclinado com halteres", "Push", "peito"),
        Exercise("push_3", "Crucifixo na máquina", "Push", "peito"),
        Exercise("push_4", "Elevação lateral com halteres", "Push", "ombros"),
        Exercise("push_5", "Remada alta na polia", "Push", "ombros"),
        Exercise("push_6", "Extensão de tríceps na polia overhead", "Push", "triceps"),
        Exercise("push_7", "Tríceps coice na polia", "Push", "triceps"),
        Exercise("push_8", "Tríceps corda", "Push", "triceps"),
        // Push - Substitutos/Extras
        Exercise("push_sub_1", "Tríceps pulley na polia alta", "Push", "triceps"),
        Exercise("push_sub_2", "Pullover com halteres", "Push", "peito"),
        Exercise("push_sub_3", "Desenvolvimento com halteres", "Push", "ombros"),
        Exercise("push_sub_4", "Desenvolvimento máquina", "Push", "ombros"),
        Exercise("push_sub_5", "Crucifixo na polia", "Push", "peito"),
        Exercise("push_sub_6", "Crucifixo inclinado com halteres", "Push", "peito"),

        // Pull - Principais
        Exercise("pull_1", "Puxada aberta frontal", "Pull", "costas"),
        Exercise("pull_2", "Remada unilateral no cabo sentado", "Pull", "costas"),
        Exercise("pull_3", "Remada aberta na máquina", "Pull", "costas"),
        Exercise("pull_4", "Remada unilateral com halteres", "Pull", "costas"),
        Exercise("pull_5", "Voador inverso", "Pull", "ombros"),
        Exercise("pull_6", "Rosca direta inclinada com halteres", "Pull", "biceps"),
        Exercise("pull_7", "Rosca scott na máquina", "Pull", "biceps"),
        Exercise("pull_8", "Rosca martelo na polia baixa", "Pull", "biceps"),
        // Pull - Substitutos/Extras
        Exercise("pull_sub_1", "Rosca direta na polia", "Pull", "biceps"),
        Exercise("pull_sub_2", "Face pull", "Pull", "ombros"),
        Exercise("pull_sub_3", "Remada curvada com barra", "Pull", "costas"),
        Exercise("pull_sub_4", "Rosca concentrada com halteres", "Pull", "biceps"),
        Exercise("pull_sub_5", "Barra fixa pronada", "Pull", "costas"),
        Exercise("pull_sub_6", "Rosca martelo com halteres", "Pull", "biceps"),
        Exercise("pull_sub_7", "Rosca martelo alternada com halteres", "Pull", "biceps"),
        Exercise("pull_sub_8", "Puxada alta com braços estendidos", "Pull", "costas"),
        Exercise("pull_sub_9", "Remada cavalinho na máquina", "Pull", "costas"),
        Exercise("pull_sub_10", "Rosca scott unilateral com halteres", "Pull", "biceps"),
        Exercise("pull_sub_11", "Remada na polia sentado", "Pull", "costas"),

        // Legs - Principais
        Exercise("legs_1", "Agachamento na máquina smith", "Legs", "quadriceps"),
        Exercise("legs_2", "Extensão de perna unilateral", "Legs", "quadriceps"),
        Exercise("legs_3", "Flexão de perna deitado", "Legs", "posteriores"),
        Exercise("legs_4", "Afundo com halteres", "Legs", "quadriceps"),
        Exercise("legs_5", "Panturrilha na máquina em pé", "Legs", "panturrilha"),
        Exercise("legs_6", "Abdominais", "Legs", "geral"),
        Exercise("legs_7", "Flexão de pernas sentado", "Legs", "posteriores"),
        // Legs - Substitutos/Extras
        Exercise("legs_sub_1", "Levantamento stiff com barra", "Legs", "posteriores"),
        Exercise("legs_sub_2", "Extensão de pernas", "Legs", "quadriceps"),
        Exercise("legs_sub_3", "Cadeira adutora", "Legs", "quadriceps"),
        Exercise("legs_sub_4", "Cadeira abdutora", "Legs", "posteriores")
    )

    private val defaultRoutines = Routine(
        push = listOf("Supino reto com barra", "Supino inclinado com halteres", "Crucifixo na máquina", "Elevação lateral com halteres", "Remada alta na polia", "Extensão de tríceps na polia overhead", "Tríceps coice na polia", "Tríceps corda"),
        pull = listOf("Puxada aberta frontal", "Remada unilateral no cabo sentado", "Remada aberta na máquina", "Remada unilateral com halteres", "Voador inverso", "Rosca direta inclinada com halteres", "Rosca scott na máquina", "Rosca martelo na polia baixa"),
        legs = listOf("Agachamento na máquina smith", "Extensão de perna unilateral", "Flexão de perna deitado", "Afundo com halteres", "Panturrilha na máquina em pé", "Abdominais", "Flexão de pernas sentado")
    )

    var exercises: MutableList<Exercise> = mutableListOf()
    var routines: Routine = Routine(emptyList(), emptyList(), emptyList())
    var lastCompletedWorkout: String = "Legs"
    var history: MutableList<WorkoutSession> = mutableListOf()
    var lastWeights: MutableMap<String, List<SetRecord>> = mutableMapOf()

    var exerciseGifMap: Map<String, ExerciseMetadata> = emptyMap()
    var exerciseGifMapPt: Map<String, String> = emptyMap()

    init {
        loadData()
        loadGifMetadata()
    }

    private fun loadData() {
        val currentVersion = 7
        val savedVersion = prefs.getInt("academiau_db_version", 0)

        val savedExercisesJson = prefs.getString("academiau_exercises", null)
        val savedRoutinesJson = prefs.getString("academiau_routines", null)

        if (savedExercisesJson != null && savedVersion == currentVersion) {
            val type = object : TypeToken<MutableList<Exercise>>() {}.type
            exercises = gson.fromJson(savedExercisesJson, type)
        } else {
            exercises = defaultExercises.toMutableList()
            saveExercises()
            prefs.edit().putInt("academiau_db_version", currentVersion).apply()
        }

        if (savedRoutinesJson != null && savedVersion == currentVersion) {
            routines = gson.fromJson(savedRoutinesJson, Routine::class.java)
        } else {
            routines = defaultRoutines
            saveRoutines()
        }

        lastCompletedWorkout = prefs.getString("academiau_last_completed", "Legs") ?: "Legs"

        val savedHistoryJson = prefs.getString("academiau_history", null)
        if (savedHistoryJson != null) {
            val type = object : TypeToken<MutableList<WorkoutSession>>() {}.type
            history = gson.fromJson(savedHistoryJson, type)
        }

        val savedWeightsJson = prefs.getString("academiau_last_weights", null)
        if (savedWeightsJson != null) {
            val type = object : TypeToken<MutableMap<String, List<SetRecord>>>() {}.type
            lastWeights = gson.fromJson(savedWeightsJson, type)
        }
    }

    private fun loadGifMetadata() {
        try {
            val jsonString = context.assets.open("mapping_metadata.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<Map<String, ExerciseMetadata>>() {}.type
            val data: Map<String, ExerciseMetadata> = gson.fromJson(jsonString, type)
            exerciseGifMap = data

            val tempPtMap = mutableMapOf<String, String>()
            data.forEach { (gifName, meta) ->
                meta.name_pt?.let { ptName ->
                    tempPtMap[ptName.lowercase(Locale.ROOT).trim()] = gifName
                }
            }
            exerciseGifMapPt = tempPtMap

            val existingNames = exercises.map { it.name.lowercase(Locale.ROOT).trim() }.toSet()
            var updated = false

            data.values.forEach { meta ->
                val cleanName = (meta.name_pt ?: meta.name).lowercase(Locale.ROOT).trim()
                if (!existingNames.contains(cleanName)) {
                    val bp = (meta.body_part ?: meta.category ?: "").lowercase(Locale.ROOT)
                    val targetMuscle = (meta.target ?: "").lowercase(Locale.ROOT)

                    // Map Category (Considering Portuguese or English from JSON)
                    val category = when {
                        bp.contains("back") || bp.contains("costas") || bp.contains("cardio") || 
                        targetMuscle.contains("biceps") || targetMuscle.contains("bíceps") -> "Pull"
                        
                        bp.contains("leg") || bp.contains("perna") || bp.contains("waist") || bp.contains("cintura") ||
                        targetMuscle.contains("abs") || targetMuscle.contains("glute") || targetMuscle.contains("quad") -> "Legs"
                        
                        else -> "Push"
                    }

                    // Map Target Muscle
                    var target = when (category) {
                        "Pull" -> "costas"
                        "Legs" -> "quadriceps"
                        else -> "peito"
                    }

                    if (targetMuscle.isNotEmpty()) {
                        target = when {
                            targetMuscle.contains("peito") || targetMuscle.contains("pectoral") -> "peito"
                            targetMuscle.contains("delts") || targetMuscle.contains("shoulder") || targetMuscle.contains("ombro") -> "ombros"
                            targetMuscle.contains("triceps") || targetMuscle.contains("tríceps") -> "triceps"
                            targetMuscle.contains("biceps") || targetMuscle.contains("bíceps") -> "biceps"
                            targetMuscle.contains("costas") || targetMuscle.contains("back") || targetMuscle.contains("lats") -> "costas"
                            targetMuscle.contains("quad") || targetMuscle.contains("perna") -> "quadriceps"
                            targetMuscle.contains("glute") || targetMuscle.contains("posterior") || targetMuscle.contains("hamstring") -> "posteriores"
                            targetMuscle.contains("calf") || targetMuscle.contains("panturrilha") -> "panturrilha"
                            else -> target
                        }
                    }

                    exercises.add(Exercise("db_${meta.id}", meta.name_pt ?: meta.name, category, target))
                    updated = true
                }
            }

            if (updated) saveExercises()
        } catch (e: IOException) { e.printStackTrace() }
    }

    fun saveExercises() {
        prefs.edit().putString("academiau_exercises", gson.toJson(exercises)).apply()
    }

    fun saveRoutines() {
        prefs.edit().putString("academiau_routines", gson.toJson(routines)).apply()
    }

    fun saveLastCompletedWorkout(split: String) {
        lastCompletedWorkout = split
        prefs.edit().putString("academiau_last_completed", split).apply()
    }

    fun addWorkoutToHistory(type: String, durationMillis: Long, exercisesInSession: List<ExerciseSession>) {
        val session = WorkoutSession(
            id = "session_${System.currentTimeMillis()}",
            type = type,
            durationMillis = durationMillis,
            timestamp = System.currentTimeMillis(),
            exercises = exercisesInSession
        )
        history.add(0, session)
        if (history.size > 50) history.removeAt(history.size - 1)
        saveHistory()
    }

    fun deleteWorkoutFromHistory(id: String) {
        history.removeAll { it.id == id }
        saveHistory()
    }

    private fun saveHistory() {
        prefs.edit().putString("academiau_history", gson.toJson(history)).apply()
    }

    fun setAsNextWorkout(split: String) {
        val previous = when (split) {
            "Push" -> "Legs"
            "Pull" -> "Push"
            "Legs" -> "Pull"
            else -> "Legs"
        }
        saveLastCompletedWorkout(previous)
    }

    fun saveWeights(exerciseName: String, sets: List<SetRecord>) {
        lastWeights[exerciseName] = sets
        prefs.edit().putString("academiau_last_weights", gson.toJson(lastWeights)).apply()
    }

    fun getSuggestedWorkout(): String {
        return when (lastCompletedWorkout) {
            "Push" -> "Pull"
            "Pull" -> "Legs"
            else -> "Push"
        }
    }

    fun addCustomExercise(name: String, category: String): Exercise {
        var target = when (category) {
            "Pull" -> "costas"
            "Legs" -> "quadriceps"
            else -> "peito"
        }
        val key = name.lowercase(Locale.ROOT).trim()
        val cleanKey = key.replace(Regex("[()°\\s\\-/',.&+#!]+"), "_").trim { it == '_' }
        val gifName = "$cleanKey.gif"

        exerciseGifMap[gifName]?.let { meta ->
            meta.target?.lowercase(Locale.ROOT)?.let { t ->
                target = when {
                    t.contains("peito") || t.contains("pectoral") -> "peito"
                    t.contains("delts") || t.contains("shoulder") || t.contains("ombro") -> "ombros"
                    t.contains("triceps") || t.contains("tríceps") -> "triceps"
                    t.contains("biceps") || t.contains("bíceps") -> "biceps"
                    t.contains("costas") || t.contains("back") || t.contains("lats") -> "costas"
                    t.contains("quad") || t.contains("perna") -> "quadriceps"
                    t.contains("glute") || t.contains("posterior") || t.contains("hamstring") -> "posteriores"
                    t.contains("calf") || t.contains("panturrilha") -> "panturrilha"
                    else -> target
                }
            }
        }

        val newEx = Exercise("custom_${System.currentTimeMillis()}", name, category, target)
        exercises.add(newEx)
        saveExercises()
        return newEx
    }

    fun deleteExercise(id: String) {
        val exerciseToDelete = exercises.find { it.id == id }
        if (exerciseToDelete != null) {
            exercises.remove(exerciseToDelete)
            saveExercises()
            val name = exerciseToDelete.name
            routines = Routine(
                push = routines.push.filter { it != name },
                pull = routines.pull.filter { it != name },
                legs = routines.legs.filter { it != name }
            )
            saveRoutines()
        }
    }

    fun substituteExercise(category: String, index: Int, newName: String) {
        val list = getRoutineList(category).toMutableList()
        if (index in list.indices) {
            list[index] = newName
            updateRoutineList(category, list)
            saveRoutines()
        }
    }

    fun addExerciseToRoutine(category: String, name: String) {
        val list = getRoutineList(category).toMutableList()
        if (!list.contains(name)) {
            list.add(name)
            updateRoutineList(category, list)
            saveRoutines()
        }
    }

    fun removeExerciseFromRoutine(category: String, index: Int) {
        val list = getRoutineList(category).toMutableList()
        if (index in list.indices) {
            list.removeAt(index)
            updateRoutineList(category, list)
            saveRoutines()
        }
    }

    fun getRoutineList(category: String): List<String> {
        return when (category) {
            "Push" -> routines.push
            "Pull" -> routines.pull
            else -> routines.legs
        }
    }

    private fun updateRoutineList(category: String, newList: List<String>) {
        routines = when (category) {
            "Push" -> routines.copy(push = newList)
            "Pull" -> routines.copy(pull = newList)
            else -> routines.copy(legs = newList)
        }
    }

    fun getExerciseGifPath(name: String): String? {
        val key = name.lowercase(Locale.ROOT).trim()
        
        // 1. Check manual mapping for default exercises
        val manualMapping = mapOf(
            "supino reto com barra" to "barbell_bench_press.gif",
            "supino inclinado com halteres" to "dumbbell_incline_bench_press.gif",
            "crucifixo na máquina" to "lever_seated_fly.gif",
            "elevação lateral com halteres" to "dumbbell_lateral_raise.gif",
            "remada alta na polia" to "cable_upright_row.gif",
            "extensão de tríceps na polia overhead" to "cable_high_pulley_overhead_tricep_extension.gif",
            "tríceps coice na polia" to "cable_kickback.gif",
            "tríceps corda" to "cable_pushdown_with_rope_attachment.gif",
            "tríceps pulley na polia alta" to "cable_pushdown.gif",
            "pullover com halteres" to "dumbbell_pullover.gif",
            "desenvolvimento com halteres" to "dumbbell_seated_shoulder_press.gif",
            "desenvolvimento máquina" to "lever_seated_shoulder_press.gif",
            "crucifixo na polia" to "cable_seated_fly.gif",
            "crucifixo inclinado com halteres" to "dumbbell_incline_fly.gif",
            "puxada aberta frontal" to "cable_lat_pulldown_full_range_of_motion.gif",
            "rosca martelo" to "dumbbell_hammer_curl.gif",
            "extensão de perna unilateral" to "lever_extension.gif",
            "remada unilateral no cabo sentado" to "cable_seated_one_arm_alternate_row.gif",
            "remada aberta na máquina" to "lever_seated_row.gif",
            "remada unilateral com halteres" to "dumbbell_one_arm_bent_over_row.gif",
            "voador inverso" to "lever_seated_reverse_fly.gif",
            "rosca direta inclinada com halteres" to "dumbbell_incline_curl.gif",
            "rosca scott na máquina" to "lever_preacher_curl.gif",
            "rosca martelo na polia baixa" to "cable_hammer_curl.gif",
            "rosca direta na polia" to "cable_bicep_curl.gif",
            "face pull" to "cable_face_pull.gif",
            "remada curvada com barra" to "barbell_bent_over_row.gif",
            "rosca concentrada com halteres" to "dumbbell_concentration_curl.gif",
            "barra fixa pronada" to "pull_up.gif",
            "rosca martelo com halteres" to "dumbbell_hammer_curl.gif",
            "rosca martelo alternada com halteres" to "dumbbell_alternate_hammer_curl.gif",
            "puxada alta com braços estendidos" to "cable_straight_arm_pulldown.gif",
            "remada cavalinho na máquina" to "lever_t_bar_row.gif",
            "rosca scott unilateral com halteres" to "dumbbell_one_arm_preacher_curl.gif",
            "remada na polia sentado" to "cable_seated_row.gif",
            "agachamento na máquina smith" to "smith_full_squat.gif",
            "extensão de perna unilateral" to "lever_extension.gif",
            "flexão de perna deitado" to "lever_lying_leg_curl.gif",
            "afundo com halteres" to "dumbbell_lunge.gif",
            "panturrilha na máquina em pé" to "lever_standing_calf_raise.gif",
            "abdominais" to "sit_up.gif",
            "flexão de pernas sentado" to "lever_seated_leg_curl.gif",
            "levantamento stiff com barra" to "barbell_stiff_leg_deadlift.gif",
            "extensão de pernas" to "lever_extension.gif",
            "cadeira adutora" to "lever_seated_hip_adduction.gif",
            "cadeira abdutora" to "lever_seated_hip_abduction.gif"
        )

        val manualGif = manualMapping[key]
        if (manualGif != null) return "file:///android_asset/gifs/$manualGif"

        // 2. Check exerciseGifMapPt (from JSON metadata)
        val gifFromPt = exerciseGifMapPt[key]
        if (gifFromPt != null) return "file:///android_asset/gifs/$gifFromPt"

        // 3. Heuristic matching
        val cleanKey = key.replace(Regex("[()°\\s\\-/',.&+#!]+"), "_").trim { it == '_' }
        val directName = "$cleanKey.gif"
        if (exerciseGifMap.containsKey(directName)) return "file:///android_asset/gifs/$directName"

        val matchedKey = exerciseGifMap.keys.find { fn ->
            val cleanFn = fn.replace(".gif", "")
            cleanKey.contains(cleanFn) || cleanFn.contains(cleanKey)
        }
        if (matchedKey != null) return "file:///android_asset/gifs/$matchedKey"

        // 4. Broad fallbacks
        return when {
            key.contains("supino") -> "file:///android_asset/gifs/barbell_bench_press.gif"
            key.contains("desenvolvimento") -> "file:///android_asset/gifs/dumbbell_seated_shoulder_press.gif"
            key.contains("elevação lateral") || key.contains("elevacao lateral") -> "file:///android_asset/gifs/dumbbell_lateral_raise.gif"
            key.contains("tríceps") || key.contains("triceps") -> "file:///android_asset/gifs/cable_pushdown.gif"
            key.contains("puxada") -> "file:///android_asset/gifs/cable_lat_pulldown_full_range_of_motion.gif"
            key.contains("remada") -> "file:///android_asset/gifs/barbell_bent_over_row.gif"
            key.contains("rosca") -> "file:///android_asset/gifs/barbell_curl.gif"
            key.contains("agachamento") -> "file:///android_asset/gifs/barbell_full_squat.gif"
            key.contains("leg press") -> "file:///android_asset/gifs/lever_alternate_leg_press.gif"
            key.contains("panturrilha") -> "file:///android_asset/gifs/barbell_standing_calf_raise.gif"
            else -> null
        }
    }
}
