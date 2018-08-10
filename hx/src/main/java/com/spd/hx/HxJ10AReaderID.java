package com.spd.hx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import cn.ywho.api.decode.DecodeWlt;

public class HxJ10AReaderID {
    public OutputStream m_OutputStream = null;
    public InputStream m_InputStream = null;

    public void setM_OutputStream(OutputStream m_OutputStream) {
        this.m_OutputStream = m_OutputStream;
    }

    public void setM_InputStream(InputStream m_InputStream) {
        this.m_InputStream = m_InputStream;
    }

    protected int m_iRecvBufSize = 1024 * 3;
    protected byte m_bysRecvBuffer[] = new byte[m_iRecvBufSize];
    protected int m_iRecvSize = 0;
    protected int m_iRecvOffset = 0;

    // 是否为外国人身份证
    protected boolean m_bIsForeignerIDCard = false;

    // 居民身份证
    protected String m_strName = null;              //姓名
    protected String m_strSex = null;               //性别
    protected String m_strNation = null;            //民族
    protected String m_strBirth = null;             //生日
    protected String m_strAddr = null;              //地址
    protected String m_strIdCode = null;            //身份证号
    protected String m_strIssue = null;             //签发机关
    protected String m_strBeginDate = null;         //有效期开始日期
    protected String m_strEndDate = null;           //有效期截止日期
    protected Bitmap m_bitmapPicture = null;        //照片
    protected byte[] m_bysFinger = new byte[1024];  //指纹
    protected boolean m_bIsFingerExist = false;     //指纹是否有

    // 外国人身份证
    protected String m_strEnName = null;            // 英文姓名
    protected String m_strEnNation = null;          // 国籍或所在地区代码
    protected String m_strEnCardVersion = null;     // 证件版本
    protected String m_strEnAuthorCode = null;      // 授权机关代码
    protected String m_strEnCardSign = null;        // 证件类型标识

    //
    //读卡
    //
    public int ReadCard() {
        int iResult = 0;
        boolean bResult = true;

        /////////////////////////////////////////////////////////////////////
        //寻卡
        byte bysCmdFind[] = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x01, 0x22};
        try {
            m_OutputStream.write(bysCmdFind);
            m_OutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            iResult = 1; //寻卡失败
        }
        if (0 != iResult) {
            return iResult;
        }
        ReadIDResp(800);       //读取卡应答
        bResult = OnFindIdResp(); //处理寻卡应答
        if (!bResult) {
            iResult = 1; //寻卡失败
            return iResult;
        }

