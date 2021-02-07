package cn.leo.lib_router;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;

import cn.leo.lib_router.inter.IRouter;
import cn.leo.lib_router.inter.IntentInterceptor;
import cn.leo.lib_router.inter.RouteResponseListener;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/20 16:36
 * description:
 */
public final class Router extends abstractRouter {

    private Router() {
        super();
    }

    public static Builder Builder() {
        return new Builder();
    }

    @Override
    public void start(@Nullable Context context) {
        mRequest.start(context);
    }


    public static class Builder {
        private final Router router;

        private Builder() {
            router = new Router();
        }

        public IRouter build() {
            return router;
        }

        public Builder setPath(String path) {
            router.setPath(path);
            return this;
        }

        public Builder putExtra(String key, Object value) {
            router.putExtra(key, value);
            return this;
        }

        public Builder setAction(@Nullable String action) {
            router.setAction(action);
            return this;
        }

        public Builder addCategory(@Nullable String category) {
            router.addCategory(category);
            return this;
        }

        public Builder setData(@Nullable Uri data) {
            router.setData(data);
            return this;
        }

        public Builder setFlags(int flags) {
            router.setFlags(flags);
            return this;
        }

        public Builder setRequestCode(int requestCode) {
            router.setRequestCode(requestCode);
            return this;
        }

        public Builder setResponseListener(RouteResponseListener listener) {
            router.setResponseListener(listener);
            return this;
        }

        public Builder setIntentInterceptor(IntentInterceptor callBack) {
            callBack.process(router.mRequest.getIntent());
            return this;
        }
    }
}