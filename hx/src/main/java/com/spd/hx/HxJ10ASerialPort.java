package com.spd.hx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

public class HxJ10ASerialPort {
    static private SerialPort mSerialPort = null;
    static private OutputStream mOutputStream = null;
    static private InputStream mInputStream = null;

    // Get
    public static SerialPort getSerialPort() {
        return mSerialPort;
    }

    public static OutputStream getmOutputStream() {
        return mOutputStream;
    }

    public static InputStream getmInputStream() {
        return mInputStream;
    }

    ///////////////////////////////////////////////////////////////

    //打开串口
    public static void openSerialPort(String SerialPortPath) {
        if (mSerialPort == null) {
            String path = SerialPortPath;
            int baudrate = 115200;

            try {
                mSerialPort = new SerialPort(new File(path), baudrate, 0);
                if (mSerialPort != null) {
                    mOutputStream = mSerialPort.getOutputStream();
                    mInputStream = mSerialPort.getInputStream();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //关闭串口
    public static void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
            mOutputStream = null;
            mInputStream = null;
        }
    }
}

