package com.example.ronny_xie.gdcp.util;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class menu_backgroundUtils {
	public static void setMenuBackground(Context context, String a) {
		@SuppressWarnings("static-access")
		SharedPreferences shared = context.getSharedPreferences(
				"menu_background", context.MODE_PRIVATE);
		SharedPreferences.Editor edit = shared.edit();
		edit.putString("background_uri",a);
		edit.commit();
	}

	public static void getMenuBackground(Context context, final FrameLayout frameLayout) {
		@SuppressWarnings("static-access")
		SharedPreferences shared = context.getSharedPreferences(
				"menu_background", context.MODE_PRIVATE);
		String requestBackgroundUri = shared.getString("background_uri", null);
		if (requestBackgroundUri != null) {
			System.out.println("requestBackGround！=null");
			Glide.with(context).load(requestBackgroundUri).asBitmap().into(new SimpleTarget<Bitmap>() {
				@Override
				public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
					Drawable drawable = new BitmapDrawable(bitmap);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
						frameLayout.setBackground(drawable);;
					}
				}
			});
		} else {
			System.out.println("null");
		}
	}
}
