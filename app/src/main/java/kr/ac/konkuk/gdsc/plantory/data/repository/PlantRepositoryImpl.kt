package kr.ac.konkuk.gdsc.plantory.data.repository

import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantDailyRecord
import kr.ac.konkuk.gdsc.plantory.domain.repository.DataStoreRepository
import kr.ac.konkuk.gdsc.plantory.domain.repository.PlantRepository
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    dataStoreRepository: DataStoreRepository
) : PlantRepository {

    override suspend fun getPlantDailyRecord(): Result<ResponseGetPlantDailyRecord> {
        return runCatching {
            ResponseGetPlantDailyRecord(
                id = 1,
                imageUrl = "https://plchldr.co/i/400x700?&bg=D4E1E4&fc=46AEA1&text=hihiplantory",
                comment = "싹이 났다",
                nickname = "식물1",
                isWatered = true,
                date = "2024-01-24"
            )
        }
    }
}