package kr.ac.konkuk.gdsc.plantory.data.repository

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostPlantHistoryDto
import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantRecordDto
import kr.ac.konkuk.gdsc.plantory.data.source.PlantDataSource
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistory
import kr.ac.konkuk.gdsc.plantory.domain.repository.PlantRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val plantDataSource: PlantDataSource
) : PlantRepository {
    override suspend fun postRegisterPlant(
        request: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ): Result<Unit> =
        runCatching {
            plantDataSource.postRegisterPlant(request, image)
        }

    override suspend fun getPlantHistories(
        companionPlantId: Int,
        targetMonth: String
    ): Result<List<PlantHistory>?> =
        runCatching {
            plantDataSource.getPlantHistories(companionPlantId, targetMonth).convertToPlantHistory()
        }

    override suspend fun postPlantHistory(
        companionPlantId: Int,
        requestPostHistoryDto: RequestPostPlantHistoryDto
    ): Result<Unit> =
        runCatching {
            plantDataSource.postPlantHistory(companionPlantId, requestPostHistoryDto)
        }

    override suspend fun postPlantRecord(
        companionPlantId: Int,
        request: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ): Result<Unit> =
        runCatching {
            plantDataSource.postPlantRecord(companionPlantId, request, image)
        }

    override suspend fun getPlantRecord(
        companionPlantId: Int,
        recordDate: String
    ): Result<ResponseGetPlantRecordDto> {
        return runCatching {
            plantDataSource.getPlantRecord(companionPlantId, recordDate)
        }
    }

    override suspend fun getAllPlants(): Result<List<Plant>> =
        runCatching {
            plantDataSource.getAllPlants().convertToPlant()
        }
}
