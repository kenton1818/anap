package com.example.lui_project.base;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lui_project.R;



public abstract class BaseActivity extends AppCompatActivity {

    private TextView title_center;//標題的中間部分
    private ImageView title_left,title_right;//標題的左邊和右邊
    private RelativeLayout title_relRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutToView();
        initValues();
        setActivityTitle();
        initViews();
        setViewsListener();
        setViewsFunction();
    }

    /**
      * 初始化標題
      */
    public void initTitle(){
        title_center = (TextView) findViewById(R.id.titles);
        title_left = (ImageView) findViewById(R.id.left_btn);
        title_right = (ImageView) findViewById(R.id.right_btn);
        title_left.setVisibility(View.INVISIBLE);
        title_right.setVisibility(View.INVISIBLE);
        title_relRelativeLayout = (RelativeLayout) findViewById(R.id.title_back);
    }
    public void setMyBackGround(int color){
        title_relRelativeLayout.setBackgroundResource(color);
    }
    /**
      * 設置TextView的下滑線
      * @param view
      */
    public void setTextViewUnderLine(TextView view){
        Paint paint = view.getPaint();
        paint.setColor(getResources().getColor(R.color.btn_gray));//設置畫筆顏色
        paint.setAntiAlias(true);//設置抗鋸齒
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);//設置下滑線
        view.invalidate();
    }
    /**
      * 初始化標題
      */
    protected abstract void setActivityTitle();
    /**
      * 初始化窗口
      */
    protected abstract void getLayoutToView();
    /**
      * 設置初始化的值和變量
      */
    protected abstract void initValues();
    /**
      * 初始化控件
      */
    protected abstract void initViews();
    /**
      * 初始化控件的監聽
      */
    protected abstract void setViewsListener();
    /**
      * 設置相關管功能
      */
    protected abstract void setViewsFunction();

    /**
      * 設置標題的名稱
      * @param name
      */
    public void setTitle(String name){
        title_center.setText(name);
        title_left.setVisibility(View.INVISIBLE);
    }
    /**
      * 設置標題有返回鍵功能-->可以改變返回鍵的圖片
      * @param name
      * @param activity
      */
    public void setTitle(String name, final Activity activity){
        title_center.setText(name);
        title_left.setVisibility(View.VISIBLE);
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    /**
      * 獲取標題左邊的按鈕
      * @param name
      * @return
      */
    public ImageView setTitleLeft(String name){
        title_center.setText(name);
        title_left.setVisibility(View.VISIBLE);
        return title_left;
    }
    /**
      * 設置標題左 中 右 全部顯示
      * @param name
      * @param activity
      * @param picID
      */
    public ImageView setTitle(String name, final Activity activity , int picID){
        title_center.setText(name);
        title_left.setVisibility(View.VISIBLE);
        title_right.setVisibility(View.VISIBLE);
        if (picID != 0){
            title_right.setImageResource(picID);
        }
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return title_right;
    }

    /**
      * 設置標題的文字顏色
      * @param colorID
      */
    public void setTitleTextColor(int colorID){
        title_center.setTextColor(colorID);
    }

    /**
      * 設置標題左側圖片按鈕的圖片
      * @param picID
      */
    public void setTitleLeftImage(int picID){
        title_left.setImageResource(picID);
    }
    /**
      * 設置標題右側圖片按鈕的圖片
      * @param picID
      */
    public void setTitleRightImage(int picID){
        title_right.setImageResource(picID);
    }
}