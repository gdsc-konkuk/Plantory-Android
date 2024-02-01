package kr.ac.konkuk.gdsc.plantory.domain.repository

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostPlantHistoryDto
import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantHistoriesDto
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
    ): Result<ResponseGetPlantHistoriesDto>

    suspend fun postPlantHistory(
        companionPlantId: Int,
        requestPostHistoryDto: RequestPostPlantHistoryDto
    ): Result<Unit>
}