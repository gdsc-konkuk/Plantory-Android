package kr.ac.konkuk.gdsc.plantory.presentation.plant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostPlantHistoryDto
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistory
import kr.ac.konkuk.gdsc.plantory.domain.repository.PlantRepository
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import retrofit2.HttpException
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {

    private var calendar = Calendar.getInstance()
    private val _currentYear = MutableStateFlow<Int>(-1)
    val currentYear: StateFlow<Int> get() = _currentYear
    private val _currentMonth = MutableStateFlow<Int>(-1)
    val currentMonth: StateFlow<Int> get() = _currentMonth
    private val _currentDay = MutableStateFlow<Int>(-1)
    val currentDay: StateFlow<Int> get() = _currentDay

    private val _isWatered = MutableStateFlow<Boolean>(false)
    val isWatered: MutableStateFlow<Boolean> get() = _isWatered

    private val _clickedPlantId = MutableStateFlow<Int>(0)
    val clickedPlantId: MutableStateFlow<Int> get() = _clickedPlantId

    init {
        calendar.time = Date()
        _currentYear.value = calendar.get(Calendar.YEAR)
        _currentMonth.value = calendar.get(Calendar.MONTH)
        _currentDay.value = calendar.get(Calendar.DAY_OF_MONTH)
        getPlantById()
        getPlantHistories()
    }

    fun updateClickedPlantId(id: Int) {
        _clickedPlantId.value = id
    }

    fun updateCalendarDayList(currYear: Int, currMonth: Int): MutableList<Date> {
        calendar.set(Calendar.YEAR, currYear)
        calendar.set(Calendar.MONTH, currMonth)
        calendar.set(Calendar.DAY_OF_MONTH, FIRST_DAY)

        val dayList: MutableList<Date> = mutableListOf()

        for (i in 0..Calendar.WEEK_OF_MONTH) {
            for (k in 0..Calendar.DAY_OF_YEAR) {
                calendar.add(
                    Calendar.DAY_OF_MONTH,
                    (FIRST_DAY - calendar.get(Calendar.DAY_OF_WEEK)) + k
                )
                dayList.add(calendar.time.clone() as Date)
            }
            calendar.add(Calendar.WEEK_OF_MONTH, FIRST_DAY)
        }
        return dayList
    }

    fun updateMonthAndYear(isPrevious: Boolean) {
        if (isPrevious) {
            _currentMonth.value--
            if (_currentMonth.value < 0) {
                _currentMonth.value = Calendar.DECEMBER
                _currentYear.value--
            }
        } else {
            _currentMonth.value++
            if (_currentMonth.value > 11) {
                _currentMonth.value = Calendar.JANUARY
                _currentYear.value++
            }
        }
    }

    fun updateIsWatered(watered: Boolean) {
        _isWatered.value = watered
    }

    /*getPlantById*/
    private val _getPlantDetailState =
        MutableStateFlow<UiState<Plant>>(UiState.Loading)
    val getPlantDetailState: StateFlow<UiState<Plant>> =
        _getPlantDetailState.asStateFlow()

    private fun getPlantById() {
        viewModelScope.launch {
            plantRepository.getAllPlants().onSuccess { response ->
                _getPlantDetailState.value = if (response.isEmpty()) {
                    UiState.Empty
                } else {
                    val clickedPlant = response.find { plant ->
                        plant.id == clickedPlantId.value
                    }

                    if (clickedPlant != null) {
                        UiState.Success(clickedPlant)
                    } else {
                        UiState.Empty
                    }
                }
            }.onFailure { t ->
                _getPlantDetailState.value = UiState.Failure("${t.message}")
            }
        }
    }

    /*getHistory*/
    private val _getPlantHistoryState =
        MutableStateFlow<UiState<List<PlantHistory>>>(UiState.Loading)
    val getPlantHistoryState: StateFlow<UiState<List<PlantHistory>>> =
        _getPlantHistoryState.asStateFlow()

    fun getPlantHistories() {
        viewModelScope.launch {
            val month = formatDateToAddZero(currentMonth.value + 1)
            val targetMonth = "${currentYear.value}-${month}"
            plantRepository.getPlantHistories(clickedPlantId.value, targetMonth)
                .onSuccess { response ->
                    if (response!= null) {
                        _getPlantHistoryState.value =
                            UiState.Success(response)
                    } else {
                        _getPlantHistoryState.value = UiState.Success(emptyList())
                    }
                }.onFailure { t ->
                    _getPlantHistoryState.value = UiState.Failure("${t.message}")
                }
        }
    }

    /*post Watered*/
    private val _postPlantWateredState =
        MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postPlantWateredState: StateFlow<UiState<Unit>> =
        _postPlantWateredState.asStateFlow()

    fun postPlantWatered() {
        viewModelScope.launch {
            _postPlantWateredState.value = UiState.Loading
            plantRepository.postPlantHistory(clickedPlantId.value, RequestPostPlantHistoryDto("WATER_CHANGE"))
                .onSuccess { response ->
                    _postPlantWateredState.value = UiState.Success(response)
                    Timber.e("성공 $response")
                }.onFailure { t ->
                    if (t is HttpException) {
                        val errorResponse = t.response()?.errorBody()?.string()
                        Timber.e("HTTP 실패: $errorResponse")
                    }
                    Timber.e("${t.message}")
                    _postPlantWateredState.value = UiState.Failure("${t.message}")
                }
        }
    }

    /*deletePlant*/
    private val _deletePlantState = MutableStateFlow<UiState<Unit?>>(UiState.Loading)
    val deletePlantState: StateFlow<UiState<Unit?>> = _deletePlantState.asStateFlow()

    fun deletePlant() {
        viewModelScope.launch {
            _deletePlantState.value = UiState.Loading
            plantRepository.deletePlant(clickedPlantId.value)
                .onSuccess { response ->
                    _deletePlantState.value = UiState.Success(null)
                }.onFailure { t ->
                    if (t is HttpException) {
                        val errorResponse = t.response()?.errorBody()?.string()
                        Timber.e("HTTP 실패: $errorResponse")
                    }
                    _deletePlantState.value = UiState.Failure("${t.message}")
                }
        }
    }

    private fun formatDateToAddZero(date: Int): String {
        if (date < 10) {
            return String.format("%02d", date)
        }
        return date.toString()
    }

    companion object {
        private const val FIRST_DAY = 1
    }
}