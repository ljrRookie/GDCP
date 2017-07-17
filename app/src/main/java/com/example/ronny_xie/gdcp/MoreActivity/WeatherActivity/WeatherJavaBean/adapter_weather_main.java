package com.example.ronny_xie.gdcp.MoreActivity.WeatherActivity.WeatherJavaBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ronny_xie.gdcp.R;
import com.example.ronny_xie.gdcp.util.ToastUtil;

import java.util.ArrayList;


/**
 * Created by ronny_xie on 2017/1/26.
 */

public class adapter_weather_main extends BaseAdapter {
    private Context context;
    private ArrayList<javabean_weather_main_pad> data;

    public adapter_weather_main(ArrayList<javabean_weather_main_pad> data, Context context) {
        this.data = data;
        this.context = context;
        if (data == null) {
            ToastUtil.show(context, "未知错误，请重试！");
            return;
        }
    }

    @Override
    public int getCount() {
        return data.size();
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_weather_main, null);
        }
        TextView tv_date = (TextView) convertView.findViewById(R.id.fragment_weather_main_date);
        ImageView tv_info = (ImageView) convertView.findViewById(R.id.fragment_weather_main_info);
        TextView tv_max = (TextView) convertView.findViewById(R.id.fragment_weather_main_max);
        TextView tv_min = (TextView) convertView.findViewById(R.id.fragment_weather_main_min);
        tv_date.setText(data.get(position).getDay());
        Glide.with(context).load("http://tqyb.com.cn" + data.get(position).getImageUrl()).into(tv_info);
        tv_max.setText(data.get(position).getMaxTem());
        tv_min.setText(data.get(position).getMinTem());
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
