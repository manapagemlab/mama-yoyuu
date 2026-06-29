package com.example.data

import kotlinx.coroutines.flow.Flow

class DiagnosisRepository(private val diagnosisDao: DiagnosisDao) {
    val allResults: Flow<List<DiagnosisResult>> = diagnosisDao.getAllResults()

    suspend fun insertResult(result: DiagnosisResult) {
        diagnosisDao.insertResult(result)
    }

    suspend fun deleteResultById(id: Int) {
        diagnosisDao.deleteResultById(id)
    }

    suspend fun deleteAllResults() {
        diagnosisDao.deleteAllResults()
    }
}
