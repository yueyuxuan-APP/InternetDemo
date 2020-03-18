package com.example.internetdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    List<Map<String,Object>> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = findViewById(R.id.tv);
        Button button = findViewById(R.id.get);
//        webview演示
//        WebView webView = findViewById(R.id.web);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("https://www.baidu.com/baidu?tn=monline_3_dg&ie=utf-8&wd=runOnUiThread");


        //联网获取数据
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    BufferedReader reader = null;
                    try {
                        URL url = new URL("https://news-at.zhihu.com/api/4/news/hot");
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        InputStream in = connection.getInputStream();
                        //读取刚刚获取的输入流
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
//                    line= reader.readLine();
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        showResponse(response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }).start();

        }
    });
}



    public void showResponse(final String string){



        Log.d("hh",string);
        try    {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("recent");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                int news_id = jsonObject1.getInt("news_id");
                String url = jsonObject1.getString("url");
                String thumbnail = jsonObject1.getString("thumbnail");
                String title = jsonObject1.getString("title");


                Map map=new HashMap();

                map.put("news_id",news_id);
                map.put("url",url);
                map.put("thumbnail",thumbnail);
                map.put("title",title);

                list.add(map);//检查到list了
            }


        }    catch (JSONException e)    {
            e.printStackTrace();
        }



    }

}
