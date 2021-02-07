package cn.leo.lib_router.data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leo.lib_router.inter.RouterInterceptor;
import cn.leo.lib_router.process.RouteStatus;
import cn.leo.lib_router.process.RouterRequest;
import cn.leo.lib_router.process.RouterResponse;
import cn.leo.lib_router.InjectByteRouterData;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/22 11:27
 * description:
 */
public enum InterceptorTable {
    INSTANCE;

    private final Map<String, String> mInterceptorMap = new HashMap<>();

    private void initTable() {
        if (mInterceptorMap.isEmpty()) {
            InjectByteRouterData.injectInterceptorList(mInterceptorMap);
        }
    }

    public RouterResponse matchInterceptor(RouterRequest request) {
        initTable();
        RouterResponse response = new RouterResponse();
        String path = request.getPath();
        List<RouterInterceptor> interceptors = new ArrayList<>();
        for (Map.Entry<String, String> entry : mInterceptorMap.entrySet()) {
            if (path.contains(entry.getKey()) ||
                    entry.getKey().equals(RouterInterceptor.GLOBAL_INTERCEPTOR)) {
                try {
                    Class clazz = Class.forName(entry.getValue());
                    Object interceptor = clazz.newInstance();
                    interceptors.add((RouterInterceptor) interceptor);
                } catch (Exception e) {
                    response.setContent("custom interceptor resolution failed", RouteStatus.FAILED);
                }
            }
        }
        request.addInterceptor(interceptors);
        return response;
    }


}