package kr.ac.konkuk.gdsc.plantory.data.service

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestPostRegisterUserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @POST("api/v1/members")
    suspend fun postRegisterUser(
        @Body requestPostRegisterUserDto: RequestPostRegisterUserDto
    )
}