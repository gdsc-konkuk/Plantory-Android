package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UploadViewModel : ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> get() = _imageUri

    private val _wateredState = MutableStateFlow<Boolean>(false)
    val wateredState: StateFlow<Boolean> get() = _wateredState

    private val _repotedState = MutableStateFlow<Boolean>(false)
    val repotedState: StateFlow<Boolean> get() = _repotedState

    private val _diaryInput = MutableStateFlow<String>("")
    val diaryInput: StateFlow<String> get() = _diaryInput

    init {
        _wateredState.value = false
        _repotedState.value = false
        _diaryInput.value = ""
        _imageUri.value = null
    }

    fun updateWateredButtonState() {
        _wateredState.value = !_wateredState.value
    }

    fun updateRepotedButtonState() {
        _repotedState.value = !_repotedState.value
    }

    fun updateDiaryInput(currinput: String) {
        _diaryInput.value = currinput
    }

    fun updateProfileImage(uri: Uri) {
        _imageUri.value = uri
    }
}