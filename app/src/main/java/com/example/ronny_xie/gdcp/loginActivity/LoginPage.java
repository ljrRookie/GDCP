package com.example.ronny_xie.gdcp.loginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ronny_xie.gdcp.R;
import com.example.ronny_xie.gdcp.mainActivity.MainActivity;
import com.example.ronny_xie.gdcp.util.ProgressDialogUtil;
import com.example.ronny_xie.gdcp.util.SharePreferenceUtil;
import com.example.ronny_xie.gdcp.util.ToastUtil;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeDelegate;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

public class LoginPage extends FragmentActivity implements OnGestureListener {
	private GestureDetector mGesture = null;
	com.gc.materialdesign.views.ButtonRectangle mButLogin;
	EditText mEdtName, mEdtPsd;
	String mUsername;
	String mPassword;
	public static HttpClient httpClient = null;
	private List<String> values;
	private Drawable drawable;
	private ImageView image;
	private Handler handler;
	private EditText code;
	public static final int NAMEEXIST = 1001;
	public static final int SCRIPTTAG = 3;
	public static final int SETIMAGECODE = 1;
	private String username;
	private CheckBox mCheckbox;
	private TextView mTerms;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//初始化
		int code = GotyeAPI.getInstance().init(this, MyApplication.APPKEY);
		//判断当前登陆状态
		int state = GotyeAPI.getInstance().isOnline();
		GotyeUser us = GotyeAPI.getInstance().getLoginUser();
		//没有登陆需要显示登陆界面
		setContentView(R.layout.activity_login);
		//注意添加LoginListener
		GotyeAPI.getInstance().addListener(mDelegate);
		mGesture = new GestureDetector(this, this);

		httpClient = new DefaultHttpClient();
		ProgressDialogUtil.showProgress(LoginPage.this, "正在连接..请稍后");
		initView();
		initBtnListener();
		initHander();

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				values = ConnInterface.Conn(httpClient);
				drawable = ConnInterface.GetImageCode(httpClient);
				handler.sendEmptyMessage(SETIMAGECODE);
			}
		});
		thread.start();
