package kr.ac.konkuk.gdsc.plantory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.domain.repository.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _deviceToken = MutableStateFlow("")
    val deviceToken: Flow<String> = _deviceToken

    init {
        getDeviceToken()
    }

    private fun getDeviceToken() {
        viewModelScope.launch {
            dataStoreRepository.getDeviceToken()?.collect {
                _deviceToken.emit(it)
            }
        }
    }

}