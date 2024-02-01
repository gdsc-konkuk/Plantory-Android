package kr.ac.konkuk.gdsc.plantory.presentation.plant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantCheckRecord
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantDailyRecord
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistory
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistoryType
import kr.ac.konkuk.gdsc.plantory.domain.repository.PlantRepository
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {
    val plantRecord: MutableList<PlantDailyRecord> = generateMockData()

    private val _plantInfo = MutableStateFlow<Plant>(Plant(0, "", "", "", "", ""))
    val plantInfo: StateFlow<Plant> get() = _plantInfo

    private var calendar = Calendar.getInstance()
    private val _currentYear = MutableStateFlow<Int>(-1)
    val currentYear: StateFlow<Int> get() = _currentYear
    private val _currentMonth = MutableStateFlow<Int>(-1)
    val currentMonth: StateFlow<Int> get() = _currentMonth

    private val _isWatered = MutableStateFlow<Boolean>(false)
    val isWatered: MutableStateFlow<Boolean> get() = _isWatered

    init {
        calendar.time = Date()
        _currentYear.value = calendar.get(Calendar.YEAR)
        _currentMonth.value = calendar.get(Calendar.MONTH)
        getPlantHistories()
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

    fun updatePlantInfo(plant: Plant) {
        _plantInfo.value = plant
    }

    fun updateIsWatered() {
        _isWatered.value = !_isWatered.value
    }

    private fun generateMockData(): MutableList<PlantDailyRecord> {

        val dailyRecordList = mutableListOf<PlantDailyRecord>()

        val record1 = PlantDailyRecord(
            id = 1,
            imageUrl = "https://plchldr.co/i/400x700?&bg=D4E1E4&fc=46AEA1&text=hihiplantory",
            date = "2024/01/17",
            nickname = "초록이",
            comment = "자고 일어나니까 그새 좀 푸릇해진 것 같은 느낌이 들어서 몹시 뿌듯했당.... 우리초록이너무귀여워사랑해너는세상에서가장멋진다육이야 기죽지마어깨펴니가짱이야",
            checkRecord = PlantCheckRecord(
                isRecorded = true,
                isWatered = false
            )
        )
        dailyRecordList.add(record1)

        val record2 = PlantDailyRecord(
            id = 2,
            imageUrl = "https://plchldr.co/i/400x700?&bg=D4E1E4&fc=46AEA1&text=hihiplantory",
            date = "2024/01/18",
            nickname = "초록이",
            comment = "오늘은 좀 더 따뜻한 날씨에 얼굴을 내민 것 같아서 기분이 좋았어요!",
            checkRecord = PlantCheckRecord(
                isRecorded = false,
                isWatered = true
            )
        )
        dailyRecordList.add(record2)

        val record3 = PlantDailyRecord(
            id = 3,
            imageUrl = "https://plchldr.co/i/400x700?&bg=D4E1E4&fc=46AEA1&text=hihiplantory",
            date = "2024/01/20",
            nickname = "초록이",
            comment = "오늘은 좀 더 따뜻한 날씨에 얼굴을 내민 것 같아서 기분이 좋았어요!",
            checkRecord = PlantCheckRecord(
                isRecorded = true,
                isWatered = true
            )
        )
        dailyRecordList.add(record3)

        val record4 = PlantDailyRecord(
            id = 4,
            imageUrl = "https://plchldr.co/i/400x700?&bg=D4E1E4&fc=46AEA1&text=hihiplantory",
            date = "2024/01/24",
            nickname = "초록이",
            comment = "오늘은 좀 더 따뜻한 날씨에 얼굴을 내민 것 같아서 기분이 좋았어요!",
            checkRecord = PlantCheckRecord(
                isRecorded = true,
                isWatered = true
            )
        )
        dailyRecordList.add(record4)

        return dailyRecordList
    }

    fun generatePlantMockData(): Plant {
        return Plant(
            id = 1,
            imageUrl = null,
            nickname = "식물1",
            shortDescription = "하이하이",
            birthDate = "2023/12/31",
            name = "선인장",
        )
    }

    /*getHistory*/
    private val _getPlantHistoryState = MutableStateFlow<UiState<List<PlantHistory>>>(UiState.Loading)
    val getPlantHistoryState: StateFlow<UiState<List<PlantHistory>>> = _getPlantHistoryState.asStateFlow()

    fun getPlantHistories() {
        viewModelScope.launch {
            val month = formatDateToAddZero(currentMonth.value+1)
            val targetMonth = "${currentYear.value}-${month}"
            plantRepository.getPlantHistories(17, targetMonth)
                .onSuccess { response ->
                    if (response.histories != null){
                        _getPlantHistoryState.value = UiState.Success(response.histories.map { plantHistoryDto ->
                            PlantHistory(
                                id = plantHistoryDto.id,
                                type = PlantHistoryType.fromString(plantHistoryDto.type),
                                date = plantHistoryDto.date
                            )
                        })
                    }else {
                        _getPlantHistoryState.value = UiState.Success(emptyList())
                    }
                }.onFailure { t ->
                    _getPlantHistoryState.value = UiState.Failure("${t.message}")
                }
        }
    }

    private fun formatDateToAddZero(date: Int): String {
        if (date < 10){
            return String.format("%02d", date)
        }
        return date.toString()
    }

    companion object {
        private const val FIRST_DAY = 1
    }
}