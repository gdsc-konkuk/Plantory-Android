package kr.ac.konkuk.gdsc.plantory.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kr.ac.konkuk.gdsc.plantory.domain.repository.DataStoreRepository
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    override suspend fun getDeviceToken(): Flow<String>? {
        return dataStore.data
            .map { preferences ->
                preferences[DEVICE_TOKEN]?.let { flowOf(it) }
            }.firstOrNull()
    }

    override suspend fun saveDeviceToken(deviceToken: String) {
        dataStore.edit {
            it[DEVICE_TOKEN] = deviceToken
        }
    }

    override suspend fun clearDataStore() {
        dataStore.edit { it.clear() }
    }

    companion object PreferencesKeys {
        private val DEVICE_TOKEN: Preferences.Key<String> = stringPreferencesKey("device_token")
    }
}
