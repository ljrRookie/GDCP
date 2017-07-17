package com.example.ronny_xie.gdcp.Fragment;

import java.util.List;

import com.example.ronny_xie.gdcp.Fragment.jw2012SyetemFragment.page.jw_main_page;
import com.example.ronny_xie.gdcp.loginActivity.ConnInterface;

import org.apache.http.impl.client.DefaultHttpClient;

import com.example.ronny_xie.gdcp.R;
import com.example.ronny_xie.gdcp.loginActivity.LoginPage;
import com.example.ronny_xie.gdcp.util.ProgressDialogUtil;
import com.example.ronny_xie.gdcp.util.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.ronny_xie.gdcp.loginActivity.LoginPage.httpClient;

public class jwFragment extends Activity {
    private EditText text;
    private ImageView image;
    private Drawable drawable;
    private com.gc.materialdesign.views.ButtonRectangle button;
    public static Handler handler;
    private String[] users;
    public static List<String> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_jw_login);
        initHandler();
        init();
        initBar();
        users = LoginPage.getUser(this);
        ProgressDialogUtil.showProgress(this, "正在连接..请稍后");
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                values = ConnInterface.Conn(ConnInterface.getHttpclient());
                drawable = ConnInterface.GetImageCode(ConnInterface.getHttpclient());
                handler.sendEmptyMessage(1);
            }
        });
        thread.start();
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    image.setBackground(drawable);
                    ProgressDialogUtil.dismiss();
                } else if (msg.what == 2) {
                    text.setText("");
                    Intent intent = new Intent(jwFragment.this, jw_main_page.class);
                    startActivity(intent);
                    finish();
                } else if (msg.what == 3) {
                    text.setText("");
                    String a = (String) msg.obj;
                    if (a == null) {
                        ToastUtil.show(jwFragment.this, "登录失败LoginPage");
                    } else {
                        ToastUtil.show(jwFragment.this, (String) msg.obj);
                    }
                    httpClient = null;
                    httpClient = new DefaultHttpClient();
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            values = ConnInterface.Conn(ConnInterface.getHttpclient());
                            drawable = ConnInterface
                                    .GetImageCode(httpClient);
                            handler.sendEmptyMessage(1);
                        }
                    });
                    thread.start();
                }
                super.handleMessage(msg);
            }
        };
    }

    private void init() {
        TextView tv_changeImage = (TextView)findViewById(
                R.id.fragment_tv_changeimage);
        button = (com.gc.materialdesign.views.ButtonRectangle)findViewById(R.id.fragment03_button);
        image = (ImageView) findViewById(R.id.fragment03_image);
        text = (EditText) findViewById(R.id.fragment03_edittext);
        tv_changeImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgressDialogUtil.showProgress(jwFragment.this, "获取中");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        drawable = ConnInterface.GetImageCodeAgain(ConnInterface.getHttpclient());
                        handler.sendEmptyMessage(1);
                    }
                });
                thread.start();
            }
        });
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgressDialogUtil.showProgress(jwFragment.this, "正在登录...请稍后");
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String mUser = users[0];
                        String mCode = text.getText().toString().trim();
                        String mpass = users[1];
                        String[] arr = {mUser, mpass, mCode};
                        int b = ConnInterface.ClickIn(ConnInterface.getHttpclient(), arr, values,
                                handler);
                        if (b == 1) {
                            handler.sendEmptyMessage(2);
                            ProgressDialogUtil.dismiss();
                        }
                        if (b == -1) {
                            handler.sendEmptyMessage(3);
                            ProgressDialogUtil.dismiss();
                        }
                    }
                });
                thread.start();
            }
        });
    }
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
