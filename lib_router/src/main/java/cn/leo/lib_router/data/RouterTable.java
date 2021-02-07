package cn.leo.lib_router.data;

import android.content.Intent;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import cn.leo.lib_router.InjectByteRouterData;
import cn.leo.lib_router.process.RouteStatus;
import cn.leo.lib_router.process.RouterRequest;
import cn.leo.lib_router.process.RouterResponse;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/22 15:36
 * description:
 */
public enum RouterTable {
    INSTANCE;

    private final Map<String, String> mRouterMap = new HashMap<>();

    private void initTable() {
        if (mRouterMap.isEmpty()) {
            InjectByteRouterData.injectRouterList(mRouterMap);
        }
    }

    public RouterResponse matchComponent(RouterRequest request) {
        initTable();
        RouterResponse response = new RouterResponse();
        Intent intent = request.getIntent();
        if (intent.getComponent() != null) {
            return response;
        }
        String path = request.getPath();
        String className = mRouterMap.get(path);
        if (!TextUtils.isEmpty(path) && TextUtils.isEmpty(className)) {
            response.setContent("Path matching failed, the corresponding class was not found", RouteStatus.NOT_FOUND);
        }
        if (!TextUtils.isEmpty(className)) {
            intent.setClassName(request.getPackageName(), className);
        }
        return response;
    }
}