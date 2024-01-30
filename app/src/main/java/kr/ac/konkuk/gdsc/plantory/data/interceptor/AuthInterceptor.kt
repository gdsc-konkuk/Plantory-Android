package kr.ac.konkuk.gdsc.plantory.data.interceptor

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kr.ac.konkuk.gdsc.plantory.domain.repository.DataStoreRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    @ApplicationContext context: Context,
    private val json: Json,
    private val dataStoreRepository: DataStoreRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        runBlocking { Timber.d("deviceToken : ${getDeviceToken()}") }
        val originalRequest = chain.request()

        val headerRequest = originalRequest.newAuthBuilder()
            .build()

        return chain.proceed(headerRequest)
    }

    private fun Request.newAuthBuilder() =
        this.newBuilder().addHeader(DEVICE_TOKEN, runBlocking(Dispatchers.IO) { getDeviceToken() })

    private suspend fun getDeviceToken(): String {
        return dataStoreRepository.getDeviceToken()?.first() ?: ""
    }

    companion object {
        private const val DEVICE_TOKEN = "Device-Token"
    }
}
