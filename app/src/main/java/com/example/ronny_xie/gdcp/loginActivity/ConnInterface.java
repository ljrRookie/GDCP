package com.example.ronny_xie.gdcp.loginActivity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

public class ConnInterface {
    private static final String TAG = "ConnInterface";
    private static HttpClient httpClient;
    private static HttpResponse response;
    private static String realName;

    public static HttpClient getHttpclient(){
        if(httpClient == null){
            httpClient = new DefaultHttpClient();
            return httpClient;
        }
        return httpClient;
    }
    /**
     * 方正教务系统服务连接
     *
     * @param httpClient 传入一个已经创建好的httpClient对象
     * @return 返回数组第一个值为VIEWSTATE，第二个值为Cookies
     */
    public static List<String> Conn(HttpClient httpClient) {

        List<String> values = null;
        try {
            // 根据浏览器个记录，是GET方法就使用HttpGet,是POST就是用HttpPost
            HttpGet getMainUrl = new HttpGet("http://jw2012.gdcp.cn/");
            HttpResponse response = httpClient.execute(getMainUrl);
            // 获取Cookie
            String cookie = response.getFirstHeader("Set-Cookie").getValue();
            Log.e(TAG, "Conn:cookie======== "+cookie);
           // Conn:cookie======== ASP.NET_SessionId=zv2kpsu2wyculn55enr1c355; path=/
            // 获取VIEWSTATE
           // Log.e(TAG, "Conn:response======== "+response);
            //Log.e(TAG, "Conn:response======== "+response.getEntity().getContent());
            //Conn:response======== org.apache.http.message.BasicHttpResponse@fdfea86
            String tempHtml = parseToString(response);
          //  Log.e(TAG, "Conn:tempHtml======== "+tempHtml);
         /*   Conn:tempHtml========
             <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
             <HTML>
             	<HEAD>
             		<title>��¼</title>
             		 <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7">
             		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7">
             		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
             		<meta http-equiv="Content-Language" content="gb2312">
             		<meta content="all" name="robots">
             		<meta content="������Ϣ" name="author">
             		<meta content="��Ȩ��Ϣ" name="Copyright">
             		<meta content="վ�����" name="description">
             		<meta content="վ��ؼ��" name="keywords">
             		<LINK media="all" href="style/base/jw.css" type="text/css" rel="stylesheet">
             			<LINK media="all" href="style/standard/jw.css" type="text/css" rel="stylesheet"> <!--[if IE 6]>
             <SCRIPT src="style/js/ie6comm.js"></SCRIPT>

             <SCRIPT>
                    DD_belatedPNG.fix('img');
                  </SCRIPT>
                  <![endif]-->
                  				<style>.fangshua { COLOR: red; FONT-SIZE: 14px; FONT-WEIGHT: bold }
                  	</style>
                  		<script language="javascript">
                    var count=0;
            function reloadcode()
            {
                var verify=document.getElementById('icode');
                verify.src=verify.src+'?';
            }
            function show(me)
                    {
            if (count==0)
            {
                me.value="";
            }
                                                                           			}
            function keydown(me)
                    {
            if (count==0)
            {
                me.value="";
                count=1;
                me.style.color="black";
            }
                                                                           			}
            function yzblur(me)
                    {
            if(me.value=="")
            {
                //me.value="��������֤��";
                //me.style.color="#92a4bf";
                count=0;
            }
                                                                           			}
                   		</script>
                   	</HEAD>
                   	<body class="login_bg">
                   		<form name="form1" method="post" action="default2.aspx" id="form1">
                   <input type="hidden" name="__VIEWSTATE" value="dDw3OTkxMjIwNTU7Oz72G0jnx2CVi9cEqCETKg2lgGSYBw==" />


                   			<div class="login_main">
                   				<div class="login_logo">
                   					<h2><img src="logo/logo_school.png"></h2>
                   					<h3><img src="logo/logo_jw.png"></h3>
                   				</div>
                   				<div class="login_left"><IMG class="login_pic" src="logo/login_pic.png"></div>
                   				<div class="login_right">
                   					<dl style="position:relative;">
                   						<dt class="uesr">
                   							<label>�û���</label>
                   						</dt>
                   						<dd>
                   							<input name="TextBox1" type="text" id="TextBox1" tabindex="1" class="text_nor" /></dd>
                   						<dt class="passw">
                   							<label>�ܡ��룺</label>
                   						</dt>
                   						<dd>
                   							<input name="TextBox2" type="password" id="TextBox2" tabindex="2" class="text_nor" /></dd>
                   						<dt class="yzm">
                   							<label id="lbYzm">��֤�룺</label>
                   						</dt>
                   						<dd>
                   							<input name="TextBox3" type="text" id="TextBox3" tabindex="3" class="text_nor" alt="�����壬��һ��" title="�����壬��һ��" onfocus="show(this);
                   							" onkeydown="keydown(this);" onblur="yzblur(this);" style="width:55px;height:18px;line-height:18px;padding:1px 2px;color:#92a4bf;font-size:14px;" />
                   							<img style="position:absolute;top:72px;_top:84px;left:130px;" id="icode" src="CheckCode.aspx" onclick="reloadcode();"
                   							 alt="�����壬��һ��" title="�����壬��һ��"/>
                   							<a onclick="reloadcode();" style="color:blue;position:absolute;top:74px;_top:83px;right:-28px;line-height:14px;width:40px;
                   							word-wrap:break-word;word-break:break-all;cursor:pointer">�����廻һ��</a>
                   						</dd>
                   						<dt></dt>
                   						<dd>
                   							<table id="RadioButtonList1" border="0">
                   	<tr>
                   		<td><input id="RadioButtonList1_0" type="radio" name="RadioButtonList1" value="����" tabindex="4" />
                   		<label for="RadioButtonList1_0">����</label></td><td><input id="RadioButtonList1_1" type="radio" name="RadioButtonList1" value="��ʦ" tabindex="4" />
                   		<label for="RadioButtonList1_1">��ʦ</label></td><td><input id="RadioButtonList1_2" type="radio" name="RadioButtonList1" value="ѧ��"
                   		checked="checked" tabindex="4" /><label for="RadioButtonList1_2">ѧ��</label></td><td>
                   		<input id="RadioButtonList1_3" type="radio" name="RadioButtonList1" value="�ÿ�" tabindex="4" /><label for="RadioButto*/
            List<QueryEntity> keyWords = new ArrayList<QueryEntity>();
            // 添加查询元素信息，这里新定义了一个实例类
            keyWords.add(new QueryEntity("input[name=__VIEWSTATE]", "val", null));
            values = getValuesByKeyWords(tempHtml, keyWords);
            values.add(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 数组第一个值为VIEWSTATE，第二个值为Cookies

        Log.e(TAG, "Conn: VIEWSTATE==="+values.get(0));
        Log.e(TAG, "Conn: Cookies==="+values.get(1));
        return values;
    }

    /**
     * 调用方法把相应内容转换为字符串
     *
     * @param response 传入连接服务后返回的Response对象
     * @return 返回HTML字符串
     */
    public static String parseToString(HttpResponse response) throws Exception {
        InputStream is = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }
        reader.close();
        is.close();
        return builder.toString();
    }

    /**
     * 调用方法返回一个Drawbale验证码
     *
     * @param httpClient 传入一个访问系统的HttpClient对象
     * @return 返回一个验证码图片
     */
    @SuppressWarnings("deprecation")
    public static Drawable GetImageCode(HttpClient httpClient) {
        Drawable drawable = null;
        try {
            HttpGet getMainUrl = new HttpGet(
                    "http://jw2012.gdcp.cn/CheckCode.aspx");
            HttpResponse response = httpClient.execute(getMainUrl);
            InputStream Stream = response.getEntity().getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(Stream);
            drawable = new BitmapDrawable(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * 调用方法返回一个Drawbale验证码
     *
     * @param httpClient 传入一个访问系统的HttpClient对象
     * @return 返回一个验证码图片
     */
    @SuppressWarnings("deprecation")
    public static Drawable GetImageCodeAgain(HttpClient httpClient) {
        Drawable drawable = null;
        try {
            HttpGet getMainUrl = new HttpGet(
                    "http://jw2012.gdcp.cn/CheckCode.aspx?");
            HttpResponse response = httpClient.execute(getMainUrl);
            InputStream Stream = response.getEntity().getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(Stream);
            drawable = new BitmapDrawable(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * 判断是否登录方正教务成功
     *
     * @param httpClient 传入一个访问系统的HttpClient对象
     * @param arr        传入一个数组【学号，密码，验证码】
     * @param value      传入登录系统返回的Values
     * @return 返回是否登录成功
     */
    public static int ClickIn(HttpClient httpClient, String[] arr,
                              List<String> value, Handler handler) {
        String dataFromHtml = null;
        try {
            // 创建一个HttpPost实例，进行模拟登录操作
            final HttpPost httpPost = new HttpPost(
                    "http://jw2012.gdcp.cn/default2.aspx");
            // 设置HttpPost的头信息
            httpPost.addHeader("Cookie", value.get(1));
            httpPost.addHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate");
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");
            httpPost.addHeader("Referer", "http://jw2012.gdcp.cn/");
            // 设置内容实体
            final List<NameValuePair> requestEntity = new ArrayList<NameValuePair>();
            requestEntity.add(new BasicNameValuePair("__VIEWSTATE", value
                    .get(0)));
            requestEntity.add(new BasicNameValuePair("TextBox1", arr[0]));
            requestEntity.add(new BasicNameValuePair("TextBox2", arr[1]));
            requestEntity.add(new BasicNameValuePair("TextBox3", arr[2]));
            requestEntity.add(new BasicNameValuePair("RadioButtonList1",
                    "%D1%A7%C9%FA"));
            requestEntity.add(new BasicNameValuePair("Button1", ""));
            requestEntity.add(new BasicNameValuePair("lbLanguage", ""));
            requestEntity.add(new BasicNameValuePair("hidPdrs", ""));
            requestEntity.add(new BasicNameValuePair("hidsc", ""));
            httpPost.setEntity(new UrlEncodedFormEntity(requestEntity, "gb2312"));
            response = httpClient.execute(httpPost);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            InputStream inStream;
            try {
                inStream = response.getEntity().getContent();
                Header encoding = response.getEntity().getContentEncoding();
                String string = encoding.toString();
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100]; // buff锟斤拷锟节达拷锟窖拷锟斤拷锟饺★拷锟斤拷锟绞憋拷锟斤拷
                int rc = 0;
                while ((rc = inStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                byte[] in_b = swapStream.toByteArray();
                byte[] uncompress = uncompress(in_b);
                dataFromHtml = new String(uncompress, "GBK");
                Log.e(TAG, "ClickIn: dataFromHtml"+dataFromHtml);
                // System.out.println(dataFromHtml);
                List<QueryEntity> keyWords = new ArrayList<QueryEntity>();
                keyWords.add(new QueryEntity("span#xhxm", "text", null));
                realName = getValuesByKeyWords(dataFromHtml, keyWords).get(0);
                Log.e(TAG, "ClickIn: realName"+realName);
                if (realName == null) {
                    return -1;
                } else {
                    Message msg = Message.obtain();
                    msg.what = LoginPage.NAMEEXIST;
                    msg.obj = realName;
                    handler.sendMessage(msg);
                    return 1;
                }
            } catch (Exception e) {
                Document doc = Jsoup.parse(dataFromHtml + "");
                Elements elementsByTag = doc.getElementsByTag("script");
                // System.out.println(dataFromHtml);
                if (elementsByTag.size()==1) {
                    String a = elementsByTag.get(1).toString();
                    Message msg = Message.obtain();
                    msg.obj = a;
                    msg.what = LoginPage.SCRIPTTAG;
                    handler.sendMessage(msg);
                }
                e.printStackTrace();
                return 0;

            }
        } else {
            return -1;
        }
    }

    /**
     * 将传入的bytes类型gzip数组解压为正常bytes数组
     *
     * @param bytes 传入被gzip压缩的bytes数组
     * @return 返回gzip解压的bytes数组
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
        }

        return out.toByteArray();
    }

    /**
     * 传入查询集合，获取需要查询元素的值，使用java反射进行封装，简化操作
     *
     * @param html          传入返回的HTML数据
     * @param queryEntities 传入要查询的QueryEntity
     * @return 返回查询的结果
     */
    public static List<String> getValuesByKeyWords(String html,
                                                   List<QueryEntity> queryEntities) throws Exception {
        List<String> values = new ArrayList<String>();
        Element body = Jsoup.parse(html).select("body").get(0);
        for (QueryEntity entity : queryEntities) {
            Element element = body.select(entity.targetSelector).get(0);
            java.lang.reflect.Method method = null;
            String value = null;
            Class<?> clazz = element.getClass();
            if (entity.methodParms == null) {
                method = clazz.getMethod(entity.methodName);
                value = (String) method.invoke(element, new Object[]{});
            } else {
                method = clazz.getMethod(entity.methodName,
                        new Class[]{String.class});
                value = (String) method.invoke(element,
                        new Object[]{entity.methodParms});
            }
            values.add(value);
        }

        return values;
    }

    public static String getXSKCBSpinner(HttpClient httpClient,
                                         List<String> value, String stuNO, String name) {
        String s = "";
        try {
            final HttpPost httpPost = new HttpPost(
                    "http://jw2012.gdcp.cn/xskbcx.aspx?xh=" + stuNO + "&xm=" + name + "&gnmkdm=N121603");
            httpPost.addHeader("Cookie", value.get(1));
            httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader("Referer", "http://jw2012.gdcp.cn/xs_main.aspx?xh=" + stuNO);
            response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inStream = response.getEntity().getContent();
                Header encoding = response.getEntity().getContentEncoding();
                String string = encoding.toString();
                System.out.println(string);
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
                int rc = 0;
                while ((rc = inStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                byte[] in_b = swapStream.toByteArray();
                byte[] uncompress = uncompress(in_b);
                s = new String(uncompress, "GBK");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 拿到教务系统的全部课程表数据，必须保证账号密码和登录系统的账号密码一致
     *
     * @param httpClient 传入一个访问系统的HttpClient对象
     * @param value      传入登录系统返回的Values
     * @param stuNO      传入查询学生的学号
     * @param name       传入查询学生的姓名
     * @return 返回课程表的全部HTML数据
     */
    public static String getXSKCBfromHTML(HttpClient httpClient,
                                          List<String> value, String stuNO, String name, String[] tem) {
        String s = "";
        try {
            final HttpPost httpPost = new HttpPost(
                    "http://jw2012.gdcp.cn/xskbcx.aspx?xh=" + stuNO + "&xm="
                            + name + "&gnmkdm=N121603");
            httpPost.addHeader("Cookie", value.get(1));
            httpPost.addHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate");
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");
            httpPost.addHeader("Referer",
                    "http://jw2012.gdcp.cn/xs_main.aspx?xh=" + stuNO);
            final List<NameValuePair> requestEntity = new ArrayList<NameValuePair>();
            requestEntity.add(new BasicNameValuePair("__EVENTTARGET", "xqd"));
            requestEntity.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
            requestEntity
                    .add(new BasicNameValuePair(
                            "__VIEWSTATE",
                            "dDwtODAxODI2NDQzO3Q8O2w8aTwwPjtpPDE+O2k8Mj47aTwzPjtpPDQ+O2k8NT47" +
                                    "PjtsPHQ8O2w8aTwxPjtpPDM+O2k8NT47aTw5Pjs+O2w8dDw7bDxpPDA+" +
                                    "Oz47bDx0PDtsPGk8MD47aTwxPjtpPDM+O2k8Nj47PjtsPHQ8cDxwPGw8VGV" +
                                    "4dDs+O2w8XGU7Pj47Pjs7Pjt0PHQ8cDxwPGw8RGF0YVRleHRGaWVsZDtEY" +
                                    "XRhVmFsdWVGaWVsZDs+O2w8eG47eG47Pj47Pjt0PGk8Mz47QDwyMDE2LT" +
                                    "IwMTc7MjAxNS0yMDE2O1xlOz47QDwyMDE2LTIwMTc7MjAxNS0yMDE2O1xl" +
                                    "Oz4+O2w8aTwxPjs+Pjs7Pjt0PHQ8OztsPGk8MD47Pj47Oz47dDw7bDxpPD" +
                                    "A+Oz47bDx0PHQ8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+OztsPGk8" +
                                    "MD47Pj47Oz47Pj47Pj47Pj47dDw7bDxpPDA+Oz47bDx0PDtsPGk8MT47aT" +
                                    "wzPjtpPDU+O2k8Nz47aTw5Pjs+O2w8dDx0PHA8cDxsPERhdGFUZXh0Rmllb" +
                                    "GQ7RGF0YVZhbHVlRmllbGQ7PjtsPG5qO25qOz4+Oz47dDxpPDg+O0A8MjA" +
                                    "xNjsyMDE1OzIwMTQ7MjAxMzsyMDEyOzIwMTE7MjAxMDtcZTs+O0A8MjAx" +
                                    "NjsyMDE1OzIwMTQ7MjAxMzsyMDEyOzIwMTE7MjAxMDtcZTs+PjtsPGk8" +
                                    "MD47Pj47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVl" +
                                    "RmllbGQ7PjtsPHh5bWM7eHlkbTs+Pjs+O3Q8aTwxMD47QDzlnJ/mnKjl" +
                                    "t6XnqIvlrabpmaI75rG96L2m5LiO5py65qKw5bel56iL5a2m6ZmiO+i/" +
                                    "kOi+k+euoeeQhuWtpumZojvorqHnrpfmnLrlt6XnqIvlrabpmaI76L2o" +
                                    "6YGT5Lqk6YCa5a2m6ZmiO+a1t+S6i+WtpumZojvnlLXlrZDkuI7pgJrk" +
                                    "v6Hlt6XnqIvlrabpmaI75ZWG6LS45a2m6ZmiO+acuueUteW3peeoi+Wtp" +
                                    "umZojtcZTs+O0A8NTE7NTI7NTM7NTQ7NTU7NTY7NTc7NTg7NjQ7XGU7Pj" +
                                    "47bDxpPDA+Oz4+Ozs+O3Q8dDxwPHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFW" +
                                    "YWx1ZUZpZWxkOz47bDx6eW1jO3p5ZG07Pj47Pjt0PGk8MjE+O0A85bu656" +
                                    "2R5bel56iL566h55CG77yI6YGT6Lev5qGl5qKB77yJKDLlubTliLYpO+W3" +
                                    "peeoi+a1i+mHj+aKgOacrzvln47luILovajpgZPkuqTpgJrlt6XnqIvmio" +
                                    "DmnK/vvIjlronlhajmioDmnK/nrqHnkIbvvIk75Z+O5biC6L2o6YGT5Lqk" +
                                    "6YCa5bel56iL5oqA5pyv77yI5pa95bel5rWL6YeP77yJO+W4guaUv+W3pe" +
                                    "eoi+aKgOacrygy5bm05Yi2KTvpq5jnrYnnuqflhazot6/nu7TmiqTkuI7n" +
                                    "rqHnkIY75bu6562R5bel56iL5oqA5pyvKDLlubTliLYpO+W7uuetkeW3peeo" +
                                    "i+aKgOacryjlu7rnrZHlraYpO+eJqeS4mueuoeeQhjvlu7rnrZHlt6XnqI" +
                                    "vnrqHnkIYo6Lev5qGl5pa55ZCRKTvmiL/lnLDkuqfnu4/okKXkuI7kvL" +
                                    "Dku7c75Z+O5biC6L2o6YGT5Lqk6YCa5bel56iL5oqA5pyvO+mBk+i3r+a" +
                                    "hpeaigeW3peeoi+aKgOacryjlt6XnqIvnm5HnkIbmlrnlkJEpO+WFrOi" +
                                    "3r+S4juahpeaigeW3peeoi+aKgOacrzvluILmlL/lt6XnqIvmioDmnK875b" +
                                    "u6562R5bel56iL5oqA5pyvO+W7uuetkeW3peeoi+aKgOacryjlt6XnqIv" +
                                    "nm5HnkIbmlrnlkJEpO+W3peeoi+mAoOS7tzvpgZPot6/moaXmooHlt6Xn" +
                                    "qIvmioDmnK876YGT6Lev5qGl5qKB5bel56iL5oqA5pyvO1xlOz47QDwwM" +
                                    "TAzOzAxMDg7MDE3NTswMTc2OzAyNjI7MDI3MzswMjU0OzAyNTU7MDExND" +
                                    "swMTEyOzAxMTM7MDE3MTswMTA1OzAxMDY7MDEwNzswMTA5OzAxMTA7MDE" +
                                    "xMTswMTA0OzAxMDI7XGU7Pj47bDxpPDA+Oz4+Ozs+O3Q8dDxwPHA8bDxE" +
                                    "YXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDxiam1jO2JqZG07Pj" +
                                    "47Pjt0PGk8MT47QDxcZTs+O0A8XGU7Pj47bDxpPDA+Oz4+Ozs+O3Q8dDxw" +
                                    "PHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDx4bTt4aDs" +
                                    "+Pjs+O3Q8aTwxPjtAPFxlOz47QDxcZTs+Pjs+Ozs+Oz4+Oz4+O3Q8O2w8" +
                                    "aTwwPjs+O2w8dDw7bDxpPDA+O2k8Mj47aTw0PjtpPDY+O2k8OD47aTwxM" +
                                    "T47PjtsPHQ8cDxwPGw8VGV4dDs+O2w85a2m5Y+377yaMTUxMzE1NzE0MT" +
                                    "s+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85aeT5ZCN77ya6LCi5b+X5p2" +
                                    "wOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlrabpmaLvvJrorqHnrpfmn" +
                                    "Lrlt6XnqIvlrabpmaI7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOS4k+S" +
                                    "4mu+8mui9r+S7tuaKgOacryjova/ku7blt6XnqIvmlrnlkJEpOz4+Oz47" +
                                    "Oz47dDxwPHA8bDxUZXh0Oz47bDzooYzmlL/nj63vvJoxNei9r+S7tuaKg" +
                                    "Oacr++8iDHvvIk7Pj47Pjs7Pjt0PHQ8OztsPGk8MD47Pj47Oz47Pj47P" +
                                    "j47dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU" +
                                    "291cmNlSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE+O2k8MD47aTwwP" +
                                    "jtsPD47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+Oz4+O3Q8QDA8cDxwPGw8UGFnZ" +
                                    "UNvdW50O18hSXRlbUNvdW50O18hRGF0YVNvdXJjZUl0ZW1Db3VudDtEYX" +
                                    "RhS2V5czs+O2w8aTwxPjtpPDE+O2k8MT47bDw+Oz4+Oz47Ozs7Ozs7Ozs" +
                                    "7PjtsPGk8MD47PjtsPHQ8O2w8aTwxPjs+O2w8dDw7bDxpPDA+O2k8MT4" +
                                    "7aTwyPjtpPDM+O2k8ND47aTw1Pjs+O2w8dDxwPHA8bDxUZXh0Oz47bDz" +
                                    "lhpvorq3kuI7lhaXlrabmlZnogrI7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ" +
                                    "7PjtsPOWNouS9kOS5oDs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8Mjs" +
                                    "+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MDMtMDMsMjAtMjA7Pj47Pjs" +
                                    "7Pjt0PHA8cDxsPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjt0PHA8cDx" +
                                    "sPFRleHQ7PjtsPCZuYnNwXDs7Pj47Pjs7Pjs+Pjs+Pjs+Pjt0PHA8bDx" +
                                    "WaXNpYmxlOz47bDxvPGY+Oz4+Ozs+O3Q8cDxsPFZpc2libGU7PjtsPG8" +
                                    "8Zj47Pj47bDxpPDA+Oz47bDx0PDtsPGk8MD47PjtsPHQ8QDA8cDxwPGw" +
                                    "8UGFnZUNvdW50O18hSXRlbUNvdW50O18hRGF0YVNvdXJjZUl0ZW1Db3V" +
                                    "udDtEYXRhS2V5czs+O2w8aTwxPjtpPDA+O2k8MD47bDw+Oz4+Oz47Ozs" +
                                    "7Ozs7Ozs7Pjs7Pjs+Pjs+Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl" +
                                    "0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7Pjt" +
                                    "sPGk8MT47aTwwPjtpPDA+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47dDx" +
                                    "AMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291cmN" +
                                    "lSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE+O2k8MT47aTwxPjtsPD4" +
                                    "7Pj47Pjs7Ozs7Ozs7Ozs+O2w8aTwwPjs+O2w8dDw7bDxpPDE+Oz47bDx" +
                                    "0PDtsPGk8MD47aTwxPjtpPDI+O2k8Mz47aTw0Pjs+O2w8dDxwPHA8bDx" +
                                    "UZXh0Oz47bDwyMDE1LTIwMTY7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7Pjt" +
                                    "sPDE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWGm+iureS4juWFpeW" +
                                    "tpuaVmeiCsjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85Y2i5L2Q5Lm" +
                                    "gOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDwyOz4+Oz47Oz47Pj47Pj4" +
                                    "7Pj47Pj47Pit99nSz4MSGScH4HkY4/dp1Dy9t"));

            requestEntity.add(new BasicNameValuePair("xnd", tem[0]));
            requestEntity.add(new BasicNameValuePair("xqd", tem[1]));
            httpPost.setEntity(new UrlEncodedFormEntity(requestEntity, "gb2312"));
            response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inStream = response.getEntity().getContent();
                Header encoding = response.getEntity().getContentEncoding();
                String string = encoding.toString();
                System.out.println(string);
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100]; // buff锟斤拷锟节达拷锟窖拷锟斤拷锟饺★拷锟斤拷锟绞憋拷锟斤拷
                int rc = 0;
                while ((rc = inStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                byte[] in_b = swapStream.toByteArray();
                byte[] uncompress = uncompress(in_b);
                s = new String(uncompress, "GBK");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 拿到课程表后调用此方法，拿到具体items的课程内容
     *
     * @param line 查询课程第几行
     * @param col  查询课程第几列
     * @param html 传入getXSKCBfromHTML返回的HTML数据
     * @return 返回具体课程数据
     */
    public static String getItemFromHTML(int line, int col, String html) {
        Document doc = Jsoup.parse(html);
        Element elementBy_table1 = doc.getElementById("Table1");
        Elements elementBy_tr = elementBy_table1.select("tr");
        Element index_tr = elementBy_tr.get(line);
        Elements elementsBy_td = index_tr.getElementsByTag("td");
        Element index_td = elementsBy_td.get(col);
        String returnData = index_td.text().toString();
        return returnData;
    }

    /**
     * 将传入的String类型数据gzip解压为正常String数据
     *
     * @param //被gzip压缩的String数据
     * @return 返回gzip解压的String数据
     */
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(
                str.getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return new String(out.toByteArray(), "GBK");
    }

    /**
     * @param httpClient 服务连接对象
     * @param value      传入一个含有cookies和VIEWSTATE的数组
     * @param stuNO      传入学号
     * @param name       传入姓名
     * @return 返回一个htmlData
     */
    public static String getXSCJfromHTML(HttpClient httpClient,
                                         List<String> value, String stuNO, String name, String[] data) {
        String s = "";
        try {
            final HttpPost httpPost = new HttpPost(
                    "http://jw2012.gdcp.cn/xscj_gc.aspx?xh=" + stuNO + "&xm="
                            + "谢" + "&gnmkdm=N121603");
            httpPost.addHeader("Cookie", value.get(1));
            httpPost.addHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate");
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");
            httpPost.addHeader("Referer",
                    "http://jw2012.gdcp.cn/xs_main.aspx?xh=" + stuNO);
            final List<NameValuePair> requestEntity = new ArrayList<NameValuePair>();
            requestEntity.add(new BasicNameValuePair("__VIEWSTATE", value
                    .get(0)));
            requestEntity.add(new BasicNameValuePair("ddlXN", data[0]));
            requestEntity.add(new BasicNameValuePair("ddlXQ", data[1]));
            requestEntity.add(new BasicNameValuePair("Button1", "按学期查询"));
            httpPost.setEntity(new UrlEncodedFormEntity(requestEntity, "gb2312"));
            response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inStream = response.getEntity().getContent();
                Header encoding = response.getEntity().getContentEncoding();
                String string = encoding.toString();
                System.out.println(string);
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
                int rc = 0;
                while ((rc = inStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                byte[] in_b = swapStream.toByteArray();
                byte[] uncompress = uncompress(in_b);
                s = new String(uncompress, "GBK");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static List<String> getClickXSCJfromHTML(HttpClient httpClient,
                                                    List<String> value, String stuNO, String name) {
        String s = "";
        List<String> values = null;
        try {
            final HttpPost httpPost = new HttpPost(
                    "http://jw2012.gdcp.cn/xscj_gc.aspx?xh=" + stuNO + "&xm="
                            + "谢" + "&gnmkdm=N121603");
            httpPost.addHeader("Cookie", value.get(1));
            httpPost.addHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate");
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");
            httpPost.addHeader("Referer",
                    "http://jw2012.gdcp.cn/xs_main.aspx?xh=" + stuNO);
            final List<NameValuePair> requestEntity = new ArrayList<NameValuePair>();
            httpPost.setEntity(new UrlEncodedFormEntity(requestEntity, "gb2312"));
            response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inStream = response.getEntity().getContent();
                Header encoding = response.getEntity().getContentEncoding();
                String string = encoding.toString();
                System.out.println(string);
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
                int rc = 0;
                while ((rc = inStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                byte[] in_b = swapStream.toByteArray();
                byte[] uncompress = uncompress(in_b);
                s = new String(uncompress, "GBK");

                List<QueryEntity> keyWords = new ArrayList<QueryEntity>();
                keyWords.add(new QueryEntity("input[name=__VIEWSTATE]", "val",
                        null));
                // 第0个值是viewstate，第1个值是cookies，第2个值是html
                values = getValuesByKeyWords(s, keyWords);
                values.add(value.get(1));
                values.add(s);
                return values;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 连接饭卡系统
     *
     * @param httpClient 传入一个已创建的httpclient
     */
    public static void ConnToCardSystem(HttpClient httpClient) {
        try {
            HttpGet getMainUrl = new HttpGet("http://card.gdcp.cn/pages/card/homeLogin.action");
            HttpResponse response = httpClient.execute(getMainUrl);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取加密键盘Drawable对象
     *
     * @param httpClient 传入一个已创建的httpclient对象
     * @return 返回一个Drawable对象
     */
    public static Drawable getPassImage(HttpClient httpClient) {
        Drawable drawable = null;
        try {
            HttpGet getMainUrl = new HttpGet("http://card.gdcp.cn/getpasswdPhoto.action");
            HttpResponse response = httpClient.execute(getMainUrl);
            InputStream Stream = response.getEntity().getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(Stream);
            drawable = new BitmapDrawable(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * 必须调用取得验证码，否则连接失败
     *
     * @param httpClient 传入已创建的httpclient对象
     * @return
     */
    public static void GetCode(HttpClient httpClient) {
        try {
            HttpGet getMainUrl = new HttpGet("http://card.gdcp.cn/pages/card/getCheckpic.action?rand=2011.7839089697563");
            HttpResponse execute = httpClient.execute(getMainUrl);
            System.out.println(execute.getStatusLine().getStatusCode() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登入饭卡系统
     *
     * @param httpClient 传入一个已经创建好的httpclient对象
     * @param password   传入加密后的密码
     */
    public static String ClickInCardSystem(HttpClient httpClient, String id,
                                           String password) {
        try {
            final HttpPost httpPost = new HttpPost(
                    "http://card.gdcp.cn/pages/card/loginstudent.action");
            httpPost.addHeader(
                    "Accept",
                    "image/jpeg,application/x-ms-application,image/gif,application/xaml+xml,image/pjpeg,application/x-ms-xbap,*/*");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate");
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");
            httpPost.addHeader("Referer",
                    "http://card.gdcp.cn/pages/card/homeLogin.action");
            final List<NameValuePair> requestEntity = new ArrayList<NameValuePair>();
            requestEntity.add(new BasicNameValuePair("name", id));
            requestEntity.add(new BasicNameValuePair("userType", "1"));
            requestEntity.add(new BasicNameValuePair("passwd", password));
            requestEntity.add(new BasicNameValuePair("loginType", "2"));
            requestEntity.add(new BasicNameValuePair("rand", "2011"));
            requestEntity.add(new BasicNameValuePair("imageField.x", "26"));
            requestEntity.add(new BasicNameValuePair("imageField.y", "12"));
            httpPost.setEntity(new UrlEncodedFormEntity(requestEntity, "gb2312"));
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inStream = response.getEntity().getContent();
                byte[] streamToByte = StreamToByte(inStream);
                String a = new String(streamToByte, "GBK");
                return a;
            }
        } catch (Exception e) {
            System.out.println("登录失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取对应账号的卡号
     *
     * @param httpClient 传入一个已经创建的httpclient对象
     * @return 返回html数据
     */
    public static String GetPersonDayHtml(HttpClient httpClient) {
        try {
            HttpGet getMainUrl = new HttpGet("http://card.gdcp.cn/accounttodayTrjn.action");
            HttpResponse response = httpClient.execute(getMainUrl);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inStream = response.getEntity().getContent();
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
                int rc = 0;
                while ((rc = inStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                byte[] in_b = swapStream.toByteArray();
                byte[] uncompress = uncompress(in_b);
                String a = new String(uncompress, "GBK");
                System.out.println(a);
                return a;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static byte[] StreamToByte(InputStream stream) {
        byte[] in_b = null;
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = stream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
                in_b = swapStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in_b;
    }

    /**
     * 获取对应账号的卡号
     *
     * @param httpClient 传入一个已经创建的httpclient对象
     * @return 返回个人页面的html信息
     */
    public static String GetPersonAccountHTML(HttpClient httpClient) {
        try {
            HttpGet getMainUrl = new HttpGet(
                    "http://card.gdcp.cn/accountcardUser.action");
            HttpResponse response = httpClient.execute(getMainUrl);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inStream = response.getEntity().getContent();
                byte[] streamToByte = StreamToByte(inStream);
                String a = new String(streamToByte);
                return a;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
