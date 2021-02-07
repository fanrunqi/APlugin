package cn.leo.lib_router.inter;

import cn.leo.lib_router.process.RouterResponse;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/28 14:30
 * description:
 */
public interface RouteResponseListener {
    void processEnd(RouterResponse response);
}
