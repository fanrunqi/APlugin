package cn.leo.aplugin.router;

import android.widget.Toast;

import androidx.annotation.NonNull;

import cn.leo.lib_router.annotation.RouteInterceptor;
import cn.leo.lib_router.inter.RouterInterceptor;
import cn.leo.lib_router.process.RouterResponse;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/28 15:08
 * description:
 */
@RouteInterceptor(RouterInterceptor.GLOBAL_INTERCEPTOR)
public class GlobalInterceptor implements RouterInterceptor {

    @NonNull
    @Override
    public RouterResponse intercept(Chain chain) {
        Toast.makeText(chain.getRequest().getContext(), "GlobalInterceptor in", Toast.LENGTH_SHORT).show();
        return chain.process();
    }
}