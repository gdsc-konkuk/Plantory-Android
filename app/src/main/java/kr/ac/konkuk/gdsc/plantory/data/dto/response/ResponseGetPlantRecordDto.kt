package kr.ac.konkuk.gdsc.plantory.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetPlantRecordDto(
    @SerialName("comment")
    val comment: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("plantRecordId")
    val plantRecordId: Int,
    @SerialName("water")
    val water: Boolean
)
