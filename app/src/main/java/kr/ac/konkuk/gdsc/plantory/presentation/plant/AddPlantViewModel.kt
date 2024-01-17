package kr.ac.konkuk.gdsc.plantory.presentation.plant

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AddPlantViewModel : ViewModel() {
    val plantSpeciesList: List<String> = generateMockData()

    private fun generateMockData(): List<String> {
        val mockDataList = mutableListOf<String>(
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
        return mockDataList
    }
}