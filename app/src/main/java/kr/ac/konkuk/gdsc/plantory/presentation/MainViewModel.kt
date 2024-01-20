package kr.ac.konkuk.gdsc.plantory.presentation

import android.util.Log
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

    fun getDeviceToken() {
        viewModelScope.launch {
            val token = dataStoreRepository.getDeviceToken()
            _deviceToken.emit(token.toString())
        }
    }

}