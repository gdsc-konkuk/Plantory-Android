package kr.ac.konkuk.gdsc.plantory.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantDailyRecord
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant

interface PlantRepository {

    suspend fun postRegisterPlant(
        request: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ) : Result<Unit>

    suspend fun getPlantDailyRecord(): Result<ResponseGetPlantDailyRecord>

    suspend fun getAllPlants(): Result<List<Plant>>

