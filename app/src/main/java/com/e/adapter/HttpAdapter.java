package com.e.adapter;

import org.xutils.common.Callback;

/**
 * 一个适配器用于xutils的http请求
 * @param <T>
 */
public class HttpAdapter<T> implements Callback.CommonCallback<T> {
    @Override
    public void onSuccess(T result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
