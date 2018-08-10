package com.spd.hx;

public class ThreadTool {
    static public void Sleep(long MillisecondsTime) {
        try {
            Thread.sleep(MillisecondsTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
