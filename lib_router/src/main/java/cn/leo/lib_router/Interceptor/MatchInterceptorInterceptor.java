package cn.leo.lib_router.Interceptor;

import androidx.annotation.NonNull;

import cn.leo.lib_router.data.InterceptorTable;
import cn.leo.lib_router.inter.RouterInterceptor;
import cn.leo.lib_router.process.RouterResponse;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/28 13:59
 * description:
 */
public class MatchInterceptorInterceptor implements RouterInterceptor {

    @NonNull
    @Override
    public RouterResponse intercept(Chain chain) {
        RouterResponse response = InterceptorTable.INSTANCE.matchInterceptor(chain.getRequest());
        return response.getStatus().isProcessing() ? chain.process() : response;
    }
}