package com.spd.hx;

import android.util.Log;

import java.lang.Thread.State;

public class BaseThread {
    // 线程句柄
    protected Thread mWorkingThread = null;
    // 线程名称
    protected String mThreadName = null;
    // 线程是否运行
    protected boolean mIsAlive = false;
    // 线程状态
    protected State mStatus = null;

    // Get 和  Set
    public State getmStatus() {
        return mStatus;
    }

    public void setmStatus(State mStatus) {
        this.mStatus = mStatus;
    }

    public boolean ismIsAlive() {
        return mIsAlive;
    }

    public String getmThreadName() {
        return mThreadName;
    }

    public void setmThreadName(String mThreadName) {
        this.mThreadName = mThreadName;
    }

    ////////////////////////////////////////////////////

    // 构造函数创建线程
    public BaseThread() {
        mWorkingThread = new Thread() {
            @Override
            public void run() {
                //这里写入子线程需要做的工作
                Log.i(GlobalDefine.TAG, "线程开始执行");
                ThreadExecute();
                Log.i(GlobalDefine.TAG, "线程结束");
            }
        };
        mIsAlive = false;
        mStatus = State.NEW;
        Log.i(GlobalDefine.TAG, "线程创建");
    }

    // 线程开始
    public void Start() {
        try {
            mWorkingThread.start();
            Log.i(GlobalDefine.TAG, "线程开始运行");
            mThreadName = mWorkingThread.getName();
            mStatus = State.RUNNABLE;
            mIsAlive = true;
        } catch (Exception ex) {
            if (mWorkingThread.getState() == State.RUNNABLE) {
                mWorkingThread.interrupt();
            }
            mIsAlive = false;
            mStatus = State.TERMINATED;
        }
    }

    // 线程停止
    public void Stop() {
        mWorkingThread.interrupt();
        mIsAlive = false;
        mStatus = State.TERMINATED;
    }

    // 外部实现方法
    public void ThreadExecute() {

    }
}
