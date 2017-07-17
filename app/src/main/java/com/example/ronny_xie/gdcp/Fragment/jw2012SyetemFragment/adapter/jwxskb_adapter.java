package com.example.ronny_xie.gdcp.Fragment.jw2012SyetemFragment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ronny_xie.gdcp.Fragment.jw2012SyetemFragment.bean.jwxskb_javabean;
import com.example.ronny_xie.gdcp.R;

import java.util.ArrayList;

public class jwxskb_adapter extends BaseAdapter {
	private ArrayList<jwxskb_javabean> beanData;
	private Context context;

	public jwxskb_adapter(ArrayList<jwxskb_javabean> beanData, Context context) {
		this.beanData = beanData;
		this.context = context;
	}

	@Override
	public int getCount() {
		return beanData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder ;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.adapter_jwxskb, null);
			holder.one = (TextView) convertView.findViewById(R.id.one);
			holder.two = (TextView) convertView.findViewById(R.id.two);
			holder.three = (TextView) convertView.findViewById(R.id.three);
			holder.four = (TextView) convertView.findViewById(R.id.four);
			holder.five = (TextView) convertView.findViewById(R.id.five);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.one.setText(beanData.get(position).getOne());
		holder.two.setText(beanData.get(position).getTwo());
		holder.three.setText(beanData.get(position).getThree());
		holder.four.setText(beanData.get(position).getFour());
		holder.five.setText(beanData.get(position).getFive());
		return convertView;
	}

	public static class ViewHolder {
		TextView one;
		TextView two;
		TextView three;
		TextView four;
		TextView five;
	}
}
