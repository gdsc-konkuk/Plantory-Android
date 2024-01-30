package kr.ac.konkuk.gdsc.plantory.data.source

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostRegisterUserDto
import kr.ac.konkuk.gdsc.plantory.data.service.UserService
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun postRegisterUser(requestPostRegisterUserDto: RequestPostRegisterUserDto) =
        userService.postRegisterUser(requestPostRegisterUserDto)
}
