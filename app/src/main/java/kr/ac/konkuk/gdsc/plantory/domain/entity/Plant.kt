package kr.ac.konkuk.gdsc.plantory.domain.entity

data class Plant(
    val id: Int,
    val imageUrl: String?,
    val nickname: String,
    val shortDescription: String,
    val birthDate: String,
    val name: String,
)
