package kr.ac.konkuk.gdsc.plantory.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun getDeviceToken(): Flow<String>?

    suspend fun saveDeviceToken(deviceToken: String = "")

    suspend fun clearDataStore()
}