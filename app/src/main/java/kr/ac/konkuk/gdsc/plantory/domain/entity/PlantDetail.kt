package kr.ac.konkuk.gdsc.plantory.domain.entity

data class PlantDetail(
    val id: Long,
    val dday: Int,
    val nickname: String,
    val species: String,
    val description: String,
    val createdAt: String
)
