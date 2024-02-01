package kr.ac.konkuk.gdsc.plantory.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestGetPlantHistoriesDto(
    @SerialName("companionPlantId")
    val companionPlantId: Long,
    @SerialName("targetMonth")
    val targetMonth: Int
)
