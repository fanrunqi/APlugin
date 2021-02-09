package cn.leo.aplugin.mock

import retrofit2.Call
import retrofit2.http.GET

interface RequestService {

    @GET(IUrl.getRightApi)
    fun getRightApi(): Call<BaseResponse<String>>
}