package cn.leo.aplugin.mock

import androidx.annotation.Keep

@Keep
data class BaseResponse<T>(
    var code: Int,
    var data: T
)

