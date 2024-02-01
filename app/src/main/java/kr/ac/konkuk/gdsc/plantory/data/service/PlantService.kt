package kr.ac.konkuk.gdsc.plantory.data.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap


interface PlantService {

    @Multipart
    @POST("api/v1/plants")
    suspend fun postRegisterPlant(
        @PartMap request: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    )
}