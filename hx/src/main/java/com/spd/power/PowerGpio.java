package com.spd.power;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;


/**
 * @author :Reginer in  2017/7/6 2:55.
 * 联系方式:QQ:282921012
 * 功能描述:gpio工具类
 */
public class PowerGpio {
    private BufferedWriter mControlFile;

    /**
     * 初始化.
     *
     * @param path gpio路径
     */
    public PowerGpio(@PowerType String path) {
        File gpioFile = new File(path);
        try {
            mControlFile = new BufferedWriter(new FileWriter(gpioFile, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主机gpio上电.
     *
     * @param gpio gpio
     * @return 结果
     */
    public boolean powerOnDevice(int... gpio) {
        for (int aGpio : gpio) {
            try {
                powerOnDevice(aGpio);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 主机gpio下电.
     *
     * @param gpio gpio
     * @return 结果
     */
    public boolean powerOffDevice(int... gpio) {
        for (int aGpio : gpio) {
            try {
                powerOffDevice(aGpio);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 外部扩展gpio上电.
     *
     * @param gpio gpio
     * @return 结果
     */
    boolean powerOnDeviceOut(int... gpio) {
        for (int aGpio : gpio) {
            try {
                powerOnDeviceOut(aGpio);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 外部扩展gpio下电.
     *
     * @param gpio gpio
     * @return 结果
     */
    boolean powerOffDeviceOut(int... gpio) {
        for (int aGpio : gpio) {
            try {
                powerOffDeviceOut(aGpio);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 主机gpio上电.
     *
     * @param gpio gpio
     */
    private void powerOnDevice(int gpio) throws IOException {
        //将GPIO99设置为GPIO模式
        mControlFile.write("-wmode" + gpio + " 0");
        mControlFile.flush();
        //将GPIO99设置为输出模式
        mControlFile.write("-wdir" + gpio + " 1");
        mControlFile.flush();
        //上电IO口调整
        mControlFile.write("-wdout" + gpio + " 1");
        mControlFile.flush();
    }

    /**
     * 主机gpio下电.
     *
     * @param gpio gpio
     */
    private void powerOffDevice(int gpio) throws IOException {
        mControlFile.write("-wmode" + gpio + " 0");
        mControlFile.flush();
        //将GPIO99设置为输出模式
        mControlFile.write("-wdir" + gpio + " 1");
        mControlFile.flush();
        //下电IO口调整
        mControlFile.write("-wdout" + gpio + " 0");
        mControlFile.flush();
    }

    /**
     * 外部扩展gpio上电.
     *
     * @param gpio gpio
     */
    private void powerOnDeviceOut(int gpio) throws IOException {
        //上电IO口调整
        mControlFile.write(gpio + "on");
        mControlFile.flush();
    }

    /**
     * 外部扩展gpio下电.
     *
     * @param gpio gpio
     */
    private void powerOffDeviceOut(int gpio) throws IOException {
        //下电IO口调整
        mControlFile.write(gpio + "off");
        mControlFile.flush();
    }

    /**
     * 主板上电路径
     */
    public static final String MAIN = "sys/class/misc/mtgpio/pin";
    /**
     * 外部扩展上电路径
     */
    public static final String OUT = "sys/class/misc/aw9523/gpio";

    @StringDef({MAIN, OUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PowerType {
    }
}
