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
@RouteInterceptor({"NeedLogin/"})
public class LoginInterceptor implements RouterInterceptor {

    @NonNull
    @Override
    public RouterResponse intercept(Chain chain) {
        Toast.makeText(chain.getRequest().getContext(), "NeedLogin Interceptor in", Toast.LENGTH_SHORT).show();
        return chain.process();
    }
}