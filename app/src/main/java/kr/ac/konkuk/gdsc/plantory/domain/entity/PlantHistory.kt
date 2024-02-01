package kr.ac.konkuk.gdsc.plantory.domain.entity

import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantHistoriesDto

data class PlantHistory(
    val id: Int,
    val type: PlantHistoryType,
    val date: String
)
enum class PlantHistoryType {
    WATER_CHANGE, POT_CHANGE, RECORDING;

    companion object {
        fun fromString(value: String): PlantHistoryType {
            return when (value) {
                "WATER_CHANGE" -> WATER_CHANGE
                "POT_CHANGE" -> POT_CHANGE
                "RECORDING" -> RECORDING
                else -> throw IllegalArgumentException("Unknown PlantHistoryType: $value")
            }
        }
    }
}

