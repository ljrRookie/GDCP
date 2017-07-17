package com.example.ronny_xie.gdcp.MoreActivity.WeatherActivity.WeatherJavaBean;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class fragment02_AsyncTask extends AsyncTask<Object, Object, String[][]> {
    private String week;
    private static String ban;
    private Document doc;
    private ArrayList<String> data = new ArrayList<String>();
    private String title = "";
    private String name;

    private  String[][] contents = new String[8][5];
    private  int courseIndex;
    private  int weekIndex = -1;

    public fragment02_AsyncTask( String week, String ban) {
        this.week = week;
        this.ban = ban;
        initContents();
    }

    @Override
    protected String[][] doInBackground(Object... arg0) {
        initContents();
        name = ban;
        String a = "http://www2.gdcp.cn/lin/sjsx/2016-2/11.html";
        String b = "http://www2.gdcp.cn/lin/sjsx/2016-2/11%20%E9%A1%B5%202.html";
        String c = "http://www2.gdcp.cn/lin/sjsx/2016-2/11%20%E9%A1%B5%203.html";
        a = a.replace("11", week);
        b = b.replace("11", week);
        c = c.replace("11", week);
        try {
            doc = Jsoup.connect(a).get();
            title = doc.title();
            System.out.println(title);
            Elements no_1 = doc.getElementsByTag("table");
            for (Element link : no_1) {
                String linkText = link.text().trim();
                linkText = linkText.replace("徐燃柏", "");
                linkText = linkText.replace("陈长彬", "");
                data.add(linkText);
                // System.out.println(linkText);
            }
            // ------------------------
            doc = Jsoup.connect(b).get();
            title = doc.title();
            System.out.println(title);
            Elements no_2 = doc.getElementsByTag("table");
            for (Element link : no_2) {
                String linkText = link.text().trim();
                linkText = linkText.replace("徐燃柏", "");
                linkText = linkText.replace("陈长彬", "");
                data.add(linkText);
                // System.out.println(linkText);
            }
            // ------------------------
            doc = Jsoup.connect(c).get();
            title = doc.title();
            System.out.println(title);
            Elements no_3 = doc.getElementsByTag("table");
            for (Element link : no_3) {
                String linkText = link.text().trim();
                linkText = linkText.replace("徐燃柏", "");
                linkText = linkText.replace("陈长彬", "");
                data.add(linkText);
                // System.out.println(linkText);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (String tem : data) {
            if (tem.contains(name) || tem.contains("星期")
                    || tem.contains("1～2 （8：10～9：45）")
                    || tem.contains("3～4 （10：10～11：45）")
                    || tem.contains("5～6 （14：15～15：55）")
                    || tem.contains("7～8 （16：10～17：40）") || tem.contains("中午")) {
                System.out.println(tem);
                transferDatas(tem);
            }
        }
        return contents;
    }

    @Override
    protected void onPostExecute(String[][] result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
		/*for (int i = 0; i < contents[0].length; i++) {
            for (int j = 0; j < contents.length; j++) {
				if (contents[j][i].equals("")) {
					System.out.println("星期" + i + "第" + j + "节,空");
				} else {
					System.out.println("星期" + i + "第" + j + "节," + contents[j][i]);
				}
			}
			System.out.println();
		}*/

    }


    public  void initContents() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                contents[j][i] = "";
            }
        }
        data.clear();
    }

    public  void transferDatas(String tem) {
        if (tem.contains("星期")) {
            weekIndex++;
            //System.out.println();
        }
        if (!tem.contains(ban) && !tem.contains("星期")) {
            courseIndex = Integer.parseInt(tem.substring(0, 1));
        }
        if (tem.contains(ban)) {
            int i = tem.indexOf(ban) + ban.length();
            String substring = tem.substring(i, tem.length());
            // System.out.println(substring.trim());
            if (courseIndex < 8 && courseIndex >= 0 && weekIndex < 5 && weekIndex >= 0) {
                contents[courseIndex - 1][weekIndex] = substring.trim();
                contents[courseIndex][weekIndex] = substring.trim();
                Log.e("jjy", "transferDatas: "+substring.trim());
            }
        }
    }

}
