package com.example.ronny_xie.gdcp.Fragment.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ronny_xie.gdcp.Fragment.card.javabean.todayData_javabean;
import com.example.ronny_xie.gdcp.R;

import java.util.ArrayList;

/**
 * Created by Ronny on 2017/5/9.
 */

public class TodayListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Object> arr = new ArrayList<Object>();


    TodayListViewAdapter(Context context, ArrayList arr) {
        this.context = context;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
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
        View view = View.inflate(context, R.layout.adapter_card_today_listview, null);
        todayData_javabean todayDataJavabean = (todayData_javabean) arr.get(position);
        TextView tv_date = (TextView) view.findViewById(R.id.fragment_card_listview_tv_date);
        TextView tv_type = (TextView) view.findViewById(R.id.fragment_card_listview_tv_type);
        TextView tv_name = (TextView) view.findViewById(R.id.fragment_card_listview_tv_name);
        TextView tv_money = (TextView) view.findViewById(R.id.fragment_card_listview_tv_money);
        tv_date.setText(todayDataJavabean.getDate().split(" ")[1]);
        tv_type.setText(todayDataJavabean.getType());
        tv_name.setText(todayDataJavabean.getName());
        tv_money.setText(todayDataJavabean.getMoney());
        return view;
    }
}
