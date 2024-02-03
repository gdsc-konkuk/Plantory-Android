package kr.ac.konkuk.gdsc.plantory.domain.repository

import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantDailyRecord
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant

interface PlantRepository {
    suspend fun getPlantDailyRecord(): Result<ResponseGetPlantDailyRecord>

    suspend fun getAllPlants(): Result<List<Plant>>
}