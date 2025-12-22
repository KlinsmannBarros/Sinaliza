package com.example.core

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)

/**
 * Mapper: Room Entity → UI Domain Model
 */
fun ReportEntity.toReport(): Report {
    return Report(
        id = id,
        title = title,
        description = description,
        latitude = latitude,
        longitude = longitude
    )
}
