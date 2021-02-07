package cn.leo.aplugin.tools

interface Constant {
    String ANNOTATION_DESCRIPTOR_ROUTER_PATHS = "Lcn/leo/lib_router/annotation/Route;"
    String ANNOTATION_DESCRIPTOR_ROUTER_INTERCEPTOR = "Lcn/leo/lib_router/annotation/RouteInterceptor;"
    String ANNOTATION_DESCRIPTOR_ROUTER_PARAM = "Lcn/leo/lib_router/annotation/RouteParam;"

    String JAR_MODIFY_CLASS_ENTRY_NAME_SUFFIX = "InjectByteRouterData.class"
    String JAR_MODIFY_CLASS_METHOD_NAME_INJECT_ROUTER_LIST = "injectRouterList"
    String JAR_MODIFY_CLASS_METHOD_NAME_INJECT_INTERCEPTOR_LIST = "injectInterceptorList"

    String FUNCTION_ROUTER = "router"
    String FUNCTION_ROUTER_LIBRARY = 'com.github.fanrunqi.APlugin:lib_router:+'
    String FUNCTION_ROUTER_JAR_INPUT_MATCH_SUFFIX = "lib_router"
    String FUNCTION_MOCK = "mock"
}