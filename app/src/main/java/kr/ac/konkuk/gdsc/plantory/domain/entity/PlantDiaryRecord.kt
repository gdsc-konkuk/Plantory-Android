package kr.ac.konkuk.gdsc.plantory.domain.entity

data class PlantDiaryRecord(
    val id: Int,
    val imageUrl: String,
    val comment: String,
    val isWatered: Boolean
)