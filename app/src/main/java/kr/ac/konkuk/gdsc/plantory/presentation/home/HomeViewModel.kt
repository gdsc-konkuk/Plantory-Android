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

    init {
        getAllPlants()
    }

    private fun getAllPlants() {
        viewModelScope.launch {
            plantRepository.getAllPlants(
            ).onSuccess { response ->
                _getAllPlantsState.value = UiState.Success(response)
            }.onFailure { t ->
                _getAllPlantsState.value = UiState.Failure("${t.message}")
            }
        }
    }
}