package com.example.ronny_xie.gdcp.MoreActivity.EmotionActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.ronny_xie.gdcp.R;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by Eddie on 2017/7/14.
 */

public class fragment_emotion extends Activity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_emotion);
		mWebView = (WebView) findViewById(R.id.tbs_webview);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setDomStorageEnabled(true);
		settings.setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, String s) {
				webView.loadUrl(s);
				return true;
			}
		});
		mWebView.loadUrl("http://qiqu.uc.cn/?uc_param_str=frpfvedncpssntnwbipreime#!/index/index");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
