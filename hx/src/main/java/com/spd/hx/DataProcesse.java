package com.spd.hx;

public class DataProcesse {
    // 日期格式化
    public static String DateConvert(String UnFormatDate) {
        String sResult = null;

        sResult = UnFormatDate.substring(0, 4) + "年" + UnFormatDate.substring(4, 6) + "月" + UnFormatDate.substring(6, 8) + "日";

        return sResult;
    }
}
