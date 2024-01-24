package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class AddPlantViewModel : ViewModel() {
    private var calendar: Calendar = Calendar.getInstance()
    val plantSpeciesList: List<String> = generateMockData()

    private val _addImageUri = MutableStateFlow<Uri?>(null)
    val addImageUri: StateFlow<Uri?> get() = _addImageUri

    private val _addBirthday = MutableStateFlow<String>("")
    val addBirthday: StateFlow<String> get() = _addBirthday

    private val _addNickname = MutableStateFlow<String>("")
    val addNickname: StateFlow<String> get() = _addNickname

    private val _addSpecies = MutableStateFlow<String>("")
    val addSpecies: StateFlow<String> get() = _addSpecies

    private val _addLastWatered = MutableStateFlow<String>("")
    val addLastWatered: StateFlow<String> get() = _addLastWatered

    private val _addDescription = MutableStateFlow<String>("")
    val addDescription: StateFlow<String> get() = _addDescription

    private val _currentYear = MutableStateFlow<Int>(0)
    val currentYear: StateFlow<Int> get() = _currentYear

    private val _currentMonth = MutableStateFlow<Int>(0)
    val currentMonth: StateFlow<Int> get() = _currentMonth

    private val _currentDay = MutableStateFlow<Int>(0)
    val currentDay: StateFlow<Int> get() = _currentDay

    init {
        _addImageUri.value = null
        _addNickname.value = ""
        _addSpecies.value = ""
        _addLastWatered.value = ""
        _addDescription.value = ""
        _currentYear.value = calendar.get(Calendar.YEAR)
        _currentMonth.value = calendar.get(Calendar.MONTH)
        _currentDay.value = calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun updateProfileImage(uri: Uri) {
        _addImageUri.value = uri
    }

    fun updateBirthday(birthday: String) {
        _addBirthday.value = birthday
    }

    fun updateNickname(nickname: String) {
        _addNickname.value = nickname
    }

    fun updateSpecies(species: String) {
        _addSpecies.value = species
    }

    fun updateLastWatered(lastWatered: String) {
        _addLastWatered.value = lastWatered
    }

    fun updateDescription(description: String) {
        _addDescription.value = description
    }

    fun updatePlantSpeciesLength(): Int {
        return addSpecies.value.length
    }

    fun checkIsNotEmpty(): Boolean {
        return addNickname.value.isNotEmpty() && addSpecies.value.isNotEmpty() && addDescription.value.isNotEmpty()
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