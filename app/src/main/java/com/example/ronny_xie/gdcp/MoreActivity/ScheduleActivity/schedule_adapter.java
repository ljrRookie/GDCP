package com.example.ronny_xie.gdcp.MoreActivity.ScheduleActivity;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ronny_xie.gdcp.MoreActivity.ScheduleActivity.db.DBService;
import com.example.ronny_xie.gdcp.R;

import java.util.ArrayList;


/**
 * Created by ronny_xie on 2017/2/5.
 */

public class schedule_adapter extends BaseAdapter {
    Context context;
    ArrayList<String> data_title = null;
    ArrayList<String> data_time = null;

    schedule_adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        Cursor cursor = new DBService(context).query();
        if (data_title == null) {
            data_title = new ArrayList<String>();
            data_time = new ArrayList<String>();
        } else {
            data_title.clear();
            data_time.clear();
        }
        while (cursor.moveToNext()) {
            data_title.add(cursor.getString(1));
            data_time.add(cursor.getString(2));
        }
        return data_title.size();
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
        View view = View.inflate(context, R.layout.adapter_schedule, null);
        TextView tv_title = (TextView) view.findViewById(R.id.schedule_adapter_title);
        TextView tv_time = (TextView) view.findViewById(R.id.schedule_adapter_time);
        tv_title.setText(data_title.get(position).toString());
        tv_time.setText(data_time.get(position).toString());
        return view;
    }
}
