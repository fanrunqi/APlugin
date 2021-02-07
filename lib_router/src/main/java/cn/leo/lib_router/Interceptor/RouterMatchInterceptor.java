package cn.leo.lib_router.Interceptor;

import androidx.annotation.NonNull;

import cn.leo.lib_router.data.RouterTable;
import cn.leo.lib_router.inter.RouterInterceptor;
import cn.leo.lib_router.process.RouterResponse;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/28 13:59
 * description:
 */
public class RouterMatchInterceptor implements RouterInterceptor {

    @NonNull
    @Override
    public RouterResponse intercept(Chain chain) {
        RouterResponse response = RouterTable.INSTANCE.matchComponent(chain.getRequest());
        return response.getStatus().isProcessing() ? chain.process() : response;
    }
}