package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantCheckRecord
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantDailyRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UploadViewModel : ViewModel() {
    private fun generateMockData(): PlantDailyRecord {
        return PlantDailyRecord(
            id = 1,
            imageUrl = "https://plchldr.co/i/400x700?&bg=D4E1E4&fc=46AEA1&text=hihiplantory",
            date = "2024/01/17",
            nickname = "초록이",
            comment = "자고 일어나니까 그새 좀 푸릇해진 것 같은 느낌이 들어서 몹시 뿌듯했당.... 우리초록이너무귀여워사랑해너는세상에서가장멋진다육이야 기죽지마어깨펴니가짱이야",
            checkRecord = PlantCheckRecord(
                isRepoted = true,
                isWatered = false
            )
        )
    }

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> get() = _imageUri

    private val _diaryInput = MutableStateFlow<String>("")
    val diaryInput: StateFlow<String> get() = _diaryInput

    init {
        _diaryInput.value = ""
        _imageUri.value = null
    }

    fun updateDiaryInput(currinput: String) {
        _diaryInput.value = currinput
    }

    fun updateProfileImage(uri: Uri) {
        _imageUri.value = uri
    }
}