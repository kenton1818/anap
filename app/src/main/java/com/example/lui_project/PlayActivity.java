package com.example.lui_project;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import com.example.lui_project.base.BaseActivity;
import com.example.lui_project.utils.SaveKeyValues;
public class PlayActivity extends BaseActivity implements View.OnClickListener{

    private int index;
    private int what;
    private boolean isNext;
    private boolean isOff;//-->defaule off
    private TextView play_time;
    private TextView play_name;
    private TextView play_message;
    private boolean isChange;
    private TextView play_more;
    private TextView play_back;
    private ImageView imageView;
    private ImageView play_switch,play_next;//播放開關
    private AnimationDrawable animationDrawable;
    private static final String zeroStr = "00:00";
    private Button back_sport;
    private LinearLayout one,two;
    private int doplan;
    private boolean isClose;//-->timer
    private Thread thread;
    private int values;
    private ProgressBar progressBar;

    public static String[] warm_up_exercise = new String[5];
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    int values1 = (int) msg.obj;
                    if (isNext){
                        return false;
                    }
                    if (values1 == 11){
                        handler.removeMessages(1);
                        animationDrawable.stop();
                        isOff = false;
                        isClose = true;
                    }
                    progressBar.setProgress(values);
                    if (values1 < 10){
                        play_time.setText("00:0"+values);
                    }else {
                        play_time.setText("00:" + values);
                    }
                    if (values1 == 12){
                        play_switch.setImageResource(R.mipmap.sport_play_start);
                        Toast.makeText(PlayActivity.this, PlayActivity.this.getString(R.string.Play_end), Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private int[] frameRes = new int[]{R.drawable.warmup_animation1,
            R.drawable.warmup_animation2,
            R.drawable.warmup_animation3,
            R.drawable.warmup_animation4,
            R.drawable.warmup_animation5};
    @Override
    protected void setActivityTitle() {
        initTitle();
        setTitle(PlayActivity.this.getString(R.string.Play_Title), this);
        setMyBackGround(R.color.watm_background_gray);
        setTitleTextColor(R.color.theme_blue_two);
        setTitleLeftImage(R.mipmap.sport_back_blue);
    }

    @Override
    protected void getLayoutToView() {
        setContentView(R.layout.activity_play);
    }

    @Override
    protected void initValues() {
        isNext = false;
        index = getIntent().getIntExtra("play_type",0);
        what = getIntent().getIntExtra("what",0);
        doplan = getIntent().getIntExtra("do_hint",0);
        if (doplan == 1){
            SaveKeyValues.putIntValues("do_hint",0);
        }
    }

    @Override
    protected void initViews() {
        imageView = (ImageView) findViewById(R.id.play_image);
        play_name = (TextView) findViewById(R.id.play_name);
        play_more = (TextView) findViewById(R.id.play_more);
        play_message = (TextView) findViewById(R.id.play_message);
        play_back = (TextView) findViewById(R.id.play_back);
        play_switch = (ImageView) findViewById(R.id.play_on_or_off);
        progressBar = (ProgressBar) findViewById(R.id.play_progress);
        play_time = (TextView) findViewById(R.id.play_time);
        one = (LinearLayout) findViewById(R.id.down_one);
        two = (LinearLayout) findViewById(R.id.down_two);
        play_next = (ImageView) findViewById(R.id.play_next);
        back_sport = (Button) findViewById(R.id.back_sport);
    }

    @Override
    protected void setViewsListener() {
        play_back.setOnClickListener(this);
        play_more.setOnClickListener(this);
        play_switch.setOnClickListener(this);
        play_next.setOnClickListener(this);
        back_sport.setOnClickListener(this);
    }

    @Override
    protected void setViewsFunction() {
        if (what == 1){
            play_next.setVisibility(View.GONE);
            back_sport.setVisibility(View.GONE);
        }else {
            play_next.setVisibility(View.VISIBLE);
            back_sport.setVisibility(View.VISIBLE);
        }

        setShow();

        warm_up_exercise[0]=this.getString(R.string.warm_up_exercise0);
        warm_up_exercise[1]=this.getString(R.string.warm_up_exercise1);
        warm_up_exercise[2]=this.getString(R.string.warm_up_exercise2);
        warm_up_exercise[3]=this.getString(R.string.warm_up_exercise3);
        warm_up_exercise[4]=this.getString(R.string.warm_up_exercise4);
        play_name.setText(warm_up_exercise[index]);
        imageView.setImageResource(frameRes[index]);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        if (animationDrawable.isRunning()){
            animationDrawable.stop();
        }

        String message = getMyText(index);
//        Log.e("說明","【"+ message +"】");
        play_message.setText(message);
    }

    private void setShow(){
        one.setVisibility(View.VISIBLE);
        two.setVisibility(View.GONE);
    }
    private void setShow2(){
        one.setVisibility(View.GONE);
        two.setVisibility(View.VISIBLE);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.play_on_or_off://switch
                isNext = false;
                if (!isOff){
                    animationDrawable.start();
                    values = 0;
                    play_switch.setImageResource(R.mipmap.sport_play_stop);
                    isOff = true;
                    isClose = false;
                    runThread();

                }else {
                    animationDrawable.stop();
                    values = 0;
                    isOff = false;
                    play_switch.setImageResource(R.mipmap.sport_play_start);
                    isClose = true;
                    handler.removeMessages(1);
                }
                break;
            case R.id.play_more://detail
                if(isChange == false){
                    setShow2();
                    isChange = true;
                }
                break;
            case R.id.play_back:
                if (isChange == true){
                    setShow();
                    isChange = false;
                }
                break;
            case R.id.play_next://play the next
                isNext = true;
                index++;
                if (index > 4){
                    Toast.makeText(this,PlayActivity.this.getString(R.string.Play_Msg), Toast.LENGTH_SHORT).show();
                }else {
                    animationDrawable.stop();
                    isOff = false;
                    isClose = true;
                    handler.removeMessages(1);
                    warm_up_exercise[0]=this.getString(R.string.warm_up_exercise0);
                    warm_up_exercise[1]=this.getString(R.string.warm_up_exercise1);
                    warm_up_exercise[2]=this.getString(R.string.warm_up_exercise2);
                    warm_up_exercise[3]=this.getString(R.string.warm_up_exercise3);
                    warm_up_exercise[4]=this.getString(R.string.warm_up_exercise4);
                    play_name.setText(warm_up_exercise[index]);
                    imageView.setImageResource(frameRes[index]);
                    animationDrawable = (AnimationDrawable) imageView.getDrawable();
                    if (animationDrawable.isRunning()){
                        animationDrawable.stop();
                    }
                    play_switch.setImageResource(R.mipmap.sport_play_start);
                    progressBar.setProgress(0);
                    play_time.setText("00:00");

                }
                break;
            case R.id.back_sport:
                finish();
                break;
            default:
                break;
        }
    }

    private void runThread(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isClose){
                    try {
                        Thread.sleep(1000);
                        Message message = Message.obtain();
                        message.obj = ++values;
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setShow();
        values = 0;
        progressBar.setProgress(0);
        play_time.setText(zeroStr);
        if (animationDrawable.isRunning()){
            animationDrawable.stop();
            isOff = false;
            play_switch.setImageResource(R.mipmap.sport_play_start);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (thread != null){
            isClose = true;
        }
    }
    /**
           *讀取文件返回String
          */

    private String getMyText(int type){

        InputStream is = null;
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = PlayActivity.this.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = PlayActivity.this.getResources().getConfiguration().locale;
        }

        String language = locale.getLanguage();
        if (language ==  "en") {
            try {
                is = getAssets().open("sport/sport" + type + "_en.txt");
                reader = new BufferedReader(new InputStreamReader(is));
                String str;
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                String text = buffer.toString();
                return text;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else{
            try {
                is = getAssets().open("sport/sport" + type + ".txt");
                reader = new BufferedReader(new InputStreamReader(is));
                String str;
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                String text = buffer.toString();
                return text;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return null;
    }
}
