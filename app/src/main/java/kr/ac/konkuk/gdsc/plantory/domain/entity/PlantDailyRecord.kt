package kr.ac.konkuk.gdsc.plantory.domain.entity

data class PlantDailyRecord (
    val id: Long,
    val date: String,
    val nickname: String,
    val imageUrl: String,
    val comment: String,
    val checkRecord: PlantCheckRecord
)

data class PlantCheckRecord(
    val isWatered: Boolean,
    val isRecorded: Boolean
)