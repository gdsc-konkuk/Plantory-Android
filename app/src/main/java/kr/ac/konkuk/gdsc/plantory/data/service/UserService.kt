package kr.ac.konkuk.gdsc.plantory.data.service

import kr.ac.konkuk.gdsc.plantory.data.dto.request.RequestGetUser
import kr.ac.konkuk.gdsc.plantory.data.dto.response.ResponseGetUser
import retrofit2.http.Body
import retrofit2.http.GET

interface UserService {
    @GET("")
    suspend fun getUserData(
        @Body requestGetUser: RequestGetUser
    ): ResponseGetUser
}