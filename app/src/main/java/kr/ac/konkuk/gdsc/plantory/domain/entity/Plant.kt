package kr.ac.konkuk.gdsc.plantory.domain.entity

data class Plant(
    val id: Long,
    val imageUrl: String?,
    val nickname: String,
    val shortDescription: String,
    val birthDate: String,
    val name: String,
)