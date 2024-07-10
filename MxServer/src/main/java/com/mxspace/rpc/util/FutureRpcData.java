package com.mxspace.rpc.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * RPC 回调任务类
 * @param <V>
 */
public class FutureRpcData<V> implements Future<V> {

    boolean isDone;

    boolean isCancel;

    String taskId;

    V result;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (isDone){
            return false;
        }
        if (mayInterruptIfRunning && !isCancel){
            isCancel = Thread.currentThread().isInterrupted();
        }
        return isCancel;
    }

    @Override
    public boolean isCancelled() {
        return isCancel;
    }


    @Override
    public boolean isDone() {
        return isDone;
    }


    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (isCancel){
            return null;
        }
        if (isDone){
            return result;
        }
        synchronized (this){
            this.wait();
        }
        return result;
    }


    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (isCancel){
            return null;
        }
        if (isDone){
            return result;
        }
        synchronized (this){
            this.wait(unit.toMillis(timeout));
        }
        return result;
    }

    public void setResult(V v){
        if (this.isCancel){
            return;
        }
        this.result = v;
        synchronized (this){
            this.notify();
        }
        this.isDone = true;
    }
}