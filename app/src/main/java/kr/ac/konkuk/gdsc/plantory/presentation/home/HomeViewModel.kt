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
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {
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

    val emptyItemForAddPlant = Plant(
        birthDate = "",
        id = Int.MAX_VALUE,
        imageUrl = "R.drawable.img_home_new",
        name = "",
        nickname = "",
        shortDescription = ""
    )
}
