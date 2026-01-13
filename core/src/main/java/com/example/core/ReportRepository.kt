package com.example.core

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class ReportRepository(context: Context) {

    // Initialize the DAO lazily so constructing the repository doesn't perform DB I/O on the caller thread
    private val dao by lazy { AppDatabase.get(context).reportDao() }

    fun getReports(): Flow<List<ReportEntity>> =
        dao.getAll().flowOn(Dispatchers.IO)

    suspend fun addReport(report: Report) {
        dao.insert(
            ReportEntity(
                id = report.id,
                title = report.title,
                description = report.description,
                latitude = report.latitude,
                longitude = report.longitude
            )
        )
    }
}
