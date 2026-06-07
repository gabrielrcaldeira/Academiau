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
        // Push
        Exercise("push_1", "Supino reto com barra", "Push", "peito", 4, 10),
        Exercise("push_2", "Supino inclinado com halteres", "Push", "peito", 3, 12),
        Exercise("push_3", "Desenvolvimento de ombro sentado com barra", "Push", "ombros", 3, 10),
        Exercise("push_4", "Elevação lateral com halteres", "Push", "ombros", 4, 15),
        Exercise("push_5", "Tríceps na polia com barra V", "Push", "triceps", 4, 12),
        Exercise("push_6", "Supino reto com halteres", "Push", "peito", 4, 10),
        Exercise("push_7", "Crucifixo reto com halteres", "Push", "peito", 3, 12),
        Exercise("push_8", "Crossover na polia alta", "Push", "peito", 3, 12),
        Exercise("push_9", "Tríceps testa com halteres", "Push", "triceps", 3, 10),
        Exercise("push_10", "Tríceps testa com barra", "Push", "triceps", 3, 10),
        Exercise("push_11", "Tríceps coice na polia", "Push", "triceps", 3, 12),
        Exercise("push_12", "Desenvolvimento de ombro sentado com halteres", "Push", "ombros", 3, 10),
        Exercise("push_13", "Elevação frontal com halteres", "Push", "ombros", 3, 12),
        Exercise("push_14", "Flexão de braço", "Push", "peito", 3, 15),

        // Pull
        Exercise("pull_1", "Puxada alta na polia com amplitude máxima", "Pull", "costas", 4, 12),
        Exercise("pull_2", "Remada curvada com barra", "Pull", "costas", 4, 10),
        Exercise("pull_3", "Remada sentado na polia", "Pull", "costas", 3, 12),
        Exercise("pull_4", "Rosca direta com barra", "Pull", "biceps", 3, 10),
        Exercise("pull_5", "Rosca martelo com halteres", "Pull", "biceps", 3, 12),
        Exercise("pull_6", "Puxada alta pegada fechada na polia", "Pull", "costas", 4, 12),
        Exercise("pull_7", "Pull down na polia alta", "Pull", "costas", 3, 12),
        Exercise("pull_8", "Levantamento terra com barra", "Pull", "costas", 3, 8),
        Exercise("pull_9", "Rosca bíceps alternada com halteres", "Pull", "biceps", 3, 10),
        Exercise("pull_10", "Rosca concentrada com halteres", "Pull", "biceps", 3, 12),
        Exercise("pull_11", "Rosca inversa com barra", "Pull", "biceps", 3, 12),
        Exercise("pull_12", "Crucifixo invertido com halteres", "Pull", "ombros", 3, 12),
        Exercise("pull_13", "Encolhimento de ombro com halteres", "Pull", "costas", 4, 15),

        // Legs
        Exercise("legs_1", "Agachamento livre com barra", "Legs", "quadriceps", 4, 8),
        Exercise("legs_2", "Leg press unilateral articulado", "Legs", "quadriceps", 4, 10),
        Exercise("legs_3", "Cadeira extensora articulada", "Legs", "quadriceps", 3, 12),
        Exercise("legs_4", "Mesa flexora deitada articulada", "Legs", "posteriores", 3, 12),
        Exercise("legs_5", "Panturrilha em pé com barra", "Legs", "panturrilha", 4, 15),
        Exercise("legs_6", "Afundo com halteres", "Legs", "quadriceps", 3, 10),
        Exercise("legs_7", "Agachamento búlgaro unilateral com elástico", "Legs", "quadriceps", 3, 10),
        Exercise("legs_8", "Stiff/levantamento terra romeno", "Legs", "posteriores", 3, 10),
        Exercise("legs_9", "Stiff com halteres", "Legs", "posteriores", 3, 10),
        Exercise("legs_10", "Elevação pélvica de joelhos com elástico", "Legs", "posteriores", 3, 10),
        Exercise("legs_11", "Cadeira adutora articulada", "Legs", "quadriceps", 3, 12),
        Exercise("legs_12", "Cadeira abdutora articulada", "Legs", "posteriores", 3, 12),
        Exercise("legs_13", "Panturrilha sentado com barra", "Legs", "panturrilha", 4, 15)
    )

    private val defaultRoutines = Routine(
        push = listOf("Supino reto com barra", "Supino inclinado com halteres", "Desenvolvimento de ombro sentado com barra", "Elevação lateral com halteres", "Tríceps na polia com barra V"),
        pull = listOf("Puxada alta na polia com amplitude máxima", "Remada curvada com barra", "Remada sentado na polia", "Rosca direta com barra", "Rosca martelo com halteres"),
        legs = listOf("Agachamento livre com barra", "Leg press unilateral articulado", "Cadeira extensora articulada", "Mesa flexora deitada articulada", "Panturrilha em pé com barra")
    )

    var exercises: MutableList<Exercise> = mutableListOf()
    var routines: Routine = Routine(emptyList(), emptyList(), emptyList())
    var lastCompletedWorkout: String = "Legs"

    // Metadata GIF maps
    var exerciseGifMap: Map<String, ExerciseMetadata> = emptyMap()
    var exerciseGifMapPt: Map<String, String> = emptyMap() // lowercased pt name -> gifName

    init {
        loadData()
        loadGifMetadata()
    }

    private fun loadData() {
        val currentVersion = 5
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
    }

    private fun loadGifMetadata() {
        try {
            val jsonString = context.assets.open("mapping_metadata.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<Map<String, ExerciseMetadata>>() {}.type
            val data: Map<String, ExerciseMetadata> = gson.fromJson(jsonString, type)
            exerciseGifMap = data

            // Build Portuguese lookup map
            val tempPtMap = mutableMapOf<String, String>()
            data.forEach { (gifName, meta) ->
                meta.name_pt?.let { ptName ->
                    tempPtMap[ptName.lowercase(Locale.ROOT).trim()] = gifName
                }
            }
            exerciseGifMapPt = tempPtMap

            // Merge metadata exercises into active database if they are not already there
            val existingNames = exercises.map { it.name.lowercase(Locale.ROOT).trim() }.toSet()
            var updated = false

            data.values.forEach { meta ->
                val cleanName = (meta.name_pt ?: meta.name).lowercase(Locale.ROOT).trim()
                if (!existingNames.contains(cleanName)) {
                    val bp = meta.category?.lowercase(Locale.ROOT) ?: ""
                    val targetMuscle = meta.target?.lowercase(Locale.ROOT) ?: ""

                    // Map Category
                    val category = when {
                        bp.contains("back") || bp.contains("cardio") || bp.contains("neck") || 
                        targetMuscle.contains("biceps") || targetMuscle.contains("forearm") -> "Pull"
                        bp.contains("leg") || bp.contains("calves") || bp.contains("waist") || 
                        targetMuscle.contains("abs") || targetMuscle.contains("calf") || targetMuscle.contains("glute") -> "Legs"
                        bp.contains("arms") && targetMuscle.contains("triceps") -> "Push"
                        bp.contains("arms") && (targetMuscle.contains("biceps") || targetMuscle.contains("forearm")) -> "Pull"
                        else -> "Push"
                    }

                    // Map Target Muscle to Portuguese Tag
                    var target = when (category) {
                        "Pull" -> "costas"
                        "Legs" -> "quadriceps"
                        else -> "peito"
                    }

                    if (targetMuscle.isNotEmpty()) {
                        target = when {
                            targetMuscle.contains("peito") || targetMuscle.contains("pectoral") -> "peito"
                            targetMuscle.contains("delts") || targetMuscle.contains("shoulder") || targetMuscle.contains("deltoid") -> "ombros"
                            targetMuscle.contains("triceps") -> "triceps"
                            targetMuscle.contains("biceps") -> "biceps"
                            targetMuscle.contains("lats") || targetMuscle.contains("back") || targetMuscle.contains("spine") || targetMuscle.contains("traps") || targetMuscle.contains("trapezius") -> "costas"
                            targetMuscle.contains("quads") || targetMuscle.contains("quadriceps") || targetMuscle.contains("thigh") -> "quadriceps"
                            targetMuscle.contains("glute") || targetMuscle.contains("hamstring") || targetMuscle.contains("posterior") -> "posteriores"
                            targetMuscle.contains("calf") || targetMuscle.contains("calves") || targetMuscle.contains("soleus") || targetMuscle.contains("gastrocnemius") -> "panturrilha"
                            else -> target
                        }
                    }

                    exercises.add(
                        Exercise(
                            id = "db_${meta.id}",
                            name = meta.name_pt ?: meta.name,
                            category = category,
                            target = target,
                            defaultSets = 3,
                            defaultReps = 12
                        )
                    )
                    updated = true
                }
            }

            if (updated) {
                saveExercises()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
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

    fun getSuggestedWorkout(): String {
        return when (lastCompletedWorkout) {
            "Push" -> "Pull"
            "Pull" -> "Legs"
            else -> "Push"
        }
    }

    fun addCustomExercise(name: String, category: String): Exercise {
        // Find target muscle
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
                    t.contains("delts") || t.contains("shoulder") || t.contains("deltoid") -> "ombros"
                    t.contains("triceps") -> "triceps"
                    t.contains("biceps") -> "biceps"
                    t.contains("lats") || t.contains("back") || t.contains("spine") || t.contains("traps") || t.contains("trapezius") -> "costas"
                    t.contains("quads") || t.contains("quadriceps") || t.contains("thigh") -> "quadriceps"
                    t.contains("glute") || t.contains("hamstring") || t.contains("posterior") -> "posteriores"
                    t.contains("calf") || t.contains("calves") || t.contains("soleus") || t.contains("gastrocnemius") -> "panturrilha"
                    else -> target
                }
            }
        }

        val newEx = Exercise(
            id = "custom_${System.currentTimeMillis()}",
            name = name,
            category = category,
            target = target,
            defaultSets = 3,
            defaultReps = 12
        )

        exercises.add(newEx)
        saveExercises()
        return newEx
    }

    fun deleteExercise(id: String) {
        val exerciseToDelete = exercises.find { it.id == id }
        if (exerciseToDelete != null) {
            exercises.remove(exerciseToDelete)
            saveExercises()

            // Remove from routines as well
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

        // 1. Try Portuguese lookup map
        val gifFromPt = exerciseGifMapPt[key]
        if (gifFromPt != null) return "file:///android_asset/gifs/$gifFromPt"

        // 2. Direct map check
        val cleanKey = key.replace(Regex("[()°\\s\\-/',.&+#!]+"), "_").trim { it == '_' }
        val directName = "$cleanKey.gif"
        if (exerciseGifMap.containsKey(directName)) return "file:///android_asset/gifs/$directName"

        // 3. Clean search match
        val matchedKey = exerciseGifMap.keys.find { fn ->
            val cleanFn = fn.replace(".gif", "")
            cleanKey.contains(cleanFn) || cleanFn.contains(cleanKey)
        }
        if (matchedKey != null) return "file:///android_asset/gifs/$matchedKey"

        // 4. Keywords fallbacks (same as app.js)
        return when {
            key.contains("supino") -> "file:///android_asset/gifs/barbell_bench_press.gif"
            key.contains("desenvolvimento") -> "file:///android_asset/gifs/dumbbell_seated_shoulder_press.gif"
            key.contains("elevação lateral") || key.contains("elevacao lateral") -> "file:///android_asset/gifs/dumbbell_lateral_raise.gif"
            key.contains("tríceps polia") || key.contains("triceps polia") -> "file:///android_asset/gifs/cable_triceps_pushdown_v_bar.gif"
            key.contains("tríceps coice") || key.contains("triceps coice") -> "file:///android_asset/gifs/cable_kickback.gif"
            key.contains("puxada alta") -> "file:///android_asset/gifs/cable_lat_pulldown_full_range_of_motion.gif"
            key.contains("remada") -> "file:///android_asset/gifs/barbell_bent_over_row.gif"
            key.contains("rosca") -> "file:///android_asset/gifs/barbell_curl.gif"
            key.contains("agachamento") -> "file:///android_asset/gifs/barbell_full_squat.gif"
            key.contains("leg press") -> "file:///android_asset/gifs/lever_alternate_leg_press.gif"
            key.contains("panturrilha") -> "file:///android_asset/gifs/barbell_standing_calf_raise.gif"
            else -> null
        }
    }
}
