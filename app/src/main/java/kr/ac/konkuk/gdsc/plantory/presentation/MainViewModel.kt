package kr.ac.konkuk.gdsc.plantory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostRegisterUserDto
import kr.ac.konkuk.gdsc.plantory.domain.repository.DataStoreRepository
import kr.ac.konkuk.gdsc.plantory.domain.repository.UserRepository
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _postRegisterUserState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postRegisterUserState: StateFlow<UiState<Unit>> = _postRegisterUserState.asStateFlow()

    suspend fun getDeviceToken(): String? {
        return dataStoreRepository.getDeviceToken()?.first()
    }

    fun postRegisterUser(deviceToken: String) {
        viewModelScope.launch {
            userRepository.postRegisterUser(
                RequestPostRegisterUserDto(
                    deviceToken = deviceToken
                )
            ).onSuccess { response ->
                _postRegisterUserState.value = UiState.Success(response)
            }.onFailure { t ->
                _postRegisterUserState.value = UiState.Failure("${t.message}")
            }
        }
    }
}