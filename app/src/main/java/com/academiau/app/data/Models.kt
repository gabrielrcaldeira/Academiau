package com.academiau.app.data

data class Exercise(
    val id: String,
    val name: String,
    val category: String, // Push, Pull, Legs
    val target: String,   // peito, costas, biceps, triceps, ombros, quadriceps, posteriores, panturrilha, geral
    val defaultSets: Int = 3,
    val defaultReps: Int = 12
)

data class ExerciseMetadata(
    val id: String,
    val name: String,
    val category: String?,
    val target: String?,
    val instructions: String?,
    val equipment: String?,
    val muscle_group: String?,
    val secondary_muscles: List<String>?,
    val name_pt: String?,
    val body_part: String?,
    val instruction_steps_en: List<String>?
)

data class Routine(
    val push: List<String>,
    val pull: List<String>,
    val legs: List<String>
)

data class SetRecord(
    val weight: Double,
    val reps: Int
)

data class ExerciseSession(
    val exerciseName: String,
    val sets: List<SetRecord>
)

data class WorkoutSession(
    val id: String,
    val type: String, // Push, Pull, Legs
    val durationMillis: Long,
    val timestamp: Long,
    val exercises: List<ExerciseSession> = emptyList()
)
