package kr.ac.konkuk.gdsc.plantory.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostRegisterPlantDto(
    @SerialName("plantInformationId")
    val plantInformationId: Int,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("shortDescription")
    val shortDescription: String,
    @SerialName("birthDate")
    val birthDate: String,
    @SerialName("lastWaterDate")
    val lastWaterDate: String,
)