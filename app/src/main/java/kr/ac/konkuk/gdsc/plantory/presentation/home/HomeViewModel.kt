package kr.ac.konkuk.gdsc.plantory.presentation.home

import androidx.lifecycle.ViewModel
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant


class HomeViewModel : ViewModel() {
    val plantList: List<Plant> = generateMockData()

    private fun generateMockData(): List<Plant> {
        val mockDataList = mutableListOf<Plant>()

//        val mockData1 = Plant(
//            id = 1,
//            dday = 2,
//            nickname = "식물1",
//            species = "선인장",
//            description = "하이하이",
//            "2023/12/31"
//        )
//
//        val mockData2 = Plant(
//            id = 2,
//            dday = 2,
//            nickname = "식물2",
//            species = "선인장",
//            description = "하이하이",
//            "2023/12/31"
//        )
//
//        val mockData3 = Plant(
//            id = 3,
//            dday = 2,
//            nickname = "식물3",
//            species = "선인장",
//            description = "하이하이",
//            "2023/12/31"
//        )
        val mockData1 = Plant(
            id = 1,
            imageUrl = null,
            nickname = "식물1",
            shortDescription = "하이하이",
            birthDate = "2023/12/31",
            name = "선인장",
        )
        val mockData2 = Plant(
            id = 2,
            imageUrl = null,
            nickname = "식물1",
            shortDescription = "하이하이",
            birthDate = "2023/12/31",
            name = "선인장",
        )
        val mockData3 = Plant(
            id = 3,
            imageUrl = null,
            nickname = "식물1",
            shortDescription = "하이하이",
            birthDate = "2023/12/31",
            name = "선인장",
        )
        mockDataList.add(mockData1)
        mockDataList.add(mockData2)
        mockDataList.add(mockData3)
        return mockDataList
    }
}