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

    private TextView title_center;//The middle part of the title
    private ImageView title_left,title_right;//Left and right of the title
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
      * init title
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
      * Set the downline of the TextView
      * @param view
      */
    public void setTextViewUnderLine(TextView view){
        Paint paint = view.getPaint();
        paint.setColor(getResources().getColor(R.color.btn_gray));//Set brush color
        paint.setAntiAlias(true);//Set anti-aliasing
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);//Set underline
        view.invalidate();
    }
    /**
      * init title
      */
    protected abstract void setActivityTitle();
    /**
      * init window
      */
    protected abstract void getLayoutToView();
    /**
      * Set the value and variable of init
      */
    protected abstract void initValues();
    /**
      * init control view
      */
    protected abstract void initViews();
    /**
      * init monitor
      */
    protected abstract void setViewsListener();

    protected abstract void setViewsFunction();

    /**
      * title name
      * @param name
      */
    public void setTitle(String name){
        title_center.setText(name);
        title_left.setVisibility(View.INVISIBLE);
    }
    /**
      * Set the title with the return key function --> can change the picture of the return key
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
      * Get the button to the left of the title
      * @param name
      * @return
      */
    public ImageView setTitleLeft(String name){
        title_center.setText(name);
        title_left.setVisibility(View.VISIBLE);
        return title_left;
    }
    /**
      * Set title left center right display
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
      * Set the text color of the title
      * @param colorID
      */
    public void setTitleTextColor(int colorID){
        title_center.setTextColor(colorID);
    }

    /**
      * Set the text color of the title...
      * @param picID
      */
    public void setTitleLeftImage(int picID){
        title_left.setImageResource(picID);
    }
    /**
      *Set the image of the image button to the right of the title
      * @param picID
      */
    public void setTitleRightImage(int picID){
        title_right.setImageResource(picID);
    }
}