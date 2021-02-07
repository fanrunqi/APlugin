package cn.leo.aplugin.execute


/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/28 9:02
 * description:
 */
class RouteExecute {
    public static Map<String, String> mRouteMap = new HashMap<>()
    public static Map<String, String> mInterceptorMap = new HashMap<>()

    static void addRouteRule(String mClass, String mTag) {
        String classPath = mClass.replaceAll("/", ".")
        mRouteMap.put(mTag, classPath)
    }

    static void addInterceptorRule(String mClass, String mTag) {
        String classPath = mClass.replaceAll("/", ".")
        mInterceptorMap.put(mTag, classPath)
    }

    static void clear(){
        mRouteMap.clear()
        mInterceptorMap.clear()
    }
}