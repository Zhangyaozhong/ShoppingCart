package com.bwie.android.shoppingcart.utils;

import android.os.Handler;
import android.telecom.Call;

import com.bwie.android.shoppingcart.callback.OkhttpCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkhttpUtil {
    //私有属性
    private static OkhttpUtil okhttpUtil;

    private final OkHttpClient okHttpClient;

    Handler handler = new Handler();

    //私有构造器
    private OkhttpUtil() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                //添加日志拦截器
                .addInterceptor(loggingInterceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 双重检验锁。单例模式进行实例化
     */
    public static OkhttpUtil getInstance() {
        if (okhttpUtil == null) {
            synchronized (OkhttpUtil.class) {
                if (okhttpUtil == null) {
                    okhttpUtil = new OkhttpUtil();
                }
            }
        }
        return okhttpUtil;
    }

    /**
     * get请求方式
     */
    public void doGet(String url, HashMap<String, String> parmas, final OkhttpCallback requestCallback) {

        StringBuilder p = new StringBuilder();
        if (parmas != null && parmas.size() > 0) {
            for (Map.Entry<String, String> map : parmas.entrySet()) {

                p.append(map.getKey()).append("=").append(map.getValue()).append("&");
            }

        }
        Request request = new Request.Builder().url(url + "?" + p.toString())
                .get().build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (requestCallback != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            requestCallback.failure("网络异常");
                        }
                    });
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (requestCallback != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            requestCallback.success(result);

                        }
                    });
                }
            }


        });


    }

    /**
     * post请求方式
     */
    public void doPost(String url, HashMap<String, String> parmas, final OkhttpCallback okhttpCallback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : parmas.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (okhttpCallback != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            okhttpCallback.failure("网络异常=======");
                        }
                    });
                }
            }


            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (okhttpCallback != null) {
                    if (200 == response.code()) {
                        final String result = response.body().string();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                okhttpCallback.success(result);
                            }
                        });
                    }
                }
            }


        });
    }

    /**
     * 取消所有请求
     * 好处：节省开销，内存开销，cpu开销（线程的开销）
     */
    public void cancelAllTask() {
        if (okHttpClient != null) {
            okHttpClient.dispatcher().cancelAll();
        }
    }
}
