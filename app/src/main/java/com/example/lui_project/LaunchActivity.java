package com.example.lui_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.example.lui_project.utils.Constant;
import com.example.lui_project.utils.SaveKeyValues;


/**
 * welcome page
 */
public class LaunchActivity extends AppCompatActivity {
    private boolean isFirst;//檢查係咪第一次開app
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (isFirst){
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                }else {
                    startActivity(new Intent(LaunchActivity.this, FunctionActivity.class));
                }
                finish();
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //唔show status list
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //設置默認加載運動首頁
        SaveKeyValues.putIntValues("launch_which_fragment", Constant.TURN_MAIN);
        //判斷是否是第一次啟動
        int count = SaveKeyValues.getIntValues("count" , 0);
        isFirst = (count == 0)? true : false;
        handler.sendEmptyMessageDelayed(1, 3000);
    }
    /**
     * 隱藏返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return false;
        }
        return false;
    }
}
