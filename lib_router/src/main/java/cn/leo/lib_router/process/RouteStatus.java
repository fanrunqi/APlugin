package cn.leo.lib_router.process;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/28 13:58
 * description:
 */
public enum RouteStatus {
    PROCESSING,
    SUCCEED,
    INTERCEPTED,
    NOT_FOUND,
    FAILED;

    public boolean isSuccessful() {
        return this == SUCCEED;
    }

    public boolean isProcessing() {
        return this == PROCESSING;
    }
}