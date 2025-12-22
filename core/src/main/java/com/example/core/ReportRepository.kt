package com.example.core

import android.content.Context
import kotlinx.coroutines.flow.Flow

class ReportRepository(context: Context) {

    private val dao = AppDatabase.get(context).reportDao()

    fun getReports(): Flow<List<ReportEntity>> =
        dao.getAll()

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
