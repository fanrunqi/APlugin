package cn.leo.lib_router.inter;

import androidx.annotation.NonNull;

import cn.leo.lib_router.process.RouterRequest;
import cn.leo.lib_router.process.RouterResponse;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/20 16:27
 * description:
 */
public interface RouterInterceptor {
    String GLOBAL_INTERCEPTOR = "global_interceptor";

    @NonNull
    RouterResponse intercept(Chain chain);

    interface Chain {
        @NonNull
        RouterRequest getRequest();

        @NonNull
        RouterResponse process();

        @NonNull
        RouterResponse intercept();
    }
}
