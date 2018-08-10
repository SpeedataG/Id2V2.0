package com.spd.hx;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import java.lang.Thread.State;

public class ThreadReadIDLoop extends BaseThread
{
	private HxJ10AReaderID mReaderID = null;

	// 线程发送消息给主界面
	private Handler mThreadSendhandler;

	public void setM_ThreadSendhandler(Handler ThreadSendhandler)
	{
		this.mThreadSendhandler = ThreadSendhandler;
	}

	// 构造函数
	public ThreadReadIDLoop(HxJ10AReaderID ReaderID)
	{
		mReaderID = ReaderID;
	}

	@Override
	public void ThreadExecute()
	{
		while (true)
		{
			// TODO 自动生成的方法存根
			if (mStatus == State.TERMINATED)
			{
				break;
			}

			String ErrMsg = null;
			String ResultMsg = null;
			Bitmap ResultPicture = null;

			do
			{
				int iRet = mReaderID.ReadCard();
				if (iRet == 1)
				{
					ErrMsg = "寻卡失败\r\n";
					break;
				}
				if (iRet == 2)
				{
					ErrMsg = "选卡失败\r\n";
					break;
				}
				if (iRet == 3)
				{
					ErrMsg = "读取卡内信息失败\r\n";
					break;
				}

				// 居民身份证
				if (mReaderID.GetIDCardType() == false)
				{
					// 姓名
					String ReadIDName = mReaderID.GetName();
					// 性别
					String ReadIDSex = mReaderID.GetSex();
					// 民族
					String ReadIDNation = mReaderID.GetNation();
					// 出生日期
					String ReadIDBirth = mReaderID.GetBirth();
					// 住址
					String ReadIDAddress = mReaderID.GetAddr();
					// 身份证号
					String ReadIDCode = mReaderID.GetIDCode();
					// 签发机关
					String ReadIDIssue = mReaderID.GetIssue();
					// 有效日期起始
					String ReadIDBeginDate = mReaderID.GetBeginDate();
					// 有效日期截止
					String ReadIDEndDate = mReaderID.GetEndDate();
					// 照片
					Bitmap ReadIDPicture = mReaderID.GetPicture();

					ResultMsg = "";
					ResultMsg += "姓名：" + ReadIDName + "\r\n";
					ResultMsg += "性别：" + ReadIDSex + "\r\n";
					ResultMsg += "民族：" + ReadIDNation + "\r\n";
					ResultMsg += "出生日期：" + DataProcesse.DateConvert(ReadIDBirth) + "\r\n";
					ResultMsg += "地址：" + ReadIDAddress + "\r\n";
					ResultMsg += "省份证号：" + ReadIDCode + "\r\n";
					ResultMsg += "签发机关：" + ReadIDIssue + "\r\n";
					ResultMsg += "有效期起始：" + DataProcesse.DateConvert(ReadIDBeginDate) + "\r\n";
					ResultMsg += "有效期截止：" + DataProcesse.DateConvert(ReadIDEndDate) + "\r\n";
					ResultPicture = ReadIDPicture;
				}
				// 外国人身份证
				else if (mReaderID.GetIDCardType() == true)
				{
					// 英文姓名
					String ReadIDEnName = mReaderID.GetEnName();
					// 性别
					String ReadIDSex = mReaderID.GetSex();
					// 永久居留证号码
					String ReadIDCode = mReaderID.GetIDCode();
					// 国籍
					String ReadIDCountry = mReaderID.GetCountry();
					// 中文姓名
					String ReadIDName = mReaderID.GetName();
					// 有效日期起始
					String ReadIDBeginDate = mReaderID.GetBeginDate();
					// 有效日期截止
					String ReadIDEndDate = mReaderID.GetEndDate();
					// 出生日期
					String ReadIDBirth = mReaderID.GetBirth();
					// 证件版本
					@SuppressWarnings("unused")
					String ReadIDCardVersion = mReaderID.GetCardVersion();
					// 授权机关代码
					String ReadIDAuthorCode = mReaderID.GetIssAuthCode();
					// 证件类型标识
					@SuppressWarnings("unused")
					String ReadIDCardSign = mReaderID.GetCardSign();
					// 照片
					Bitmap ReadIDPicture = mReaderID.GetPicture();

					ResultMsg = "";
					ResultMsg += "英文姓名：" + ReadIDEnName + "\r\n";
					ResultMsg += "中文姓名：" + ReadIDName + "\r\n";
					ResultMsg += "性别：" + ReadIDSex + "\r\n";
					ResultMsg += "国籍：" + ReadIDCountry + "\r\n";
					ResultMsg += "出生日期：" + DataProcesse.DateConvert(ReadIDBirth) + "\r\n";
					ResultMsg += "永久居留证号：" + ReadIDCode + "\r\n";
					ResultMsg += "有效期起始：" + DataProcesse.DateConvert(ReadIDBeginDate) + "\r\n";
					ResultMsg += "有效期截止：" + DataProcesse.DateConvert(ReadIDEndDate) + "\r\n";
					ResultMsg += "授权机关：" + ReadIDAuthorCode + "\r\n";
					ResultPicture = ReadIDPicture;
				}

			} while (false);

			// 需要数据传递，用下面方法

			// 读卡失败，返回失败提示
			if (ErrMsg != null)
			{
				Message NoteMsg = mThreadSendhandler.obtainMessage();
				NoteMsg.what = 0;
				NoteMsg.obj = ErrMsg;
				mThreadSendhandler.sendMessage(NoteMsg);
				continue;
			}

			if (ResultMsg == null || ResultPicture == null)
			{
				continue;
			}

			Message TxtMsg = mThreadSendhandler.obtainMessage();
			TxtMsg.what = 1;
			TxtMsg.obj = ResultMsg;
			mThreadSendhandler.sendMessage(TxtMsg);

			Message PictureMsg = mThreadSendhandler.obtainMessage();
			PictureMsg.what = 2;
			PictureMsg.obj = ResultPicture;
			mThreadSendhandler.sendMessage(PictureMsg);

			ThreadTool.Sleep(800);
		}

		Message BreakMsg = mThreadSendhandler.obtainMessage();
		BreakMsg.what = 3;
		BreakMsg.obj = "连续读卡已停止";
		mThreadSendhandler.sendMessage(BreakMsg);
	}
}
