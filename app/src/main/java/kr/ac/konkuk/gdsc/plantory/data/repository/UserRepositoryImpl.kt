package kr.ac.konkuk.gdsc.plantory.data.repository

import kr.ac.konkuk.gdsc.plantory.data.source.UserDataSource
import kr.ac.konkuk.gdsc.plantory.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
}