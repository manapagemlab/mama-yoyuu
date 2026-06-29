package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "diagnosis_results")
data class DiagnosisResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val totalScore: Int,         // Sum of scores (0-30)
    val percentage: Int,         // 0-100 calculated leeway percentage
    val physicalScore: Int,      // Physical leeway sub-score
    val mentalScore: Int,        // Mental leeway sub-score
    val timeScore: Int,          // Time leeway sub-score
    val supportScore: Int,       // Support leeway sub-score
    val compassionScore: Int,    // Self-Compassion leeway sub-score
    val statusLabel: String,     // e.g. "満タン状態", "ちょっとお疲れ", etc.
    val comment: String          // Advice comment
)

@Dao
interface DiagnosisDao {
    @Query("SELECT * FROM diagnosis_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<DiagnosisResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: DiagnosisResult)

    @Query("DELETE FROM diagnosis_results WHERE id = :id")
    suspend fun deleteResultById(id: Int)

    @Query("DELETE FROM diagnosis_results")
    suspend fun deleteAllResults()
}

@Database(entities = [DiagnosisResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diagnosisDao(): DiagnosisDao
}
