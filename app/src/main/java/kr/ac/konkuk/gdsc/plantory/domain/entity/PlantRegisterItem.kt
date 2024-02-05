package kr.ac.konkuk.gdsc.plantory.domain.entity

data class PlantRegisterItem(
    val species: String,
    val nickname: String,
    val shortDescription: String,
    val birthDate: String,
    val lastWaterDate: String
)
