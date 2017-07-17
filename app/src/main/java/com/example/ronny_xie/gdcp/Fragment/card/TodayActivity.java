package com.example.ronny_xie.gdcp.Fragment.card;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ronny_xie.gdcp.Fragment.card.javabean.todayData_javabean;
import com.example.ronny_xie.gdcp.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Ronny on 2017/5/8.
 */

public class TodayActivity extends Activity{
    private int NOTIFYDATAGETSUCCESS = 1001;
    private Handler handler;
    private Gson gson;
    private ArrayList<Object> arr;
    private LineChartView lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_card_today_main);
        super.onCreate(savedInstanceState);
        initChart();
        initBar();//设置无标题
        initData();//获取信息并展现main
        initHanler();
    }

    private void initHanler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==NOTIFYDATAGETSUCCESS){
                    Object fromJson = gson.fromJson((String)msg.obj, todayData_javabean.class);
                    todayData_javabean todayDataJavabean= (todayData_javabean)fromJson;
                    String moneyData = todayDataJavabean.getTotalMoney();
                    solveData();
                    initListView();
                    initView(moneyData);
                }
                return true;
            }
        });
    }

    private void initListView() {
        ListView listView = (ListView) findViewById(R.id.card_activity_today_listview);
        listView.setAdapter(new TodayListViewAdapter(TodayActivity.this, arr));
    }

    private void solveData() {
        Float[] dataMoney= {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
        todayData_javabean todayDataJavabean = null;
        for(int i = 0;i<arr.size();i++){
            todayDataJavabean=  (todayData_javabean)arr.get(i);
            String date = todayDataJavabean.getDate();
            int hour = Integer.parseInt(date.split(" ")[1].split(":")[0]);
            if(hour>=6&&hour<9){
                dataMoney[0] += Float.valueOf(todayDataJavabean.getMoney().replace("-",""));
            }else if(hour>=9&&hour<12){
                dataMoney[1] += Float.valueOf(todayDataJavabean.getMoney().replace("-",""));
            }else if(hour>=12&&hour<15){
                dataMoney[2] += Float.valueOf(todayDataJavabean.getMoney().replace("-",""));
            }else if(hour>=15&&hour<18){
                dataMoney[3] += Float.valueOf(todayDataJavabean.getMoney().replace("-",""));
            }else if(hour>=18&&hour<21){
                dataMoney[4] += Float.valueOf(todayDataJavabean.getMoney().replace("-",""));
            }else if(hour>=21&&hour<24){
                dataMoney[5] += Float.valueOf(todayDataJavabean.getMoney().replace("-",""));
            }
        }
        score = dataMoney;
        mPointValues.clear();
        for (int i = 0; i < dataMoney.length; i++) {
            mPointValues.add(new PointValue(i, dataMoney[i]));
        }
        lineChart.startDataAnimation(10);
    }

    private void initView(String moneyData) {
        TextView tv_time = (TextView) findViewById(R.id.card_activity_today_time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tv_time.setText(format.format(System.currentTimeMillis()));
        com.chaychan.viewlib.NumberRunningTextView tv_money = (com.chaychan.viewlib.NumberRunningTextView) findViewById(R.id.card_activity_today_tv);
        tv_money.setContent(moneyData);
    }

    private void initData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String todayData = cardClient.getTodayData(cardClient.getHttpClient());
                try {
                    gson = new Gson();
                    arr = new ArrayList<Object>();
                    JSONArray array = new JSONArray(todayData);
                    for (int i = 0; i < array.length()-1; i++) {
                        Object fromJson = gson.fromJson(array.get(i).toString(), todayData_javabean.class);
                        arr.add(fromJson);
                    }
                    Message msg = Message.obtain();
                    msg.what = NOTIFYDATAGETSUCCESS;
                    msg.obj = array.get(array.length()-1).toString();
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
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

    String[] date = {"6-9","9-12","12-15","15-18","18-21","21-24"};//X轴的标注
    Float[] score= {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private void initChart() {
        initAxisXlables();//获取X坐标数据
        initAxisPoints();//获取坐标点
        initLineChart();
    }

    private void initLineChart() {
        lineChart = (LineChartView) findViewById(R.id.fragment_card_linechart);
        Line line = new Line(mPointValues).setColor(Color.parseColor("#4682B4"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(true);//曲线是否平滑
	    line.setStrokeWidth(2);//线条的粗细，默认是3
        line.setFilled(true);//是否填充曲线的面积
//        line.setHasLabels(true);//曲线的数据坐标是否加上备注
		line.setHasLabelsOnlyForSelected(false);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#cdcdcd"));//灰色

//	    axisX.setName("未来几天的天气");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
//        data.setAxisXBottom(axisX); //x 轴在底部
	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(false); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
//        data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 3);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 尼玛搞的老子好辛苦！！！见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据），当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。当数据点个数大于（29）的时候，
         * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
         * 若设置axisX.setMaxLabelChars(int count)这句话,
         * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，则
         刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
         若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
         * 并且Y轴是根据数据的大小自动设置Y轴上限
         * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
//        lineChart.startDataAnimation();
    }


    private void initAxisPoints() {
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }

    private void initAxisXlables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }
}
