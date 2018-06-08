package com.drove.wall.gcm;

/**
 * Created by Arjun on 9/12/15.
 */
public interface GcmCallBack {
    void beforeRequest();

    void requestSuccess(String regId);

    void requestFailed(String message);
}
