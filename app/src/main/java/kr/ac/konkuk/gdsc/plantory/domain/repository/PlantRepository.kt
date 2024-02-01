package kr.ac.konkuk.gdsc.plantory.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface PlantRepository {

    suspend fun postRegisterPlant(
        request: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ) : Result<Unit>
}