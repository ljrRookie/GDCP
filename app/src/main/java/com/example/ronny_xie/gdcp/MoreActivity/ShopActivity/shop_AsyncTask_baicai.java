package com.example.ronny_xie.gdcp.MoreActivity.ShopActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public class shop_AsyncTask_baicai extends AsyncTask<Object, Object, Object> {
	private String baicai;
	private Context context;
	public shop_AsyncTask_baicai(String baicai, Context context) {
		this.baicai = baicai;
		this.context = context;
	}

	@Override
	protected Object doInBackground(Object... params) {
		shopConnList list = new shopConnList();
		list.show(baicai);
		return null;
	}


	@Override
	protected void onPostExecute(Object result) {
		Message msg = Message.obtain();
		msg.obj = shopConnList.shopList;
		msg.what = 1;
		shopActivity2.handler.sendMessage(msg);
		// lv.setAdapter(new shop_Adapter(shopList, context));
		super.onPostExecute(result);
	}
}
