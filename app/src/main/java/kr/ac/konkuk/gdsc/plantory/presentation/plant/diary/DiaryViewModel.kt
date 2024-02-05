package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantRecordDto
import kr.ac.konkuk.gdsc.plantory.domain.repository.PlantRepository
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {

    private val _getPlantDailyRecordState =
        MutableStateFlow<UiState<ResponseGetPlantRecordDto>>(UiState.Loading)
    val getPlantDailyRecordState: StateFlow<UiState<ResponseGetPlantRecordDto>> =
        _getPlantDailyRecordState.asStateFlow()


    fun getPlantDailyRecord(
        companionPlantId: Int,
        recordDate: String
    ) {
        viewModelScope.launch {
            plantRepository.getPlantRecord(
                companionPlantId = companionPlantId,
                recordDate = recordDate
            )
                .onSuccess { response ->
                    _getPlantDailyRecordState.value = UiState.Success(response)
                    Timber.e("성공 $response")
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("HTTP 실패")
                    }
                    Timber.e("${t.message}")
                    _getPlantDailyRecordState.value = UiState.Failure("${t.message}")
                }
        }
    }
}
