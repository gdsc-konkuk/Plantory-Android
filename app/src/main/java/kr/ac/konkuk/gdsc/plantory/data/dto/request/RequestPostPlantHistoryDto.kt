package kr.ac.konkuk.gdsc.plantory.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostPlantHistoryDto(
    @SerialName("historyType")
    val historyType: String
)