        /////////////////////////////////////////////////////////////////////
        //选卡
        byte bysCmdSelect[] = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x02, 0x21};
        try {
            m_OutputStream.write(bysCmdSelect);
            m_OutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            iResult = 2;  //选卡失败
        }
        if (0 != iResult) {
            return iResult;
        }
        ReadIDResp(300);         //读取卡应答
        bResult = OnSelectIdResp(); //处理选卡应答
        if (!bResult) {
            iResult = 2; //选卡失败
            return iResult;
        }

        /////////////////////////////////////////////////////////////////////
        //读卡
        byte bysCmdRead[] = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x30, 0x10, 0x23};
        try {
            m_OutputStream.write(bysCmdRead);
            m_OutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            iResult = 3;  //读取卡内信息失败
        }
        ReadIDResp(1100);       //读取卡应答
        bResult = OnReadIdResp(); //处理读卡内信息应答
        if (!bResult) {
            iResult = 3;  //读取卡内信息失败
            return iResult;
        }

        /////////////////////////////////////////////////////////////////////
        //返回
        return iResult;
    }

    //
    // 获取是否为外国人身份证
    //
    public boolean GetIDCardType() {
        return m_bIsForeignerIDCard;
    }

    //
    // 获取证件标志
    //
    public String GetCardSign() {
        return m_strEnCardSign;
    }

    //
    // 获取英文姓名
    //
    public String GetEnName() {
        return m_strEnName;
    }

    //
    // 获取国籍
    //
    public String GetCountry() {
        return m_strEnNation;
    }

    //
    // 获取证件版本
    //
    public String GetCardVersion() {
        return m_strEnCardVersion;
    }

    //
    // 获取发证机关代码
    //
    public String GetIssAuthCode() {
        return m_strEnAuthorCode;
    }

    //
    //获取姓名
    //
    public String GetName() {
        return m_strName;
    }

    //
    //获取性别
    //
    public String GetSex() {
        return m_strSex;
    }

    //
    //获取民族
    //
    public String GetNation() {
        return m_strNation;
    }

    //
    //获取出生日期
    //
    public String GetBirth() {
        return m_strBirth;
    }

    //
    //获取住址
    //
    public String GetAddr() {
        return m_strAddr;
    }

    //
    //获取身份证号
    //
    public String GetIDCode() {
        return m_strIdCode;
    }

    //
    //获取签发机关
    //
    public String GetIssue() {
        return m_strIssue;
    }

    //
    //获取有效期开始日期
    //
    public String GetBeginDate() {
        return m_strBeginDate;
    }

    //
    //获取有效期截止日期
    //
    public String GetEndDate() {
        return m_strEndDate;
    }

    //
    //获取照片
    //
    public Bitmap GetPicture() {
        return m_bitmapPicture;
    }

    //
    //获取指纹
    //
    public byte[] GetFinger() {
        if (m_bIsFingerExist) {
            return m_bysFinger;
        }

        return null;
    }

    //
    //读取身份证应答
    //
    protected void ReadIDResp(final long lWaitTotal) {
        int i = 0;
        byte[] buffer = new byte[1024];
        int size = 0;
        //int sizeTotal = 0;

        int iReadTimes = 0;
        int iLen1Len2 = 0;

        //long lWaitTotal = 300;
        //long lWaitStep = 100;
        long lWait = 0;

        int iCanReadSize = 0;

        boolean bIsDataExits = true;

        try {
            //showString("读数据");

            while (true) {
                while (true) {
                    iCanReadSize = m_InputStream.available();
                    if (iCanReadSize > 0) {
                        break;
                    }

                    //读数据超时, 跳出.
                    if (lWait > lWaitTotal) {
                        //showString("数据读完了.\n");
                        bIsDataExits = false;
                        break;
                    }
                    ThreadTool.Sleep((long) 100);
                    lWait += (long) 100;
                }

                if (!bIsDataExits) {
                    break;
                }

                size = m_InputStream.read(buffer);
                if (size <= 0) {
                    //showString("...没读到数据.\n");
                    continue;
                } else {
                    //showString(".");
                    //sizeTotal  += size;
                    iReadTimes++;
                }

                if (1 == iReadTimes) {
                    m_iRecvSize = 0;
                    m_iRecvOffset = 0;
                }

                for (i = 0; i < size; i++) {
                    m_bysRecvBuffer[m_iRecvOffset + i] = buffer[i];
                }
                m_iRecvOffset += size;
                m_iRecvSize = m_iRecvOffset;

                //获取应答长度
                if ((0 == iLen1Len2) && (m_iRecvSize >= 7)) {
                    iLen1Len2 = m_bysRecvBuffer[5] << 8;
                    iLen1Len2 += m_bysRecvBuffer[6];
                }
                //若数据读完, 跳出.
                if ((0 != iLen1Len2) && (m_iRecvSize >= (iLen1Len2 + 7))) {
                    //iCanReadSize = mInputStream.available();
                    //if(0 == iCanReadSize)
                    //{
                    //	break;
                    //}
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }

    //
    //处理身份证寻卡应答
    //
    protected boolean OnFindIdResp() {
        byte SW1 = m_bysRecvBuffer[7];
        byte SW2 = m_bysRecvBuffer[8];
        byte SW3 = m_bysRecvBuffer[9];
        return (0x0 == SW1) && (0x0 == SW2) && (((byte) 0x9F) == SW3);
    }

    //
    //处理身份证选卡应答
    //
    protected boolean OnSelectIdResp() {
        byte SW1 = m_bysRecvBuffer[7];
        byte SW2 = m_bysRecvBuffer[8];
        byte SW3 = m_bysRecvBuffer[9];

        return (0x0 == SW1) && (0x0 == SW2) && (((byte) 0x90) == SW3);
    }

    //
    //处理读卡数据应答
    //
    protected boolean OnReadIdResp() {
        byte SW1 = m_bysRecvBuffer[7];
        byte SW2 = m_bysRecvBuffer[8];
        byte SW3 = m_bysRecvBuffer[9];

        if (m_iRecvSize < 1024) {
            return false;
        }

        if ((0x0 != SW1) || (0x0 != SW2) || (((byte) 0x90) != SW3)) {
            return false;
        }

        int iOffset = 16;
        int iTextSize = 0;
        int iPhotoSize = 0;
        int iFingerSize = 0;

        byte bysName[] = new byte[30];
        byte bysSexCode[] = new byte[2];
        byte bysNationCode[] = new byte[4];
        byte bysBirth[] = new byte[16];
        byte bysAddr[] = new byte[70];
        byte bysIdCode[] = new byte[36];
        byte bysIssue[] = new byte[30];
        byte bysBeginDate[] = new byte[16];
        byte bysEndDate[] = new byte[16];

        // 外国人新增字段
        byte bysNameEN[] = new byte[120];        //英文姓名
        byte bysNationCodeEx[] = new byte[6];    //国籍
        byte bysCardVer[] = new byte[4];        //证件版本
        byte bysIssAuthCode[] = new byte[8];    //发证机关代码
        byte bysCardSign[] = new byte[2];        //证件标志

        iTextSize = m_bysRecvBuffer[10] << 8 + m_bysRecvBuffer[11];
        iPhotoSize = m_bysRecvBuffer[12] << 8 + m_bysRecvBuffer[13];
        iFingerSize = m_bysRecvBuffer[14] << 8 + m_bysRecvBuffer[15];

        //获得证件标志，判断是否为外国人身份证
        //判断类型标识，更新UI显示信息
        //大写字母'I'表示为外国人居住证，卡片返回身份信息数据默认为宽字符，这里采用直接判断字节的方法（宽字符大写字母'I'由2字节组成，分别为0x49 0x00）
        bysCardSign[0] = m_bysRecvBuffer[iOffset + 248];
        bysCardSign[1] = m_bysRecvBuffer[iOffset + 249];

        if ((bysCardSign[0] == 0x49) && (bysCardSign[1] == 0x00)) {
            // 外国人身份证
            m_bIsForeignerIDCard = true;

            //截取英文姓名
            System.arraycopy(m_bysRecvBuffer, iOffset, bysNameEN, 0, 120);
            try {
                m_strEnName = new String(bysNameEN, "UTF-16LE");
                m_strEnName.replace(" ", "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取性别代码
            System.arraycopy(m_bysRecvBuffer, iOffset + 120, bysSexCode, 0, 2);
            try {
                m_strSex = new String(bysSexCode, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            m_strSex = getSexFromCode(m_strSex);

            //截取永久居留证号
            System.arraycopy(m_bysRecvBuffer, iOffset + 122, bysIdCode, 0, 30);
            try {
                m_strIdCode = new String(bysIdCode, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取国籍代码
            System.arraycopy(m_bysRecvBuffer, iOffset + 152, bysNationCodeEx, 0, 6);
            try {
                m_strEnNation = new String(bysNationCodeEx, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取中文姓名
            System.arraycopy(m_bysRecvBuffer, iOffset + 158, bysName, 0, 30);
            try {
                m_strName = new String(bysName, "UTF-16LE");
                m_strName.replace(" ", "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取有效期开始日期
            System.arraycopy(m_bysRecvBuffer, iOffset + 188, bysBeginDate, 0, 16);
            try {
                m_strBeginDate = new String(bysBeginDate, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取有效期结束日期
            System.arraycopy(m_bysRecvBuffer, iOffset + 204, bysEndDate, 0, 16);
            try {
                m_strEndDate = new String(bysEndDate, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取生日
            System.arraycopy(m_bysRecvBuffer, iOffset + 220, bysBirth, 0, 16);
            try {
                m_strBirth = new String(bysBirth, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取证件版本
            System.arraycopy(m_bysRecvBuffer, iOffset + 236, bysCardVer, 0, 4);
            try {
                m_strEnCardVersion = new String(bysCardVer, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取签发机关代码
            System.arraycopy(m_bysRecvBuffer, iOffset + 240, bysIssAuthCode, 0, 8);
            try {
                m_strEnAuthorCode = new String(bysIssAuthCode, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //证件类型标志
            System.arraycopy(m_bysRecvBuffer, iOffset + 248, bysCardSign, 0, 2);
            try {
                m_strEnCardSign = new String(bysCardSign, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // 预留项6字节
        } else {
            // 居民身份证
            m_bIsForeignerIDCard = false;

            //截取姓名
            System.arraycopy(m_bysRecvBuffer, iOffset, bysName, 0, 30);
            try {
                m_strName = new String(bysName, "UTF-16LE");
                m_strName.replace(" ", "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取性别代码
            System.arraycopy(m_bysRecvBuffer, iOffset + 30, bysSexCode, 0, 2);
            String strSexCode = null;
            try {
                strSexCode = new String(bysSexCode, "UTF-16LE");
                m_strSex = getSexFromCode(strSexCode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取民族
            System.arraycopy(m_bysRecvBuffer, iOffset + 32, bysNationCode, 0, 4);
            String strNation = null;
            try {
                strNation = new String(bysNationCode, "UTF-16LE");
                m_strNation = getNationFromCode(strNation);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            m_strSex = getSexFromCode(strSexCode);

            //截取生日
            System.arraycopy(m_bysRecvBuffer, iOffset + 36, bysBirth, 0, 16);
            try {
                m_strBirth = new String(bysBirth, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取地址
            System.arraycopy(m_bysRecvBuffer, iOffset + 52, bysAddr, 0, 70);
            try {
                m_strAddr = new String(bysAddr, "UTF-16LE");
                m_strAddr.replaceAll(" ", "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取身份证号
            System.arraycopy(m_bysRecvBuffer, iOffset + 122, bysIdCode, 0, 36);
            try {
                m_strIdCode = new String(bysIdCode, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取签发机关
            System.arraycopy(m_bysRecvBuffer, iOffset + 158, bysIssue, 0, 30);
            try {
                m_strIssue = new String(bysIssue, "UTF-16LE");
                m_strIssue.replace(" ", "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取有效期开始日期
            System.arraycopy(m_bysRecvBuffer, iOffset + 188, bysBeginDate, 0, 16);
            try {
                m_strBeginDate = new String(bysBeginDate, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //截取有效期截止日期
            System.arraycopy(m_bysRecvBuffer, iOffset + 204, bysEndDate, 0, 16);
            try {
                m_strEndDate = new String(bysEndDate, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        //照片
        byte[] wlt = new byte[1024];
        byte[] bmp = new byte[14 + 40 + 308 * 126];

        //for(i=0; i<iPhotoSize; i++)
        //{
        //	wlt[i] = m_bysRecvBuffer[16+iTextSize+i];
        //}
        System.arraycopy(m_bysRecvBuffer, (16 + iTextSize), wlt, 0, iPhotoSize);


        DecodeWlt.hxgcWlt2Bmp(wlt, bmp, 708);
        m_bitmapPicture = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);

        //指纹
        if (0 == iFingerSize) {
            m_bIsFingerExist = false;
        } else {
            if (iFingerSize > 1024) {
                iFingerSize = 1024;
            }
            System.arraycopy(m_bysRecvBuffer, (16 + iTextSize + iPhotoSize), m_bysFinger, 0, iFingerSize);
            m_bIsFingerExist = true;
        }

        return true;
    }

    /**
     * 将性别代码转换为性别
     *
     * @param strSexCode strSexCode
     * @return 性别
     */
    private String getSexFromCode(final String strSexCode) {
        if ('0' == strSexCode.charAt(0)) {
            return "未知";
        } else if ('1' == strSexCode.charAt(0)) {
            return "男";
        } else if ('2' == strSexCode.charAt(0)) {
            return "女";
        } else if ('9' == strSexCode.charAt(0)) {
            return "未说明";
        }

        return "未定义";
    }

    /**
     * 将民族代码转换为民族名
     *
     * @param strNationCode strNationCode
     * @return 民族
     */
    private String getNationFromCode(final String strNationCode) {
        switch (strNationCode) {
            case "01":
                return "汉";
            case "02":
                return "蒙古";
            case "03":
                return "回";
            case "04":
                return "藏";
            case "05":
                return "维吾尔";
            case "06":
                return "苗";
            case "07":
                return "彝";
            case "08":
                return "壮";
            case "09":
                return "布依";
            case "10":
                return "朝鲜";
            case "11":
                return "满";
            case "12":
                return "侗";
            case "13":
                return "瑶";
            case "14":
                return "白";
            case "15":
                return "土家";
            case "16":
                return "哈尼";
            case "17":
                return "哈萨克";
            case "18":
                return "傣";
            case "19":
                return "黎";
            case "20":
                return "傈僳";
            case "21":
                return "佤";
            case "22":
                return "畲";
            case "23":
                return "高山";
            case "24":
                return "拉祜";
            case "25":
                return "水";
            case "26":
                return "东乡";
            case "27":
                return "纳西";
            case "28":
                return "景颇";
            case "29":
                return "柯尔克孜";
            case "30":
                return "土";
            case "31":
                return "达斡尔";
            case "32":
                return "仫佬";
            case "33":
                return "羌";
            case "34":
                return "布朗";
            case "35":
                return "撒拉";
            case "36":
                return "毛南";
            case "37":
                return "仡佬";
            case "38":
                return "锡伯";
            case "39":
                return "阿昌";
            case "40":
                return "普米";
            case "41":
                return "塔吉克";
            case "42":
                return "怒";
            case "43":
                return "乌孜别克";
            case "44":
                return "俄罗斯";
            case "45":
                return "鄂温克";
            case "46":
                return "德昂";
            case "47":
                return "保安";
            case "48":
                return "裕固";
            case "49":
                return "京";
            case "50":
                return "塔塔尔";
            case "51":
                return "独龙";
            case "52":
                return "鄂伦春";
            case "53":
                return "赫哲";
            case "54":
                return "门巴";
            case "55":
                return "珞巴";
            case "56":
                return "基诺";
            case "97":
                return "其他";
            case "98":
                return "外国血统中国籍人士";
            default:
                return "未知";
        }

    }
}