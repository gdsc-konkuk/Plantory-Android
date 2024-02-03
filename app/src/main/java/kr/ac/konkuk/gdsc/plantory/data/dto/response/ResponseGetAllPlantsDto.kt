package kr.ac.konkuk.gdsc.plantory.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant

@Serializable
data class ResponseGetAllPlantsDto(
    @SerialName("companionPlants")
    val companionPlants: List<CompanionPlant>
) {
    @Serializable
    data class CompanionPlant(
        @SerialName("birthDate")
        val birthDate: String,
        @SerialName("id")
        val id: Int,
        @SerialName("imageUrl")
        val imageUrl: String,
        @SerialName("name")
        val name: String,
        @SerialName("nickname")
        val nickname: String,
        @SerialName("shortDescription")
        val shortDescription: String
    )

    fun convertToPlant(): List<Plant> = companionPlants.map { data ->
        Plant(
            id = data.id,
            birthDate = data.birthDate,
            imageUrl = data.imageUrl,
            name = data.name,
            nickname = data.nickname,
            shortDescription = data.shortDescription
        )
    }
}