package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostRegisterPlantDto
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantRegisterItem
import kr.ac.konkuk.gdsc.plantory.domain.repository.PlantRepository
import kr.ac.konkuk.gdsc.plantory.util.multipart.ContentUriRequestBody
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddPlantViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {
    private var calendar: Calendar = Calendar.getInstance()
    val plantSpeciesList: List<String> = generateMockData()

    private val _currentYear = MutableStateFlow<Int>(0)
    val currentYear: StateFlow<Int> get() = _currentYear

    private val _currentMonth = MutableStateFlow<Int>(0)
    val currentMonth: StateFlow<Int> get() = _currentMonth

    private val _currentDay = MutableStateFlow<Int>(0)
    val currentDay: StateFlow<Int> get() = _currentDay

    private val _plantRegisterItem = MutableStateFlow(PlantRegisterItem("", "", "", "", ""))
    val plantRegisterItem: StateFlow<PlantRegisterItem> get() = _plantRegisterItem

    private val _postRegisterPlantState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postRegisterPlantState: StateFlow<UiState<Unit>> = _postRegisterPlantState.asStateFlow()

    private val _plantImage = MutableStateFlow<Uri?>(null)
    val plantImage: StateFlow<Uri?> = _plantImage.asStateFlow()

    private var imageRequestBody: ContentUriRequestBody? = null

    init {
        _currentYear.value = calendar.get(Calendar.YEAR)
        _currentMonth.value = calendar.get(Calendar.MONTH)
        _currentDay.value = calendar.get(Calendar.DAY_OF_MONTH)
        _plantRegisterItem.value = PlantRegisterItem(
            species = "",
            nickname = "",
            shortDescription = "",
            birthDate = "${currentYear.value}-${formatDateToAddZero(currentMonth.value + 1)}-${
            formatDateToAddZero(
                currentDay.value
            )
            }",
            lastWaterDate = "${currentYear.value}-${formatDateToAddZero(currentMonth.value + 1)}-${
            formatDateToAddZero(
                currentDay.value
            )
            }"
        )
    }

    fun postRegisterPlant() {
        viewModelScope.launch {
            _postRegisterPlantState.value = UiState.Loading
            val (request, image) = createRequestBody(plantRegisterItem.value)

            plantRepository.postRegisterPlant(request, image).onSuccess { response ->
                _postRegisterPlantState.value = UiState.Success(response)
                Timber.e("성공 $response")
            }.onFailure { t ->
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Timber.e("HTTP 실패: $errorResponse")
                }
                Timber.e("${t.message}")
                _postRegisterPlantState.value = UiState.Failure("${t.message}")
            }
        }
    }

    private fun createRequestBody(
        plant: PlantRegisterItem
    ): Pair<HashMap<String, RequestBody>, MultipartBody.Part?> {
        val imageFormData = imageRequestBody?.toFormData()
        val plantDto = RequestPostRegisterPlantDto(
            plantInformationId = findPlantInformationId(plant.species),
            nickname = plant.nickname,
            shortDescription = plant.shortDescription,
            birthDate = plant.birthDate,
            lastWaterDate = plant.lastWaterDate
        )

        val plantInfoJsonString =
            Json.encodeToString(RequestPostRegisterPlantDto.serializer(), plantDto)

        val plainTextRequestBody = hashMapOf(
            "request" to plantInfoJsonString.toRequestBody("application/json".toMediaTypeOrNull())
        )
        return Pair(plainTextRequestBody, imageFormData)
    }

    private fun findPlantInformationId(species: String): Int {
        return 1
    }

    fun updatePlantInfo(newPlantRegisterItem: PlantRegisterItem) {
        _plantRegisterItem.value = newPlantRegisterItem
    }

    fun updatePlantImage(uri: Uri, context: Context) {
        _plantImage.value = uri
        val imageUri = plantImage.value ?: return
        val requestBody = ContentUriRequestBody(context, imageUri)
        updateRequestBody(requestBody)
    }

    fun updateRequestBody(requestBody: ContentUriRequestBody) {
        this.imageRequestBody = requestBody
    }

    fun updatePlantSpeciesLength(): Int {
        return plantRegisterItem.value.species.length
    }

    fun checkIsNotEmpty(): Boolean {
        with(plantRegisterItem.value) {
            return nickname.isNotEmpty() && species.isNotEmpty() && shortDescription.isNotEmpty()
        }
    }

    private fun formatDateToAddZero(date: Int): String {
        if (date < 10) {
            return String.format("%02d", date)
        }
        return date.toString()
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
