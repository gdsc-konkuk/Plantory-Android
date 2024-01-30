package kr.ac.konkuk.gdsc.plantory.data.repository

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostRegisterUserDto
import kr.ac.konkuk.gdsc.plantory.data.source.UserDataSource
import kr.ac.konkuk.gdsc.plantory.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun postRegisterUser(requestPostRegisterUserDto: RequestPostRegisterUserDto) : Result<Unit> =
        runCatching {
            userDataSource.postRegisterUser(requestPostRegisterUserDto)
        }
}