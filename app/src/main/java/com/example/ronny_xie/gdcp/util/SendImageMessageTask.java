package com.example.ronny_xie.gdcp.util;

import java.io.File;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.ronny_xie.gdcp.Fragment.chat.page.ChatPage;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeChatTarget;
import com.gotye.api.GotyeCustomerService;
import com.gotye.api.GotyeMessage;


public class SendImageMessageTask extends AsyncTask<String, String, String> {

	public static final int IMAGE_MAX_SIZE_LIMIT = 100;
	private GotyeMessage createMessage;
	private GotyeChatTarget target;
	private ChatPage chatPage;

	private String bigImagePath;

	public SendImageMessageTask(ChatPage chatPage, GotyeChatTarget target) {
		this.target = target;
		this.chatPage = chatPage;
	}

	@Override
	protected String doInBackground(String... arg0) {
		File f = new File(arg0[0]);
		if (!f.exists()) {
			return null;
		}
		if (f.length() < 1000) {
			if (BitmapUtil.checkCanSend(f.getAbsolutePath())) {
				return f.getAbsolutePath();
			} else {
				return BitmapUtil.saveBitmapFile(BitmapUtil.getSmallBitmap(
						f.getAbsolutePath(), 500, 500));
			}
		} else {
			Bitmap bmp = BitmapUtil.getSmallBitmap(f.getAbsolutePath(), 500,
					500);
			if (bmp != null) {
				return BitmapUtil.saveBitmapFile(bmp);
			}
		}

		return null;
	}

	private void sendImageMessage(String imagePath) {
		createMessage = GotyeMessage.createImageMessage(GotyeAPI.getInstance()
				.getLoginUser(), target, imagePath);
		createMessage.getMedia().setPathEx(imagePath);
		//int code = 
		if(target instanceof GotyeCustomerService){
			String extraStr = "http://kefu-c.gotye.com.cn/product";
			createMessage.putExtraData(extraStr.getBytes());
		}
		GotyeAPI.getInstance().sendMessage(createMessage);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (result == null) {
			ToastUtil.show(chatPage, "请发送jpg图片");
			return;
		}
		sendImageMessage(result);
		if (createMessage == null) {
			ToastUtil.show(chatPage, "图片消息发送失败");
		} else {
			createMessage.getMedia().setPathEx(bigImagePath);
			chatPage.callBackSendImageMessage(createMessage);
		}
		super.onPostExecute(result);
	}
}
