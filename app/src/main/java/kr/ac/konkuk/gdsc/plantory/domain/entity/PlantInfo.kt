package kr.ac.konkuk.gdsc.plantory.domain.entity

import android.net.Uri

data class PlantInfo(
    val species: String,
    val nickname: String,
    val shortDescription: String,
    val birthDate: String,
    val lastWaterDate: String,
//    val image: Uri?,
    )
