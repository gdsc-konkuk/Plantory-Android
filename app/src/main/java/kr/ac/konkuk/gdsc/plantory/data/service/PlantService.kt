package kr.ac.konkuk.gdsc.plantory.data.service

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostPlantHistoryDto
import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetAllPlantsDto
import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantHistoriesDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface PlantService {
    @GET("api/v1/plants")
    suspend fun getAllPlants(): ResponseGetAllPlantsDto

    @Multipart
    @POST("api/v1/plants")
    suspend fun postRegisterPlant(
        @PartMap request: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    )

    @GET("api/v1/plants/{companionPlantId}/histories")
    suspend fun getPlantHistories(
        @Path("companionPlantId") companionPlantId: Int,
        @Query("targetMonth") targetMonth: String
    ): ResponseGetPlantHistoriesDto

    @POST("api/v1/plants/{companionPlantId}/histories")
    suspend fun postPlantHistory(
        @Path("companionPlantId") companionPlantId: Int,
        @Body requestPostHistoryDto: RequestPostPlantHistoryDto
    )

    @DELETE("api/v1/plants/{companionPlantId}")
    suspend fun deletePlant(
        @Path("companionPlantId") companionPlantId: Int
    )
}
