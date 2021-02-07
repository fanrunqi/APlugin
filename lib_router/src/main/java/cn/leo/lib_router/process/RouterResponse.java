package cn.leo.lib_router.process;

import androidx.annotation.NonNull;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/22 15:12
 * description:
 */
public final class RouterResponse {
    private RouteStatus mStatus;
    private String mMessage;

    public RouterResponse() {
        mStatus = RouteStatus.PROCESSING;
    }

    public RouterResponse(String msg, RouteStatus status) {
        mMessage = msg;
        mStatus = status;
    }

    public RouteStatus getStatus() {
        return mStatus;
    }

    public void setContent(String msg, RouteStatus status) {
        mMessage = msg;
        mStatus = status;
    }

    public void setStatus(@NonNull RouteStatus status) {
        this.mStatus = status;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String msg) {
        mMessage = msg;
    }

}
