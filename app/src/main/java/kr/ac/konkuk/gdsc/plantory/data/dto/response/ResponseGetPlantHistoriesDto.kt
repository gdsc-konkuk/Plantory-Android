package kr.ac.konkuk.gdsc.plantory.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistoryType

@Serializable
data class ResponseGetPlantHistoriesDto (
    @SerialName("histories")
    val histories: List<PlantHistoryDto>?
) {
    @Serializable
    data class PlantHistoryDto(
        @SerialName("id")
        val id: Int,
        @SerialName("type")
        val type: String,
        @SerialName("date")
        val date: String
    )
}