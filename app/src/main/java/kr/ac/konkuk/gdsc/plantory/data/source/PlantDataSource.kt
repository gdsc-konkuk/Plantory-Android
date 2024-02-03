package kr.ac.konkuk.gdsc.plantory.data.source

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostRegisterPlantDto
import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostRegisterUserDto
import kr.ac.konkuk.gdsc.plantory.data.service.PlantService
import kr.ac.konkuk.gdsc.plantory.data.service.UserService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import javax.inject.Inject

class PlantDataSource @Inject constructor(
    private val plantService: PlantService
) {
    suspend fun getAllPlants() =
        plantService.getAllPlants()

    suspend fun postRegisterPlant(
        request: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ) = plantService.postRegisterPlant(request, image)
}