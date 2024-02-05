package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostRecordDto
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantRecord
import kr.ac.konkuk.gdsc.plantory.domain.repository.PlantRepository
import kr.ac.konkuk.gdsc.plantory.util.multipart.ContentUriRequestBody
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val plantRepository: PlantRepository
): ViewModel() {
    private val _clickedPlantId = MutableStateFlow<Int>(0)
    val clickedPlantId: StateFlow<Int> get() = _clickedPlantId

    private val _clickedPlantNickname = MutableStateFlow<String>("")
    val clickedPlantNickname: StateFlow<String> get() = _clickedPlantNickname

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> get() = _imageUri

    private val _plantRecord = MutableStateFlow<PlantRecord>(PlantRecord(""))
    val plantRecord: StateFlow<PlantRecord> get() = _plantRecord

    private var imageRequestBody: ContentUriRequestBody? = null

    init {
        _plantRecord.value = PlantRecord("")
        _imageUri.value = null
    }

    fun updateClickedPlantId(id: Int) {
        _clickedPlantId.value = id
    }

    fun updateClickedPlantNickname(nickname: String) {
        _clickedPlantNickname.value = nickname
    }

    fun updateDiaryInput(currinput: String) {
        _plantRecord.value.comment = currinput
    }

    fun updateProfileImage(uri: Uri) {
        _imageUri.value = uri
    }

    /*postPlantRecord*/
    private val _postPlantRecordState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postPlantRecordState: StateFlow<UiState<Unit>> = _postPlantRecordState.asStateFlow()

    fun postPlantRecord() {
        viewModelScope.launch {
            _postPlantRecordState.value = UiState.Loading
            val (request, image) = createRequestBody(plantRecord.value)

            plantRepository.postPlantRecord(clickedPlantId.value, request, image).onSuccess { response ->
                _postPlantRecordState.value = UiState.Success(response)
                Timber.e("성공 $response")
            }.onFailure { t ->
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Timber.e("HTTP 실패: $errorResponse")
                }
                _postPlantRecordState.value = UiState.Failure("${t.message}")
            }
        }
    }

    private fun createRequestBody(
        plantRecord: PlantRecord
    ): Pair<HashMap<String, RequestBody>, MultipartBody.Part?> {
        val imageFormData = imageRequestBody?.toFormData()
        val plantRecordDto = RequestPostRecordDto(
            comment = plantRecord.comment
        )
        val plantInfoJsonString =
            Json.encodeToString(RequestPostRecordDto.serializer(), plantRecordDto)

        val plainTextRequestBody = hashMapOf(
            "request" to plantInfoJsonString.toRequestBody("application/json".toMediaTypeOrNull())
        )
        return plainTextRequestBody to imageFormData
    }

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
