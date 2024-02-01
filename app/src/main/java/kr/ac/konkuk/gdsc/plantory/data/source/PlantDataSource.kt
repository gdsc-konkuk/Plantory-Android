package kr.ac.konkuk.gdsc.plantory.data.source

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostPlantHistoryDto
import kr.ac.konkuk.gdsc.plantory.data.service.PlantService
import kr.ac.konkuk.gdsc.plantory.data.service.UserService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.Path
import javax.inject.Inject

class PlantDataSource @Inject constructor(
    private val plantService: PlantService
) {
    suspend fun postRegisterPlant(
        request: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ) = plantService.postRegisterPlant(request, image)

    suspend fun getPlantHistories(
        companionPlantId: Int,
        targetMonth: String
    ) = plantService.getPlantHistories(companionPlantId, targetMonth)

    suspend fun postPlantHistory(
        companionPlantId: Int,
        requestPostHistoryDto: RequestPostPlantHistoryDto
    ) = plantService.postPlantHistory(companionPlantId, requestPostHistoryDto)
}