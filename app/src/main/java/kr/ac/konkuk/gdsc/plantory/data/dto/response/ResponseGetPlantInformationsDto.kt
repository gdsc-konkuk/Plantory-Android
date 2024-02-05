package kr.ac.konkuk.gdsc.plantory.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantInformation

@Serializable
data class ResponseGetPlantInformationsDto(
    @SerialName("plantInformations")
    val plantInformations: List<PlantInformationDto>
) {
    @Serializable
    data class PlantInformationDto(
        @SerialName("id")
        val id: Int,
        @SerialName("species")
        val species: String,
        @SerialName("familyName")
        val familyName: String
    )

    fun convertToPlantInformation(): List<PlantInformation> = plantInformations.map { data ->
        PlantInformation(
            id = data.id,
            species = data.species,
            familyName = data.familyName
        )
    }
}