//        initBar();
		initAnimation();
		initVideoView();
	}

	private void initVideoView() {
		final com.example.ronny_xie.gdcp.view.LoginPageVideoView videoView = (com.example.ronny_xie.gdcp.view.LoginPageVideoView) findViewById(R.id.loginpage_videoview);
		final String uri = "android.resource://" + getPackageName() + "/" + R.raw.guide_2;
		videoView.setVideoURI(Uri.parse(uri));
		videoView.start();
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				mp.setLooping(true);

			}
		});
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				videoView.setVideoPath(uri);
				videoView.start();

			}
		});

	}


	//设计动画事件
	private void initAnimation() {
		{
			LinearLayout tv_login_logo = (LinearLayout) findViewById(R.id.login_logo);
			AlphaAnimation alphaAnimation_login_tv = new AlphaAnimation(0.1f, 1);
			alphaAnimation_login_tv.setDuration(2000);
			tv_login_logo.setAnimation(alphaAnimation_login_tv);
		}
	}

	//设计响应事件
	private void initHander() {
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == SETIMAGECODE) {
					image.setBackground(drawable);
					ProgressDialogUtil.dismiss();
				} else if (msg.what == 2) {
					if (checkUser()) {
						// 登录的时候要传入登录监听，当重复登录时会直接返回登录状态
						saveUser(LoginPage.this, mUsername, mEdtPsd.getText().toString().trim(), false);
						Intent login = new Intent(LoginPage.this, GotyeService.class);
						login.setAction(GotyeService.ACTION_LOGIN);
						login.putExtra("name", mUsername);
						if (!TextUtils.isEmpty("")) {
							login.putExtra("pwd", "");
						}
						startService(login);
						ProgressDialogUtil.showProgress(LoginPage.this, "正在登录...");
						ToastUtil.show(LoginPage.this, username + "，欢迎您登录");
					}
				} else if (msg.what == SCRIPTTAG) {
					String a = (String) msg.obj;
					if (a == null) {
						ToastUtil.show(LoginPage.this, "登录失败LoginPage");
					} else {
						ToastUtil.show(LoginPage.this, (String) msg.obj);
					}
					httpClient = null;
					httpClient = new DefaultHttpClient();
					Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {
							values = ConnInterface.Conn(httpClient);
							drawable = ConnInterface.GetImageCode(httpClient);
							handler.sendEmptyMessage(SETIMAGECODE);
						}
					});
					thread.start();
				} else if (msg.what == NAMEEXIST) {
					username = msg.obj.toString();
					String userName = msg.obj.toString().replace("同学", "");
					SharedPreferences userSharePreference = SharePreferenceUtil.newSharePreference(LoginPage.this, "username");
					SharePreferenceUtil.saveString("userName", userName, userSharePreference);
				}
			}
		};
	}

	//设计登陆按钮和验证码按钮的点击事件
	private void initBtnListener() {
		mButLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mEdtName.getText().toString().trim().equals("")) {
					ToastUtil.show(LoginPage.this, "请输入用户ID");
					return;
				} else if (mEdtPsd.getText().toString().trim().equals("")) {
					ToastUtil.show(LoginPage.this, "请输入密码");
					return;
				} else if (code.getText().toString().trim().equals("")) {
					ToastUtil.show(LoginPage.this, "请输入验证码");
					return;
				} else if(!mCheckbox.isChecked()){
					ToastUtil.show(LoginPage.this, "请先勾选我已阅读并同意服务条款");
					return;
				}
				ProgressDialogUtil.showProgress(LoginPage.this, "正在登录...请稍后");
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						String mUser = mEdtName.getText().toString().trim();
						String mCode = code.getText().toString().trim();
						String mpass = mEdtPsd.getText().toString().trim();
						String[] arr = {mUser, mpass, mCode};
						int b = ConnInterface.ClickIn(httpClient, arr, values,
								handler);
						if (b == 1) {
							//登陆成功
							handler.sendEmptyMessage(2);
							ProgressDialogUtil.dismiss();
						}
						if (b == -1) {
							//登陆失败
							handler.sendEmptyMessage(SCRIPTTAG);
							ProgressDialogUtil.dismiss();
						}
					}
				});
				thread.start();
			}
		});

		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				httpClient = null;
				httpClient = new DefaultHttpClient();
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						values = ConnInterface.Conn(httpClient);
						drawable = ConnInterface.GetImageCode(httpClient);
						handler.sendEmptyMessage(SETIMAGECODE);
					}
				});
				thread.start();
			}
		});

		mTerms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSeriviceDialog();
			}
		});
	}

	private void showSeriviceDialog() {
		ScrollView sc = new ScrollView(this);
		sc.setBackground(getResources().getDrawable(R.drawable.bg_round_white));
		//sc.setBackgroundColor(getResources().getColor(R.color.scbackground));
		TextView tv = new TextView(this);
		tv.setTextSize(16);
		tv.setTextColor(Color.BLACK);
		tv.setText(R.string.service_content);

		sc.addView(tv);
		// 内容
		new AlertDialog.Builder(this,R.style.dialog)
				.setTitle(R.string.serivice_dialog)
				.setCancelable(true)
				.setView(sc)
				.create().show();

	}

	private void initView() {
		mTerms = (TextView) findViewById(R.id.terms);
		mCheckbox = (CheckBox) findViewById(R.id.checkbox);
		code = (EditText) findViewById(R.id.code);
		image = (ImageView) findViewById(R.id.image);
		mButLogin = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.start);
		mEdtName = (EditText) findViewById(R.id.username);
		mEdtPsd = (EditText) findViewById(R.id.userpsd);
		String user[] = getUser(LoginPage.this);
		String hasUserName = user[0];
		String hasPassWord = user[1];
		mUsername = hasUserName;
		mPassword = hasPassWord;
		if (mUsername != null) {
			mEdtName.setText(hasUserName);
			mEdtName.setSelection(mEdtName.getText().length());
		}
	}

	private boolean checkUser() {
		mUsername = mEdtName.getText().toString();
		boolean isValid = true;
		if (mUsername == null || mUsername.length() == 0) {
			Toast.makeText(LoginPage.this, "请输入用户名", Toast.LENGTH_SHORT)
					.show();
			isValid = false;
		}
		return isValid;
	}

	public static final String CONFIG = "login_config";

	public static void saveUser(Context context, String name, String password, boolean haslogin) {
		if (TextUtils.isEmpty(name)) {
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(CONFIG,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("username", name);
		edit.putString("password", password);
		edit.putBoolean("haslogin", haslogin);
		edit.commit();
	}

	public static String[] getUser(Context context) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG,
				Context.MODE_PRIVATE);
		String name = sp.getString("username", null);
		String password = sp.getString("password", null);
		String[] user = new String[2];
		user[0] = name;
		user[1] = password;
		return user;
	}

	@Override
	public void onBackPressed() {
		GotyeAPI.getInstance().removeListener(mDelegate);
		super.onBackPressed();
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGesture.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		// 移除监听
		GotyeAPI.getInstance().removeListener(mDelegate);
		super.onDestroy();
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	                       float velocityY) {
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	// 单击
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
	                        float distanceY) {
		return false;
	}

	@Override
	// 长按
	public void onLongPress(MotionEvent e) {

	}

	//Gotye登陆函数
	private GotyeDelegate mDelegate = new GotyeDelegate() {

		public void onLogin(int code, GotyeUser user) {
			ProgressDialogUtil.dismiss();
			// 判断登陆是否成功
			if (code == GotyeStatusCode.CodeOK //0
					|| code == GotyeStatusCode.CodeReloginOK //5
					|| code == GotyeStatusCode.CodeOfflineLoginOK) {  //6

				// 传入已登过的状态
				String user1[] = LoginPage.getUser(LoginPage.this);
				String hasUserName = user1[0];
				String hasPassWord = user1[1];
				LoginPage.saveUser(LoginPage.this, hasUserName, hasPassWord, true);

				Intent i = new Intent(LoginPage.this, MainActivity.class);
				startActivity(i);

				if (code == GotyeStatusCode.CodeOfflineLoginOK) {
					Toast.makeText(LoginPage.this, "您当前处于离线状态", Toast.LENGTH_SHORT).show();
				} else if (code == GotyeStatusCode.CodeOK) {
					Toast.makeText(LoginPage.this, "登录成功", Toast.LENGTH_SHORT).show();
				}
				LoginPage.this.finish();
			} else {
				// 失败,可根据code定位失败原因
				Toast.makeText(LoginPage.this, "登录失败 code=" + code, Toast.LENGTH_SHORT)
						.show();
			}
		}

		public void onLogout(int code) {
		}

		public void onReconnecting(int code, GotyeUser currentLoginUser) {
		}
	};

	private void initBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}
}
