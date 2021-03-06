package com.example.ronny_xie.gdcp.Fragment.chat.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ronny_xie.gdcp.R;
import com.example.ronny_xie.gdcp.Fragment.chat.CreateGroupSelectUser;
import com.example.ronny_xie.gdcp.bean.GotyeUserProxy;
import com.example.ronny_xie.gdcp.util.BitmapUtil;
import com.example.ronny_xie.gdcp.util.ImageCache;

@SuppressLint("DefaultLocale")
public class SelectUserAdapter extends BaseAdapter {
	private ArrayList<GotyeUserProxy> mData;
	private CreateGroupSelectUser mContext;

	public Map<String, Boolean> selectedMap;

	public SelectUserAdapter(CreateGroupSelectUser mContext,
			ArrayList<GotyeUserProxy> mData) {
		this.mContext = mContext;
		this.mData = mData;
		selectedMap = new HashMap<String, Boolean>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_select_contacts_item, null);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) arg1.findViewById(R.id.icon);
			viewHolder.firstChar = (TextView) arg1
					.findViewById(R.id.first_char);
			viewHolder.name = (TextView) arg1.findViewById(R.id.name);
			viewHolder.isSelected = (CheckBox) arg1
					.findViewById(R.id.is_selected);
			arg1.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		final GotyeUserProxy contacts = (GotyeUserProxy) getItem(position);

		String name = contacts.gotyeUser.getName();
		viewHolder.name.setText(name);

		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			viewHolder.firstChar.setText(contacts.firstChar);
			viewHolder.firstChar.setVisibility(View.VISIBLE);
		} else {
			viewHolder.firstChar.setVisibility(View.GONE);
		}
		if (selectedMap.containsKey(name)) {
			viewHolder.isSelected.setChecked(selectedMap.get(name));
		} else {
			viewHolder.isSelected.setChecked(false);
		}
		viewHolder.isSelected
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg1) {
							selectedMap.put(contacts.gotyeUser.getName(), true);
						} else {
							selectedMap.remove(contacts.gotyeUser.getName());
						}
						mContext.getCount(selectedMap);
					}
				});
		setUserIcon(viewHolder.icon, contacts);
		return arg1;
	}

	private void setUserIcon(ImageView iconView, GotyeUserProxy member) {
		Bitmap bmp = ImageCache.getInstance().get(member.gotyeUser.getName());
		if (bmp != null) {
			iconView.setImageBitmap(bmp);
		} else {
			if (member.gotyeUser.getIcon() != null) {
				bmp = BitmapUtil
						.getBitmap(member.gotyeUser.getIcon().getPath());
				if (bmp != null) {
					iconView.setImageBitmap(bmp);
					ImageCache.getInstance().put(member.gotyeUser.getName(),
							bmp);
				}
			}

		}
	}

	public int getSectionForPosition(int position) {
		return mData.get(position).firstChar.charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mData.get(i).firstChar;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	static class ViewHolder {
		ImageView icon;
		TextView firstChar, name;
		CheckBox isSelected;
	}
}
