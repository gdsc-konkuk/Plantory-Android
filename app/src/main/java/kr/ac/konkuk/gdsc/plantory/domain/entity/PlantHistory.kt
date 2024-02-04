package kr.ac.konkuk.gdsc.plantory.domain.entity

import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantHistoriesDto

data class PlantHistory(
    val id: Int,
    val type: PlantHistoryType,
    val date: String
)
enum class PlantHistoryType {
    WATER_CHANGE, POT_CHANGE, RECORDING;
}
