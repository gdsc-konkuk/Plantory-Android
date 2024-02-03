package kr.ac.konkuk.gdsc.plantory.data.service

import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetAllPlantsDto
import retrofit2.http.GET

interface PlantService {
    @GET("api/v1/plants")
    suspend fun getAllPlants() : ResponseGetAllPlantsDto
}