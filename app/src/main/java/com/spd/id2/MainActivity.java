package com.spd.id2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spd.hx.DataProcesse;
import com.spd.hx.HxJ10AReaderID;
import com.spd.hx.HxJ10ASerialPort;
import com.spd.hx.ThreadReadIDLoop;
import com.spd.power.PowerGpio;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import android_serialport_api.SerialPort;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SerialPort mSerialPort = null;
    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;
    private HxJ10AReaderID mReaderID = null;
    private ThreadReadIDLoop mReadLoop = null;
    private static final String TAG = "Reginer";
    private PowerGpio mGpio;
    private TextView mIdInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIdInfo = findViewById(R.id.IdInfo);
        HxJ10ASerialPort.openSerialPort("/dev/ttyMT1");
        mSerialPort = HxJ10ASerialPort.getSerialPort();
        mOutputStream = HxJ10ASerialPort.getmOutputStream();
        mInputStream = HxJ10ASerialPort.getmInputStream();
        mGpio = new PowerGpio(PowerGpio.MAIN);
        mGpio.powerOnDevice(93);
        PlaySoundUtils.initSoundPool(this);
        onClick();
    }

    private void onClick() {
        findViewById(R.id.singleRead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReaderID = new HxJ10AReaderID();

                mReaderID.setInputStream(mInputStream);
                mReaderID.setOutputStream(mOutputStream);

                int iRet = mReaderID.readCard();
                if (iRet == 1) {
                    Log.d(TAG, "寻卡失败");
                    return;
                }
                if (iRet == 2) {
                    Log.d(TAG, "选卡失败");
                    return;
                }
                if (iRet == 3) {
                    Log.d(TAG, "读取卡内信息失败");
                    return;
                }
                // 居民身份证
                if (!mReaderID.getIdCardType()) {
                    // 姓名
                    String readIdName = mReaderID.GetName();
                    // 性别
                    String readIdSex = mReaderID.GetSex();
                    // 民族
                    String readIdNation = mReaderID.GetNation();
                    // 出生日期
                    String readIdBirth = mReaderID.GetBirth();
                    // 住址
                    String readIdAddress = mReaderID.GetAddr();
                    // 身份证号
                    String readIdCode = mReaderID.GetIDCode();
                    // 签发机关
                    String readIdIssue = mReaderID.GetIssue();
                    // 有效日期起始
                    String readIdBeginDate = mReaderID.GetBeginDate();
                    // 有效日期截止
                    String readIdEndDate = mReaderID.GetEndDate();
                    // 照片
                    Bitmap readIdPicture = mReaderID.GetPicture();
                    String stringBuffer = "姓名：" + readIdName + "\r\n" +
                            "性别：" + readIdSex + "\r\n" +
                            "民族：" + readIdNation + "\r\n" +
                            "出生日期：" + DataProcesse.DateConvert(readIdBirth) + "\r\n" +
                            "地址：" + readIdAddress +
                            "身份证号：" + readIdCode + "\r\n" +
                            "签发机关：" + readIdIssue + "\r\n" +
                            "有效期起始：" + DataProcesse.DateConvert(readIdBeginDate) + "\r\n" +
                            "有效期截止：" + DataProcesse.DateConvert(readIdEndDate) + "\r\n";
                    mIdInfo.setText(stringBuffer);
                    ImageSpan imgSpan = new ImageSpan(MainActivity.this, readIdPicture);
                    final SpannableString spanString = new SpannableString("icon");
                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mIdInfo.append(spanString);
                }
                // 外国人身份证
                else if (mReaderID.getIdCardType()) {
                    // 英文姓名
                    String readIdEnName = mReaderID.GetEnName();
                    // 性别
                    String readIdSex = mReaderID.GetSex();
                    // 永久居留证号码
                    String readIdCode = mReaderID.GetIDCode();
                    // 国籍
                    String readIdCountry = mReaderID.GetCountry();
                    // 中文姓名
                    String readIdName = mReaderID.GetName();
                    // 有效日期起始
                    String readIdBeginDate = mReaderID.GetBeginDate();
                    // 有效日期截止
                    String readIdEndDate = mReaderID.GetEndDate();
                    // 出生日期
                    String readIdBirth = mReaderID.GetBirth();
                    // 证件版本
                    @SuppressWarnings("unused")
                    String readIdCardVersion = mReaderID.GetCardVersion();
                    // 授权机关代码
                    String readIdAuthorCode = mReaderID.GetIssAuthCode();
                    // 证件类型标识
                    @SuppressWarnings("unused")
                    String deadIdCardSign = mReaderID.getCardSign();
                    // 照片
                    Bitmap deadIdPicture = mReaderID.GetPicture();
                    String stringBuilder = "英文姓名：" + readIdEnName + "\r\n" +
                            "中文姓名：" + readIdName + "\r\n" +
                            "性别：" + readIdSex + "\r\n" +
                            "国籍：\"：" + readIdCountry + "\r\n" +
                            "出生日期：" + DataProcesse.DateConvert(readIdBirth) + "\r\n" +
                            "永久居留证号：" + readIdCode + "\r\n" +
                            "有效期起始：" + DataProcesse.DateConvert(readIdBeginDate) + "\r\n" +
                            "有效期截止：" + DataProcesse.DateConvert(readIdEndDate) + "\r\n" +
                            "授权机关：" + readIdAuthorCode + "\r\n";
                    mIdInfo.setText(stringBuilder);
                    ImageSpan imgSpan = new ImageSpan(MainActivity.this, deadIdPicture);
                    final SpannableString spanString = new SpannableString("icon");
                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mIdInfo.append(spanString);
                }


            }
        });

        findViewById(R.id.continueRead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button mBtReadCardIDLoop = findViewById(R.id.continueRead);
                if (mBtReadCardIDLoop.getText().toString().equals("连续")) {
                    mBtReadCardIDLoop.setText("停止");
                    mReaderID = new HxJ10AReaderID();
                    mReaderID.setInputStream(mInputStream);
                    mReaderID.setOutputStream(mOutputStream);

                    // 开始连续读卡
                    mReadLoop = new ThreadReadIDLoop(mReaderID);
                    mReadLoop.setM_ThreadSendhandler(mReceiveMsgFromThread);
                    mReadLoop.Start();
                } else {
                    mBtReadCardIDLoop.setText("连续");
                    if (mReadLoop == null) {
                        return;
                    }
                    // 停止连续读卡
                    mReadLoop.Stop();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private MyHandler mReceiveMsgFromThread = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            try {
                super.handleMessage(msg);

                if (null == oReference.get()) {
                    return;
                }
                PlaySoundUtils.play(1,1);
                // 接收消息
                switch (msg.what) {
                    case 0:
                        mIdInfo.setText("");
                        String readIDError = (String) msg.obj;
                        mIdInfo.append(readIDError + "\r\n");
                    case 1:
                        mIdInfo.setText("");
                        String readInfoFromID = (String) msg.obj;
                        mIdInfo.append(readInfoFromID + "\r\n");
                        break;
                    case 2:
                        Bitmap readPictureFromID = (Bitmap) msg.obj;
                        ImageSpan imgSpan = new ImageSpan(MainActivity.this, readPictureFromID);
                        final SpannableString spanString = new SpannableString("icon");
                        spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mIdInfo.append(spanString);
                        break;
                    case 3:
                        mIdInfo.setText("");
                        String breakInfoFromID = (String) msg.obj;
                        mIdInfo.append(breakInfoFromID + "\r\n");
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static class MyHandler extends Handler {
        WeakReference<Activity> oReference;

        MyHandler(Activity activity) {
            oReference = new WeakReference<>(activity);
        }
    }

    ;

    @Override
    protected void onDestroy() {
        HxJ10ASerialPort.closeSerialPort();
        mGpio.powerOffDevice(93);
        mSerialPort.close();
        super.onDestroy();
    }
}
