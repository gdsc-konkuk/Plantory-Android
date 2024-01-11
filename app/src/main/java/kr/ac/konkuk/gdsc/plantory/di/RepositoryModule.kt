package kr.ac.konkuk.gdsc.plantory.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.ac.konkuk.gdsc.plantory.data.repository.UserRepositoryImpl
import kr.ac.konkuk.gdsc.plantory.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

}