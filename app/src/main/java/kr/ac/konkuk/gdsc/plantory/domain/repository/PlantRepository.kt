package kr.ac.konkuk.gdsc.plantory.domain.repository

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostPlantHistoryDto
import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantDailyRecord
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistory
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface PlantRepository {

    suspend fun postRegisterPlant(
        request: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ): Result<Unit>

    suspend fun getPlantHistories(
        companionPlantId: Int,
        targetMonth: String
    ): Result<List<PlantHistory>?>

    suspend fun postPlantHistory(
        companionPlantId: Int,
        requestPostHistoryDto: RequestPostPlantHistoryDto
    ): Result<Unit>

    suspend fun getPlantDailyRecord(): Result<ResponseGetPlantDailyRecord>

    suspend fun getAllPlants(): Result<List<Plant>>

    suspend fun deletePlant(
        companionPlantId: Int
    ): Result<Unit?>
}
