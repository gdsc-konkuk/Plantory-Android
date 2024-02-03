package kr.ac.konkuk.gdsc.plantory.data.source

import kr.ac.konkuk.gdsc.plantory.data.service.PlantService
import javax.inject.Inject

class PlantDataSource @Inject constructor(
    private val plantService: PlantService
) {
    suspend fun getAllPlants() =
        plantService.getAllPlants()
}