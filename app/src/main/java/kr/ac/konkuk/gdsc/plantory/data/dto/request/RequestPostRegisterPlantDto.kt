package kr.ac.konkuk.gdsc.plantory.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantInfo

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
){
    fun convertToPlantInfo() : PlantInfo = PlantInfo(
        "plantInfoId", nickname, shortDescription, birthDate, lastWaterDate
    )
}