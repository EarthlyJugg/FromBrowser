package com.lingtao.frombrowser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        String data = intent.getDataString(); // 接收到网页传过来的数据：scheme://data/xxx
        Log.d("Main2ActivityFromWeb", "onCreate: " + data);
    }
}
