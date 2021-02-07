package cn.leo.lib_router.process;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leo.lib_router.Interceptor.HandleDataInterceptor;
import cn.leo.lib_router.Interceptor.InterceptorChainImp;
import cn.leo.lib_router.Interceptor.RouterMatchInterceptor;
import cn.leo.lib_router.inter.RouteResponseListener;
import cn.leo.lib_router.inter.RouterInterceptor;
import cn.leo.lib_router.Interceptor.MatchInterceptorInterceptor;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/2/1 14:47
 * description:
 */
public class RouterRequest {
    private String mPath;
    private String mPackage;
    private Context mContext;
    private Integer mRequestCode;
    private RouteResponseListener mResponseListener;
    private final Intent mIntent = new Intent();
    private final Map<String, Object> mExtraMap = new HashMap<>();
    private final List<RouterInterceptor> mInterceptors = new ArrayList<>();

    public RouterRequest() {
        mInterceptors.add(new RouterMatchInterceptor());
        mInterceptors.add(new HandleDataInterceptor());
        mInterceptors.add(new MatchInterceptorInterceptor());
    }

    public void setResponseListener(RouteResponseListener listener) {
        mResponseListener = listener;
    }

    public Context getContext() {
        return mContext;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getPath() {
        return mPath;
    }

    public String getPackageName() {
        return mPackage;
    }

    public void setRequestCode(int requestCode) {
        mRequestCode = requestCode;
    }

    public Map<String, Object> getExtraMap() {
        return mExtraMap;
    }

    public <T> void putExtra(String key, T value) {
        if (value instanceof Integer) {
            mIntent.putExtra(key, (Integer) value);
        } else if (value instanceof String) {
            mIntent.putExtra(key, (String) value);
        } else if (value instanceof Boolean) {
            mIntent.putExtra(key, (Boolean) value);
        } else if (value instanceof Double) {
            mIntent.putExtra(key, (Double) value);
        } else if (value instanceof Long) {
            mIntent.putExtra(key, (Long) value);
        } else if (value instanceof Short) {
            mIntent.putExtra(key, (Short) value);
        } else if (value instanceof Float) {
            mIntent.putExtra(key, (Float) value);
        } else if (value instanceof Byte) {
            mIntent.putExtra(key, (Byte) value);
        } else if (value instanceof Character) {
            mIntent.putExtra(key, (Character) value);
        } else {
            mExtraMap.put(key, value);
        }
    }

    public void setAction(@Nullable String action) {
        mIntent.setAction(action);
    }

    public void addCategory(String category) {
        mIntent.addCategory(category);
    }

    public void setData(@Nullable Uri data) {
        mIntent.setData(data);
    }

    public void setFlags(int flags) {
        mIntent.setFlags(flags);
    }

    public void addInterceptor(List<RouterInterceptor> interceptor) {
        mInterceptors.addAll(interceptor);
    }

    public Intent getIntent() {
        return mIntent;
    }


    public void start(@Nullable final Context context) {
        mContext = context;
        mPackage = context.getPackageName();
        InterceptorChainImp chainActual = new InterceptorChainImp(this, mInterceptors);
        RouterResponse response = chainActual.process();
        if (response.getStatus().isSuccessful()) {
            try {
                if ((mRequestCode != null) && (context instanceof Activity)) {
                    if (mRequestCode < 0) {
                        response.setContent("the requestCode must >= 0", RouteStatus.FAILED);
                    } else {
                        ((Activity) context).startActivityForResult(mIntent, mRequestCode);
                    }
                } else {
                    context.startActivity(mIntent);
                }
            } catch (ActivityNotFoundException e) {
                response.setContent("ActivityNotFoundException with: " + e.toString(), RouteStatus.FAILED);
            }
        }
        if (mResponseListener != null) {
            mResponseListener.processEnd(response);
        }
    }
}