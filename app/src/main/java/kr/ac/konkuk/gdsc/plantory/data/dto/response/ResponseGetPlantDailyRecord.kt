package kr.ac.konkuk.gdsc.plantory.data.dto.response

data class ResponseGetPlantDailyRecord(
    val id: Int,
    val date: String,
    val imageUrl: String,
    val comment: String,
    val nickname: String,
    val isWatered: Boolean
)
