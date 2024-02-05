package kr.ac.konkuk.gdsc.plantory.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.ac.konkuk.gdsc.plantory.data.service.PlantService
import kr.ac.konkuk.gdsc.plantory.data.service.UserService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun providePlantService(retrofit: Retrofit): PlantService =
        retrofit.create(PlantService::class.java)
}
