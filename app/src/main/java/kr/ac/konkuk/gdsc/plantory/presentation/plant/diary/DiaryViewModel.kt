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

//    private fun generateMockData(): PlantDailyRecord {
//        return PlantDailyRecord(
//            id = 1,
//            imageUrl = "https://plchldr.co/i/400x700?&bg=D4E1E4&fc=46AEA1&text=hihiplantory",
//            date = "2024/01/17",
//            nickname = "초록이",
//            comment = "자고 일어나니까 그새 좀 푸릇해진 것 같은 느낌이 들어서 몹시 뿌듯했당.... 우리초록이너무귀여워사랑해너는세상에서가장멋진다육이야 기죽지마어깨펴니가짱이야",
//            checkRecord = PlantCheckRecord(
//                isRecorded = true,
//                isWatered = false
//            )
//        )
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
