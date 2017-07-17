package com.example.ronny_xie.gdcp.MoreActivity.ShopActivity;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ronny_xie.gdcp.R;

public class shop_Adapter extends BaseAdapter {
	private ArrayList<shopBean> shop_List;
	private Context context;
	private ArrayList<String> bitmap_List;

	public shop_Adapter(ArrayList<shopBean> shop_List, Context context,
						ArrayList<String> bitmap_List) {
		this.shop_List = shop_List;
		this.context = context;
		this.bitmap_List = bitmap_List;
	}

	@Override
	public int getCount() {
		System.out.println(shop_List.size());
		return shop_List.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.adapter_shop_details, null);
		}
		TextView tv_title = (TextView) convertView
				.findViewById(R.id.shop_list_title);
		ImageView rl = (ImageView) convertView
				.findViewById(R.id.shop_list_image);
		TextView tv_desc = (TextView) convertView
				.findViewById(R.id.shop_list_desc);
		TextView tv_time = (TextView) convertView
				.findViewById(R.id.shop_list_time);
		tv_title.setText(shop_List.get(position).getTitle());
		tv_desc.setText(shop_List.get(position).getDesc());
		tv_time.setText(shop_List.get(position).getTime());
		System.out.println(bitmap_List.get(position));
		Glide.with(context).load(bitmap_List.get(position)).into(rl);
		return convertView;
	}

}
