package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantCheckList
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantDailyRecord
import java.util.Calendar
import java.util.Date

@HiltViewModel
class DetailViewModel: ViewModel() {
    val plantRecord: MutableList<PlantDailyRecord> = generateMockData()
    val plantInfo: Plant = generatePlantMockData()

    private var calendar = Calendar.getInstance()
    private val _currentYear = MutableStateFlow<Int>(-1)
    val currentYear: StateFlow<Int> get() = _currentYear
    private val _currentMonth = MutableStateFlow<Int>(-1)
    val currentMonth: StateFlow<Int> get() = _currentMonth

    init {
        calendar.time = Date()
        _currentYear.value = calendar.get(Calendar.YEAR)
        _currentMonth.value = calendar.get(Calendar.MONTH)
    }

    fun updatePreviousNextMonthYear(isPrevious: Boolean) {
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

    private fun generateMockData(): MutableList<PlantDailyRecord> {

        val dailyRecordList = mutableListOf<PlantDailyRecord>()

        val record1 = PlantDailyRecord(
            id = 1,
            imageUrl = "https://plchldr.co/i/400x700?&bg=D4E1E4&fc=46AEA1&text=hihiplantory",
            date = "2024/01/17",
            nickname = "초록이",
            comment = "자고 일어나니까 그새 좀 푸릇해진 것 같은 느낌이 들어서 몹시 뿌듯했당.... 우리초록이너무귀여워사랑해너는세상에서가장멋진다육이야 기죽지마어깨펴니가짱이야",
            checkList = PlantCheckList(
                isRepoted = true,
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
            checkList = PlantCheckList(
                isRepoted = false,
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
            checkList = PlantCheckList(
                isRepoted = true,
                isWatered = true
            )
        )
        dailyRecordList.add(record3)

        return dailyRecordList
    }

    private fun generatePlantMockData(): Plant {
        return Plant(
            id = 1,
            dday = 2,
            nickname = "식물1",
            species = "선인장",
            description = "하이하이",
            createdAt = "2023/12/31"
        )
    }
}