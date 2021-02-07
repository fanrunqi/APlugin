package cn.leo.lib_router.Interceptor;

import androidx.annotation.NonNull;

import java.util.List;

import cn.leo.lib_router.inter.RouterInterceptor;
import cn.leo.lib_router.process.RouteStatus;
import cn.leo.lib_router.process.RouterRequest;
import cn.leo.lib_router.process.RouterResponse;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/22 14:26
 * description:
 */
public final class InterceptorChainImp implements RouterInterceptor.Chain {

    @NonNull
    private final RouterRequest mRequest;
    @NonNull
    private final List<RouterInterceptor> mInterceptors;

    public InterceptorChainImp(
            @NonNull RouterRequest request,
            @NonNull List<RouterInterceptor> interceptors) {
        this.mRequest = request;
        this.mInterceptors = interceptors;
    }

    @NonNull
    @Override
    public RouterRequest getRequest() {
        return mRequest;
    }

    @Override
    public RouterResponse process() {
        if (mInterceptors.isEmpty()) {
            return new RouterResponse("SUCCEED", RouteStatus.SUCCEED);
        }
        return mInterceptors.remove(0).intercept(this);
    }

    @NonNull
    @Override
    public RouterResponse intercept() {
        return new RouterResponse("INTERCEPTED", RouteStatus.INTERCEPTED);
    }
}