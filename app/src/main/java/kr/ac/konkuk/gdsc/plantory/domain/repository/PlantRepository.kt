package kr.ac.konkuk.gdsc.plantory.domain.repository

import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantDailyRecord

interface PlantRepository {
    suspend fun getPlantDailyRecord(): Result<ResponseGetPlantDailyRecord>
}