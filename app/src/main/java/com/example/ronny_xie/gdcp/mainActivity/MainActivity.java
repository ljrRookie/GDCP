package com.example.ronny_xie.gdcp.mainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ronny_xie.gdcp.Fragment.cardFragment;
import com.example.ronny_xie.gdcp.Fragment.libFragment;
import com.example.ronny_xie.gdcp.Fragment.settingFragment;
import com.example.ronny_xie.gdcp.R;
import com.example.ronny_xie.gdcp.MoreActivity.MoreApplication;
import com.example.ronny_xie.gdcp.loginActivity.MyApplication;
import com.example.ronny_xie.gdcp.loginActivity.LoginPage;
import com.example.ronny_xie.gdcp.util.SharePreferenceUtil;
import com.example.ronny_xie.gdcp.util.menu_backgroundUtils;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeChatTargetType;
import com.gotye.api.GotyeDelegate;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeMessageStatus;
import com.gotye.api.GotyeNotify;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;
import com.example.ronny_xie.gdcp.util.BeepManager;
import com.example.ronny_xie.gdcp.util.BitmapUtil;
import com.example.ronny_xie.gdcp.util.ImageCache;
import com.example.ronny_xie.gdcp.util.ToastUtil;
import com.example.ronny_xie.gdcp.Fragment.contactsFragment;
import com.example.ronny_xie.gdcp.Fragment.messageFragment;
import com.example.ronny_xie.gdcp.MoreActivity.WeatherActivity.fragment_weather;
import com.example.ronny_xie.gdcp.Fragment.tranroomFragment;
import com.example.ronny_xie.gdcp.Fragment.jwFragment;
import com.example.ronny_xie.gdcp.MoreActivity.ShopActivity.fragment_shop;


public class MainActivity extends FragmentActivity {
    private com.example.ronny_xie.gdcp.Fragment.messageFragment messageFragment;
    private com.example.ronny_xie.gdcp.Fragment.contactsFragment contactsFragment;
    private com.example.ronny_xie.gdcp.Fragment.settingFragment settingFragment;
    private tranroomFragment tranroomFragment;
    private libFragment libFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private int currentPosition = 0;
    private BeepManager beep;
    private GotyeAPI api;
    private GotyeUser user;
    private TextView msgTip;
    private static final String TAG = "MainActivity";
    private String userName;
    private FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //跳转activity的请求码
    private  final int CARD_CODE =110 ;
    private  final int JW_CODE = 111;
    private  final int MORE_CODE = 112;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        api = GotyeAPI.getInstance();
        setContentView(R.layout.layout_main);
        api.addListener(mDelegate);
        user = api.getLoginUser();
        api.getUserDetail(user, true);
        beep = new BeepManager(MainActivity.this);
        beep.updatePrefs();
//        initMenu();
        initUserInfo();
        initNav();//初始化侧拉
        Log.i(TAG, "initUserName: " + user.getName() + "+++++" + user.getNickname());
        initViews();//初始化界面
        fragmentManager = getSupportFragmentManager();// getFragmentManager();
        setTabSelection(0);
        clearNotify();
        menu_backgroundUtils.getMenuBackground(this,frameLayout);
    }

    private void initUserInfo() {
        SharedPreferences userSharePreference = SharePreferenceUtil.newSharePreference(getApplicationContext(), "username");
        userName = SharePreferenceUtil.getString("userName", userSharePreference);
        GotyeUser forModify = new GotyeUser(user.getName());
        forModify.setNickname(userName);
        forModify.setInfo("");
        forModify.setGender(user.getGender());
        String headPath = null;
        int code = api.reqModifyUserInfo(forModify, headPath);
        Log.i(TAG, "initUserInfo: " + userName);
        Log.i(TAG, "initUserInfo: " + user.getNickname());
        Log.i(TAG, "initUserInfo: " + user.getName());
        Log.d("initText", "" + code);
    }

    private void initNav() {
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        //获取头部布局
        View headerView = navigationView.getHeaderView(0);
        ImageView nav_header_image = (ImageView) headerView.findViewById(R.id.nav_header_image);
        TextView nav_header_name = (TextView) headerView.findViewById(R.id.nav_header_name);
        TextView nav_header_sign = (TextView) headerView.findViewById(R.id.nav_header_sign);
        frameLayout = (FrameLayout) headerView.findViewById(R.id.nav_header_framelayout);
        ImageView nav_header_leave = (ImageView) headerView.findViewById(R.id.nav_leave_image);
        //此处为侧拉框的
        frameLayout.setBackgroundResource(R.mipmap.head_img);
        //点击nav的header部分跳转到设置
        frameLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(3);
                drawerLayout.closeDrawer(navigationView);
            }
        });
        //点击nav上部分退出按钮
        nav_header_leave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = api.isOnline();
                int code = api.logout();
                int x = code;
                Log.d("", "code" + code + "" + x);
                if (code == GotyeStatusCode.CodeNotLoginYet) {
                    Intent intent1 = new Intent(getApplicationContext(),
                            LoginPage.class);
                    startActivity(intent1);
                    finish();
                }
            }
        });
        if (user != api.getLoginUser()) {
            user = api.getLoginUser();
            setUserInfo(user, nav_header_name, nav_header_image);
            SharedPreferences share = getSharedPreferences(user.getName().toString(),
                    Activity.MODE_PRIVATE);
            String sign = share.getString("sign",
                    "还没给我设置签名噢~");
            nav_header_sign.setText(sign);
        }

        navigationView.setItemIconTintList(null);//设置图标颜为默认
        navigationView.getMenu().getItem(0).setChecked(true);//设置默认选中为第一个
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setCheckable(true);
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_message:
                        nav_select(0);
                        break;
                    case R.id.nav_contacts:
                        nav_select(1);
                        break;
                    case R.id.nav_computerroom:
                        nav_select(3);
                        break;
                    case R.id.nav_jw2012:
                        Intent intentjw2012 = new Intent(getApplicationContext(), jwFragment.class);
