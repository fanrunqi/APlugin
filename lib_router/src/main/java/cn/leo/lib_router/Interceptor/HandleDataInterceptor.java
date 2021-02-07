package cn.leo.lib_router.Interceptor;

import androidx.annotation.NonNull;

import cn.leo.lib_router.data.IntentDataProcess;
import cn.leo.lib_router.inter.RouterInterceptor;
import cn.leo.lib_router.process.RouterResponse;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/28 13:59
 * description:
 */
public class HandleDataInterceptor implements RouterInterceptor {

    @NonNull
    @Override
    public RouterResponse intercept(Chain chain) {
        RouterResponse response = IntentDataProcess.convertDataIn(chain.getRequest());
        return response.getStatus().isProcessing() ? chain.process() : response;
    }
}