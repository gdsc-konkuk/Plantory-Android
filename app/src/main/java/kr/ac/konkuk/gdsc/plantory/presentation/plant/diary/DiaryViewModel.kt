package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import androidx.lifecycle.ViewModel
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantCheckList
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantDailyRecord

class DiaryViewModel : ViewModel() {
    val plantRecord: PlantDailyRecord = generateMockData()

    private fun generateMockData(): PlantDailyRecord {
        return PlantDailyRecord(
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
    }
}