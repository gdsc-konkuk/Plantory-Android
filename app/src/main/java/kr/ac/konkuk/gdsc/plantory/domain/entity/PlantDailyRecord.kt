package kr.ac.konkuk.gdsc.plantory.domain.entity

data class PlantDailyRecord (
    val id: Long,
    val date: String,
    val nickname: String,
    val imageUrl: String,
    val comment: String,
    val checkList: PlantCheckList
)

data class PlantCheckList(
    val isWatered: Boolean,
    val isRepoted: Boolean
)