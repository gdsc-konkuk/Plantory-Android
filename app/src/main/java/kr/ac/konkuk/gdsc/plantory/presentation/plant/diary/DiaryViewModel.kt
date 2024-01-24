package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetPlantDailyRecord
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
        MutableStateFlow<UiState<ResponseGetPlantDailyRecord>>(UiState.Loading)
    val getPlantDailyRecordState: StateFlow<UiState<ResponseGetPlantDailyRecord>> =
        _getPlantDailyRecordState.asStateFlow()

    init {
        getPlantDailyRecord()
    }

    private fun getPlantDailyRecord() {
        viewModelScope.launch {
            plantRepository.getPlantDailyRecord()
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