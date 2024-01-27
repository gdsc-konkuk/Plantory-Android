package kr.ac.konkuk.gdsc.plantory.presentation.plant

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantInfo
import java.util.Calendar

class AddPlantViewModel : ViewModel() {
    private var calendar: Calendar = Calendar.getInstance()
    val plantSpeciesList: List<String> = generateMockData()

    private val _currentYear = MutableStateFlow<Int>(0)
    val currentYear: StateFlow<Int> get() = _currentYear

    private val _currentMonth = MutableStateFlow<Int>(0)
    val currentMonth: StateFlow<Int> get() = _currentMonth

    private val _currentDay = MutableStateFlow<Int>(0)
    val currentDay: StateFlow<Int> get() = _currentDay

    private val _plantInfo = MutableStateFlow(PlantInfo("","","","","", null))
    val plantInfo: StateFlow<PlantInfo> get() = _plantInfo

    init {
        _currentYear.value = calendar.get(Calendar.YEAR)
        _currentMonth.value = calendar.get(Calendar.MONTH)
        _currentDay.value = calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun updatePlantInfo(newPlantInfo: PlantInfo) {
        _plantInfo.value = newPlantInfo
    }

    fun updatePlantSpeciesLength(): Int {
        return plantInfo.value.species.length
    }

    fun checkIsNotEmpty(): Boolean {
        with(plantInfo.value) {
            return nickname.isNotEmpty() && species.isNotEmpty() && shortDescription.isNotEmpty()
        }
    }

    private fun generateMockData(): List<String> {
        return mutableListOf(
            "Cactus",
            "Cymbidium Orchid",
            "Chrysanthemum",
            "Calla Lily",
            "Carnation",
            "Cyrilla",
            "Cyphostemma",
            "Cydista",
            "Cyperus"
        )
    }
}