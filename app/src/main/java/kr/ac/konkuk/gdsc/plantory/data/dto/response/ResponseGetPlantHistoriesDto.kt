package kr.ac.konkuk.gdsc.plantory.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistory
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistoryType

@Serializable
data class ResponseGetPlantHistoriesDto(
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

    fun convertToPlantHistory(): List<PlantHistory>? = histories?.map { data ->
        PlantHistory(
            id = data.id,
            type = PlantHistoryType.valueOf(data.type),
            date = data.date
        )
    }
}
