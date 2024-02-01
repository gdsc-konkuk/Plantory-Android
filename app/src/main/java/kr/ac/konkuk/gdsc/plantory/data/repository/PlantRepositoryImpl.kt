package kr.ac.konkuk.gdsc.plantory.data.repository

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostRegisterUserDto
import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantHistoriesDto
import kr.ac.konkuk.gdsc.plantory.data.source.PlantDataSource
import kr.ac.konkuk.gdsc.plantory.domain.repository.PlantRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val plantDataSource: PlantDataSource
): PlantRepository {
    override suspend fun postRegisterPlant(
        request: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ) : Result<Unit> =
        runCatching {
            plantDataSource.postRegisterPlant(request, image)
        }

    override suspend fun getPlantHistories(
        companionPlantId: Int,
        targetMonth: String
    ) : Result<ResponseGetPlantHistoriesDto> =
        runCatching {
            plantDataSource.getPlantHistories(companionPlantId, targetMonth)
        }
}