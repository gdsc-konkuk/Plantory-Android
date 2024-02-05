package kr.ac.konkuk.gdsc.plantory.domain.entity

data class Plant(
    val birthDate: String,
    val id: Int,
    val imageUrl: String,
    val name: String,
    val nickname: String,
    val shortDescription: String
)
