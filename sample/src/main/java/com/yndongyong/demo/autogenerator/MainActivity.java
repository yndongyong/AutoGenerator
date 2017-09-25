package com.yndongyong.demo.autogenerator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yndongyong.autogenerator.Arg;
import com.yndongyong.autogenerator.AutoGenerator;
import com.yndongyong.autogenerator.BindView;
import com.yndongyong.autogenerator.OnClick;

/**
 * Created by yndongyong on 2017/9/21.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_button1)
    TextView tv_1;

    @BindView(R.id.tv_button2)
    TextView tv_2;

    @Arg("KEY_EXTRA_A")
    String extra_name;

    @Arg("KEY_EXTRA_B")
    String extra_nick_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoGenerator.inject(this);
        Log.d("MainActivity", "tv_1 :" + tv_1.getText());
        Log.d("MainActivity", "tv_2 :" + tv_2.getText());

    }
    public void doInUiThread() {
        Toast.makeText(this, "doInUiThread" , Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.tv_button1, R.id.tv_button2})
    public void onBtnClick(View view) {
        if (view.getId() == R.id.tv_button1) {
            Toast.makeText(this, "tv_1.getText():" + tv_1.getText(), Toast.LENGTH_SHORT).show();
        } else {
            doInUiThread();
        }
    }
    public void doInBackground() {

    }
}
