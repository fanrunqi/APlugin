package cn.leo.lib_mock

import okhttp3.*

/**
 * Author: leo
 * Date: 2020/12/16 10:58
 * Description:
 */
class MockDataInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestUrl = chain.request().url().toString()
        val responseJson = MockDataManager.getMockResponseJson(requestUrl)

        return if (responseJson.isNotEmpty()) {
            val body = ResponseBody.create(MediaType.parse("application/json"), responseJson)
            Response.Builder()
                .body(body)
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .message("Mock data from MockDataInterceptor")
                .code(200)
                .build()
        } else chain.proceed(chain.request())
    }

}