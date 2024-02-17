package kr.ac.konkuk.gdsc.plantory.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.domain.repository.PlantRepository
import kr.ac.konkuk.gdsc.plantory.util.date.DateUtil
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {
    private val _totalCarbon = MutableStateFlow(0.0)
    val totalCarbon: StateFlow<Double> get() = _totalCarbon

    private val _getAllPlantsState =
        MutableStateFlow<UiState<List<Plant>>>(UiState.Loading)
    val getAllPlantsState: StateFlow<UiState<List<Plant>>> =
        _getAllPlantsState.asStateFlow()

    fun getAllPlants() {
        viewModelScope.launch {
            plantRepository.getAllPlants().onSuccess { response ->
                _getAllPlantsState.value = if (response.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(addNewPlantItem(response.size, response))
                }
            }.onFailure { t ->
                _getAllPlantsState.value = UiState.Failure("${t.message}")
            }
        }
    }

    private fun addNewPlantItem(size: Int, list: List<Plant>): MutableList<Plant> {
        return list.toMutableList().apply { add(size, emptyItemForAddPlant) }
    }

    fun calculateTotalCarbon(plants: List<Plant>) {
        plants.forEach { plant ->
            val date = DateUtil.parseDate(plant.birthDate)
            if (date != null) {
                val days = DateUtil.calculateDaysFromDate(date) + 1
                updateTotalCarbon(totalCarbon.value + days * 16.6)
            }
        }
    }

    fun updateTotalCarbon(carbon: Double) {
        _totalCarbon.value = carbon
    }

    val emptyItemForAddPlant = Plant(
        birthDate = "",
        id = Int.MAX_VALUE,
        imageUrl = "R.drawable.img_home_add_new",
        name = "",
        nickname = "",
        shortDescription = "",
        dDay = -1
    )
}
