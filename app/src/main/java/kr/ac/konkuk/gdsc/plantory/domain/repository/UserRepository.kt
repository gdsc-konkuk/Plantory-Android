package kr.ac.konkuk.gdsc.plantory.domain.repository

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostRegisterUserDto

interface UserRepository {
    suspend fun postRegisterUser(requestPostRegisterUserDto: RequestPostRegisterUserDto): Result<Unit>
}