//                        startActivity(intentjw2012);
                        startActivityForResult(intentjw2012,JW_CODE);
                        break;
                    case R.id.nav_card:
                        Intent intentCardLogin = new Intent(getApplicationContext(), cardFragment.class);
//                        startActivity(intentCardLogin);
                        startActivityForResult(intentCardLogin,CARD_CODE);
                        break;
                    case R.id.nav_lib:
                        nav_select(4);
                        break;
                    case R.id.nav_more:
                        Intent intentMoreActivity = new Intent(getApplicationContext(), MoreApplication.class);
//                        startActivity(intentMoreActivity);
                        startActivityForResult(intentMoreActivity,MORE_CODE);

                }
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });
    }

    public FrameLayout getFrameLayout() {
        if (frameLayout != null) {
            return frameLayout;
        }
        return null;
    }

    boolean hasRequest = false;

    private void setUserInfo(GotyeUser user, TextView name, ImageView image) {
        if (user.getIcon() == null && !hasRequest) {
            hasRequest = true;
            api.getUserDetail(user, true);
        } else {
            Bitmap bm = BitmapUtil.getBitmap(user.getIcon().getPath());
            if (bm != null) {
                image.setImageBitmap(bm);
                ImageCache.getInstance().put(user.getName(), bm);
            } else {
                api.downloadMedia(user.getIcon());
            }
        }
        if (userName != null) {
            name.setText(userName);
        } else {
            name.setText(user.getName());
            ToastUtil.show(this, "获取用户名称失败");
        }
//        id.setText(user.getName());
    }

    Fragment fragment_list[] = {messageFragment, contactsFragment, settingFragment, tranroomFragment, libFragment};
    Fragment lastFragment;

    public void nav_select(int i) {
        transaction = fragmentManager.beginTransaction();
        if (fragment_list[i] == null) {
            if (i == 0) {
                fragment_list[i] = new messageFragment();
            } else if (i == 1) {
                fragment_list[i] = new contactsFragment();
                contactsFragment = (contactsFragment) fragment_list[i];
            } else if (i == 2) {
                fragment_list[i] = new settingFragment();

            } else if (i == 3) {
                fragment_list[i] = new tranroomFragment();
                tranroomFragment = (tranroomFragment) fragment_list[i];
            } else if (i == 4) {
                fragment_list[i] = new libFragment();
                libFragment = (libFragment) fragment_list[i];
            }
            transaction.add(R.id.content, fragment_list[i]);
        } else {
            transaction.show(fragment_list[i]);
        }
        hideFragments(transaction, i);
        transaction.commit();
        lastFragment = fragment_list[i];
    }

    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction, int i) {
        if (lastFragment != null) {
            if (lastFragment != fragment_list[i]) {
                transaction.hide(lastFragment);
                if (settingFragment != null) {
                    transaction.hide(settingFragment);
                }
                Glide.get(getApplicationContext()).clearMemory();
            }
        }
    }
    //第一次点击返回键的时间
    private long clickTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //判断settingFragment是否可见，如果可见，点击手机回退按钮settingFragment就会隐藏
            if (settingFragment != null && settingFragment.isVisible()) {
                hideFragment();
                return false;
            } else if (contactsFragment != null && contactsFragment.isVisible()) {
                hideFragment();
                return false;
            } else if (libFragment != null && libFragment.isVisible()) {
                hideFragment();
                return false;
            } else if (tranroomFragment != null && tranroomFragment.isVisible()) {
                hideFragment();
                return false;
            } else {
                //判断drawerLayout是否打开，是则关闭
                if (drawerLayout != null && drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                    return true;
                } else {
                    long currnetTime = System.currentTimeMillis();
                    if (clickTime == 0) {
                        clickTime = currnetTime;
                        ToastUtil.show(this, "再按一次返回键退出程序");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    clickTime = 0;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        return true;
                    } else if (currnetTime - clickTime < 2 * 1000 && clickTime != 0) {
                        return super.onKeyDown(keyCode, event);
                    } else {
                        return super.onKeyDown(keyCode, event);
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    public void hideFragment() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
            return;
        }
        setTabSelection(0);
        if (drawerLayout != null) {
            drawerLayout.openDrawer(navigationView);
        }

    }

    private boolean returnNotify = false;

    @Override
    protected void onResume() {
        super.onResume();
        menu_backgroundUtils.getMenuBackground(this,frameLayout);
        returnNotify = false;

        mainRefresh();
    }

    @Override
    protected void onPause() {
        returnNotify = true;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        api.removeListener(mDelegate);
        super.onDestroy();
    }

    private void initViews() {
        msgTip = (TextView) findViewById(R.id.new_msg_tip);
    }


    @SuppressLint("NewApi")
    private void setTabSelection(int index) {
        updateUnReadTip();
        currentPosition = index;
        transaction = fragmentManager.beginTransaction();
        hideFragments(transaction, index);
        switch (index) {
            case 0:
                if (messageFragment == null) {
                    messageFragment = new messageFragment();
                    transaction.add(R.id.content, messageFragment);
                    lastFragment = messageFragment;
                } else {
                    transaction.show(messageFragment);
                }
                break;
            case 1:
                if (contactsFragment == null) {
                    contactsFragment = new contactsFragment();
                    transaction.add(R.id.content, contactsFragment);
                    lastFragment = messageFragment;
                } else {
                    transaction.show(contactsFragment);
                }
                break;
            case 2:
            default:
                if (settingFragment == null) {
                    settingFragment = new settingFragment();
                    transaction.add(R.id.content, settingFragment);
                    lastFragment = messageFragment;
                } else {
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }


    // 更新提醒
    public void updateUnReadTip() {
        //Todo
        int unreadCount = api.getTotalUnreadMessageCount();
        int unreadNotifyCount = api.getUnreadNotifyCount();
        unreadCount += unreadNotifyCount;
//        msgTip.setVisibility(View.VISIBLE);
        if (unreadCount > 0 && unreadCount < 100) {
//            msgTip.setText(String.valueOf(unreadCount));
        } else if (unreadCount >= 100) {
            msgTip.setText("99");
        } else {
//            msgTip.setVisibility(View.GONE);
        }

    }

    // 页面刷新
    private void mainRefresh() {
        updateUnReadTip();
        messageFragment.refresh();
        if (contactsFragment != null) {
            contactsFragment.refresh();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            int tab = intent.getIntExtra("tab", -1);
            if (tab == 1) {
                contactsFragment.refresh();
            }
            int notify = intent.getIntExtra("notify", 0);
            if (notify == 1) {
                clearNotify();
            }

            int selection_index = intent.getIntExtra("selection_index", -1);
            if (selection_index == 1) {
                setTabSelection(1);
            }
        }

    }

    // 清理推送通知
    private void clearNotify() {
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    //图片返回值操作
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: " + requestCode + "aaa" + resultCode);
        // 选取图片的返回值
        if (requestCode == 1) {
            Log.i(TAG, "setImageToHeadView: 222222222");
            if (data != null) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
//                    String path = URIUtil.uriToPath(this, selectedImage);
//                    setPicture(path);
                    cropRawPhoto(data.getData());//直接裁剪图片
                }
            }
        }
        if (requestCode == 10086) {
            if (data != null) {
                setImageToHeadView(data);
//                String path = URIUtil.uriToPath(getApplicationContext(),data.getData());
                setPicture(getApplication().getFilesDir() + "/Ask/okkk.jpg");
            }
        }
        //返回MainActivity时打开侧拉栏
        if (requestCode==CARD_CODE){
            if (drawerLayout!=null){
                drawerLayout.openDrawer(navigationView);
            }
        }
        if (requestCode==JW_CODE){
            if (drawerLayout!=null){
                drawerLayout.openDrawer(navigationView);
            }
        }
        if (requestCode==MORE_CODE){
            if (drawerLayout!=null){
                drawerLayout.openDrawer(navigationView);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //把裁剪的数据填入里面

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 10086);
    }

    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            File nf = new File(getApplication().getFilesDir() + "/Ask");
            nf.mkdir();
            File f = new File(getApplication().getFilesDir() + "/Ask", "okkk.jpg");
            Log.i(TAG, "setImageToHeadView: 11111111" + f.getAbsolutePath().toString());
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //设置图片
    private void setPicture(String path) {
        String smallImagePath = path;
        smallImagePath = BitmapUtil.check(smallImagePath);

        Bitmap smaillBit = BitmapUtil.getSmallBitmap(smallImagePath, 50, 50);
        String smallPath = BitmapUtil.saveBitmapFile(smaillBit);
        settingFragment.modifyUserIcon(smallPath);
    }

    //Gotye内部操作
    private GotyeDelegate mDelegate = new GotyeDelegate() {
        @Override
        public void onModifyUserInfo(int code, GotyeUser user) {
            Log.i(TAG, "onModifyUserInfo: " + code);
            if (code == 0) {
                Log.i(TAG, "initUserInfo2: " + userName);
                Log.i(TAG, "initUserInfo2: " + user.getNickname());
                Log.i(TAG, "initUserInfo2: " + user.getName());
            } else {

            }
        }

        // 此处处理账号在另外设备登陆造成的被动下线
        @Override
        public void onLogout(int code) {
            // FragmentTransaction t=fragmentManager.beginTransaction();
            // t.remove(messageFragment);
            // t.commit();
            ImageCache.getInstance().clear();

            if (code == GotyeStatusCode.CodeForceLogout) {
                Toast.makeText(MainActivity.this, "您的账号在另外一台设备上登录了！",
                        Toast.LENGTH_SHORT).show();
                MyApplication.clearHasLogin(MainActivity.this);
                Intent intent = new Intent(getBaseContext(), LoginPage.class);
                startActivity(intent);
                finish();
            } else if (code == GotyeStatusCode.CodeNetworkDisConnected) {
            } else {
                Toast.makeText(MainActivity.this, "退出登陆！", Toast.LENGTH_SHORT)
                        .show();
                MyApplication.clearHasLogin(MainActivity.this);
                Intent i = new Intent(MainActivity.this, LoginPage.class);
                startActivity(i);
                finish();
            }

        }

        // 收到消息（此处只是单纯的更新聊天历史界面，不涉及聊天消息处理，当然你也可以处理，若你非要那样做）
        @Override
        public void onReceiveMessage(GotyeMessage message) {
            if (returnNotify) {
                return;
            }
            messageFragment.refresh();
            if (message.getStatus() == GotyeMessageStatus.GotyeMessageStatusUnread) {
                updateUnReadTip();

                if (!MyApplication.isNewMsgNotify()) {
                    return;
                }
                if (message.getReceiver().getType() == GotyeChatTargetType.GotyeChatTargetTypeGroup) {
                    if (MyApplication.isNotReceiveGroupMsg()) {
                        return;
                    }
                    if (MyApplication.isGroupDontdisturb(((GotyeGroup) message
                            .getReceiver()).getGroupID())) {
                        return;
                    }
                }
                beep.playBeepSoundAndVibrate();
            }
        }

        // 自己发送的信息统一在此处理
        @Override
        public void onSendMessage(int code, GotyeMessage message) {
            if (returnNotify) {
                return;
            }
            messageFragment.refresh();
        }

        // 收到群邀请信息
        @Override
        public void onReceiveNotify(GotyeNotify notify) {
            if (returnNotify) {
                return;
            }
            messageFragment.refresh();
            updateUnReadTip();
            if (!MyApplication.isNotReceiveGroupMsg()) {
                beep.playBeepSoundAndVibrate();
            }
        }

        @Override
        public void onRemoveFriend(int code, GotyeUser user) {
            if (returnNotify) {
                return;
            }
            api.deleteSession(user, false);
            messageFragment.refresh();
            contactsFragment.refresh();
        }

        @Override
        public void onAddFriend(int code, GotyeUser user) {
            if (returnNotify) {
                return;
            }
            if (currentPosition == 1) {
                contactsFragment.refresh();
            }
        }

        @Override
        public void onGetMessageList(int code, List<GotyeMessage> list) {
            // if(list != null && list.size() > 0){
            mainRefresh();
            // }
        }
    };
}
