package com.evidence.blockchainevidence.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http工具类
 */
public class HttpUtils {

    /**
     * 发送POST请求
     * @param url 请求url
     * @param data 请求数据
     * @return 结果
     */
    @SuppressWarnings("deprecation")
    public static String doPost(String url, String data) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(50000).setConnectTimeout(100000)
                .setConnectionRequestTimeout(50000).build();
        httpPost.setConfig(requestConfig);
        String context = StringUtils.EMPTY;
        if (!StringUtils.isEmpty(data)) {
            StringEntity body = new StringEntity(data, "utf-8");
            httpPost.setEntity(body);
        }
// 设置回调接口接收的消息头
        httpPost.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            context = EntityUtils.toString(entity, HTTP.UTF_8);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                response.close();
                httpPost.abort();
                httpClient.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return context;
    }

    /**
     * 解析出url参数中的键值对
     * @param url url参数
     * @return 键值对
     */
    public static Map<String, String> getRequestParam(String url) {

        Map<String, String> map = new HashMap<String, String>();
        String[] arrSplit = null;

// 每个键值为一组
        arrSplit = url.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

// 解析出键值
            if (arrSplitEqual.length > 1) {
// 正确解析
                map.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    map.put(arrSplitEqual[0], "");
                }
            }
        }
        return map;
    }




    public static String  doPost(String url, Map<String, Object> map) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);


        // 使用HttpClient发起请求，返回response
        CloseableHttpResponse response = null;
        String context="error";
        try {

            // 判断map不为空
            if (map != null) {
                // 声明存放参数的List集合
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                // 遍历map，设置参数到list中
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }

                // 创建form表单对象
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf-8");
//            formEntity.setContentType("Content-Type:application/json");

                // 把表单对象设置到httpPost中
                httpPost.setEntity(formEntity);
            }

            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            context = EntityUtils.toString(entity, HTTP.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
//            e.getStackTrace();
        } finally {
            try {
                response.close();
                httpPost.abort();
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
//                e.getStackTrace();
            }
        }
        return context;


    }


    /**
     * 以post方式调用第三方接口,以form-data形式  发送 MultipartFile文件数据
     *
     * @param url  post请求url
     * @param files  文件
     * @return
     */
    public static Boolean doPostFormData(String url, List<MultipartFile> files){
        Boolean context = false;
        try {
            // 获取OkHttpClient对象
            OkHttpClient client = new OkHttpClient();
            // 以form形式封装参数
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            //临时文件数组
            List<File> tmpfiles = new ArrayList<>();

            for (int i = 0; i < files.size(); i++) {
                String filename = files.get(i).getOriginalFilename();
                //把MultipartFile转为File形式
//                File sendfile = new File("D:\\tmp\\tmp-" + filename);
                File directory = new File("");//参数为空
                String curPath = directory.getCanonicalPath() ;

                File sendfile = new File(curPath + "/" +filename);
                files.get(i).transferTo(sendfile);
                builder.addFormDataPart("file", filename, RequestBody.create(MediaType.parse("application/octet-stream"), sendfile));

                // 会在本地产生临时文件，用完后需要删除
                tmpfiles.add(sendfile);
            }

            RequestBody requestBody = builder.build();

            // 3 封装 request
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            // 4 请求
            Response response = client.newCall(request).execute();
            // 其中Response对象就是服务器返回的数据，将数据转换成字符串
            JSONObject responseData = JSON.parseObject(response.body().string());
            if((Boolean) responseData.get("status") == true)
                context = true;

            //删除本地缓存
            for (int i = 0; i < tmpfiles.size(); i++) {
                if (tmpfiles.get(i).exists()) {
                    tmpfiles.get(i).delete();
                }
            }

            // 4 异步回调
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                //网络请求失败
//                public void onFailure(Call call, IOException e) {
//                    e.printStackTrace();
//                }
//
//                //网络请求成功
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    String test = "true";
//                }
//            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }
}