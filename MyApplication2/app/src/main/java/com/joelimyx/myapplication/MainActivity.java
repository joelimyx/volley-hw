package com.joelimyx.myapplication;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    Button mButton1;
    Button mButton2;
    Button mButton3;
    public static final StringBuilder GET_URL_BUILDER = new StringBuilder("http://api.walmartlabs.com/v1/search?format=json&apiKey=xp8nz8badbdjrn2pb9r7bsub&query=");

    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info = manager.getActiveNetworkInfo();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp="";
                if (info!=null && info.isConnected()) {
                    switch (view.getId()) {
                        case R.id.button1:
                            temp = "cereal";
                            break;
                        case R.id.button2:
                            temp = "tea";
                            break;
                        case R.id.button3:
                            temp = "chocolate";
                            break;
                    }
                    getResponse(GET_URL_BUILDER.toString() + temp);
                }
            }
        };

        mButton1.setOnClickListener(listener);
        mButton2.setOnClickListener(listener);
        mButton3.setOnClickListener(listener);
    }

    public void getResponse(String url){
        mRequestQueue.cancelAll("TAG");
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Walmart walmart = new Gson().fromJson(response.toString(),Walmart.class);
                mRecyclerView.setAdapter(new MainAdapter(walmart.getItems()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setTag("TAG");
        mRequestQueue.add(request);
    }
}
