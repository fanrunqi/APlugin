package cn.leo.lib_router;

import android.net.Uri;

import androidx.annotation.Nullable;

import cn.leo.lib_router.inter.IRouter;
import cn.leo.lib_router.inter.RouteResponseListener;
import cn.leo.lib_router.process.RouterRequest;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/28 14:39
 * description:
 */
abstract class abstractRouter implements IRouter {
    RouterRequest mRequest;

    abstractRouter() {
        mRequest = new RouterRequest();
    }

    void setPath(String path) {
        mRequest.setPath(path);
    }

    void addCategory(String category) {
        mRequest.addCategory(category);
    }

    void putExtra(String key, Object value) {
        mRequest.putExtra(key, value);
    }

    void setAction(@Nullable String action) {
        mRequest.setAction(action);
    }

    public void setResponseListener(RouteResponseListener listener){
        mRequest.setResponseListener(listener);
    }
    void setData(@Nullable Uri data) {
        mRequest.setData(data);
    }

    void setFlags(int flags) {
        mRequest.setFlags(flags);
    }

    void setRequestCode(int requestCode) {
        mRequest.setRequestCode(requestCode);
    }
}