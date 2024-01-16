package kr.ac.konkuk.gdsc.plantory.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.konkuk.gdsc.plantory.domain.entity.Notification

@HiltViewModel
class NotificationViewModel : ViewModel() {
    val notificationList: List<Notification> = generateMockData()

    private fun generateMockData(): List<Notification> {
        val mockDataList = mutableListOf<Notification>()

        val mockData1 = Notification(
            id = 1,
            message = "물 주세요",
            timeAgo = "3분전"
        )

        val mockData2 = Notification(
            id = 2,
            message = "분갈이 하세요",
            timeAgo = "3일전"
        )

        val mockData3 = Notification(
            id = 3,
            message = "기록을 작성하세요",
            timeAgo = "3분전"
        )
        mockDataList.add(mockData1)
        mockDataList.add(mockData2)
        mockDataList.add(mockData3)
        return mockDataList
    }
}