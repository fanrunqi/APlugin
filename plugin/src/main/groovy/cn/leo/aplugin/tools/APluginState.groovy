package cn.leo.aplugin.tools

import org.apache.http.util.TextUtils

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/2/8 16:46
 * description:
 */
class APluginState {
    static String FUNCTION_ROUTER = "router"
    static String FUNCTION_MOCK = "mock"

    static boolean isPluginEnable = true

    static boolean isRouterEffect = true
    static boolean isMockEffect = true

    static void readConfig() {
        def disableFunctionsStr = System.properties['APlugin.disableFunctions'].toString()
        if(TextUtils.isEmpty(disableFunctionsStr)){
            return
        }
        isRouterEffect = !disableFunctionsStr.contains(FUNCTION_ROUTER)
        isMockEffect = !disableFunctionsStr.contains(FUNCTION_MOCK)
        isPluginEnable = (isRouterEffect || isMockEffect)
    }
}