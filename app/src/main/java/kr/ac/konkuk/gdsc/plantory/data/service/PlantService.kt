package kr.ac.konkuk.gdsc.plantory.data.service

import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetAllPlantsDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface PlantService {
    @GET("api/v1/plants")
    suspend fun getAllPlants(): ResponseGetAllPlantsDto

    @Multipart
    @POST("api/v1/plants")
    suspend fun postRegisterPlant(
        @PartMap request: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    )
}
