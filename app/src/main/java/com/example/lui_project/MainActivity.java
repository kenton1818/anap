package com.example.lui_project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lui_project.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.lui_project.utils.SaveKeyValues;


import mrkj.library.wheelview.pickerView.PickerView;
import mrkj.library.wheelview.scalerulerview.ScaleRulerView;
import mrkj.library.wheelview.utils.DateViewHelper;

/**
 * 完善信息界面
 * -->主界面是FunctionActivity
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, PickerView.onSelectListener, RadioGroup.OnCheckedChangeListener, DateViewHelper.OnResultMessageListener, View.OnFocusChangeListener{

    //TAG
    private static final String TAG = MainActivity.class.getSimpleName();
    //功能
    private DateViewHelper dateViewHelper;//日曆操作
    private LayoutInflater inflater;//佈局填充器
    private boolean closeDataPicker;//判斷顯示或隱藏日曆
    private List<String> height_list;//身高的集合
    private boolean closeHeightPicker;//判斷顯示或隱藏數字選擇器
    private boolean nextShow;//判斷按鈕是否顯示
    // control
    private LinearLayout personal_information_page_one;//完善資料1/2佈局
    private RadioGroup group;//性別選擇
    private EditText input_nick;//屬性暱稱
    private TextView input_birthday,input_height; //生日、身高
    private Button next_action;//下一步
    private LinearLayout choose_date;//日期選擇
    private LinearLayout choose_height;//選擇身高
    private PickerView height_picker;//橫下滑動選擇身高
    private ImageView back;//返回上一步
    private LinearLayout personal_information_page_two;//完善資料2/2佈局
    private ScaleRulerView input_weight;//選擇體
    private TextView show_weight;//顯示選擇的體重
    private ScaleRulerView input_length;//選擇體重
    private TextView show_length;//顯示選擇的體重
    private Button go_walk;//先去逛逛
    private Button go_make;//制定計劃/
    // /信息
    private String gender_str;//性別
    private String nick_str;//暱稱
    private String birthday_str;//生日
    private String height_str;//身高
    private int custom_age;//年齡
    private String weight_str;//體重文字
    private int weight;//體重數值
    private String length_str;//步長文字
    private int length;//步長數值

    private int year;
    private int month;
    private  int day;
    /**
     * init view
     */
    @Override
    protected void getLayoutToView() {
        setContentView(R.layout.activity_main);
    }
    /**
     * init title
     */
    @Override
    protected  void setActivityTitle(){
        initTitle();
        setTitle(getString(R.string.personal_information_one));
        setTitleTextColor(getResources().getColor(R.color.black));


    }
    /**
     * 設置init的值和變量
     */
    @Override
    protected void initValues() {
        gender_str = getResources().getString(R.string.boy);
        initHeightData();
        nextShow = true;
    }

    /**
     * init身高的集合
     */
    private void initHeightData(){
        //set 130cm至210cm
        height_list = new ArrayList<>();
        for (int i = 130;i <= 210;i++){
            height_list.add(i+"");
        }
    }

    /**
     * init control
     */
    @Override
    protected void initViews() {

        //======================================= 個人信息1/2 =======================================

        personal_information_page_one = (LinearLayout) findViewById(R.id.personal_information_page_one);
        group = (RadioGroup) findViewById(R.id.gender);
        input_nick = (EditText) findViewById(R.id.input_nick);
        input_birthday = (TextView) findViewById(R.id.input_birthday);
        input_height = (TextView) findViewById(R.id.input_height);
        next_action = (Button) findViewById(R.id.next);
        //date selector
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        choose_date=(LinearLayout) findViewById(R.id.choose_date);
        dateViewHelper = new DateViewHelper(this);
        //height selector
        choose_height = (LinearLayout) findViewById(R.id.choose_height);
        height_picker = (PickerView) findViewById(R.id.height_picker);


        //======================================= 個人信息2/2 =======================================

        personal_information_page_two = (LinearLayout) findViewById(R.id.personal_information_page_two);
        input_weight = (ScaleRulerView) findViewById(R.id.input_weight);
        show_weight = (TextView) findViewById(R.id.show_weight);
        input_length = (ScaleRulerView) findViewById(R.id.input_length);
        show_length = (TextView) findViewById(R.id.show_length);
        go_walk = (Button) findViewById(R.id.walk);
        go_make = (Button) findViewById(R.id.make);
    }

    /**
     * 體重信息
     */
    private ScaleRulerView.OnValueChangeListener input_weight_listener = new ScaleRulerView.OnValueChangeListener() {
        @Override
        public void onValueChange(float value) {
            show_weight.setText((int)value+getString(R.string.kg));
            weight = (int) value;
            weight_str = (int)value+getString(R.string.kg);
        }
    };
    /**
     * 获取步长信息
     */
    private ScaleRulerView.OnValueChangeListener input_length_listener = new ScaleRulerView.OnValueChangeListener() {
        @Override
        public void onValueChange(float value) {
            show_length.setText((int)value+getString(R.string.cm));
            length = (int) value;
            length_str = (int)value+getString(R.string.cm);
        }
    };
    /**
     * hide selector and keybroad
     */
    private View.OnTouchListener messageListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP){
                hideOthers();
            }
            return true;
        }
    };
    /**
     * init control 的監聽
     */
    @Override
    protected void setViewsListener() {

        //======================================= 個人信息1/2 =======================================

        group.setOnCheckedChangeListener(this);
        input_birthday.setOnClickListener(this);
        input_height.setOnClickListener(this);
        next_action.setOnClickListener(this);
        dateViewHelper.setOnResultMessageListener(this);
        input_nick.setOnFocusChangeListener(this);
        input_birthday.setOnFocusChangeListener(this);
        input_height.setOnFocusChangeListener(this);
        height_picker.setOnSelectListener(this);
        personal_information_page_one.setOnTouchListener(messageListener);

        //======================================= 個人信息2/2 =======================================

        input_weight.setValueChangeListener(input_weight_listener);
        input_length.setValueChangeListener(input_length_listener);
        go_walk.setOnClickListener(this);
        go_make.setOnClickListener(this);
    }
    /**
     * 設置相關功能
     */
    @Override
    protected void setViewsFunction() {

        //======================================= 個人信息1/2 =======================================

        personal_information_page_one.setVisibility(View.VISIBLE);
        input_nick.setClickable(true);
        input_birthday.setClickable(true);
        input_height.setClickable(true);
        input_nick.setFocusableInTouchMode(true);
        input_birthday.setFocusableInTouchMode(true);
        input_height.setFocusableInTouchMode(true);
        //date selector
        choose_date.addView(dateViewHelper.getDataPick(inflater));
        //height seleector
        height_picker.setData(height_list);

        //======================================= 個人信息2/2 =======================================

        personal_information_page_two.setVisibility(View.GONE);
        ///默認50千克，最小30千克，最大130千克-->單位千克
        input_weight.initViewParam(50, 130, 30);
        //默認70厘米，最小40厘米，最大100厘米-->單位厘米
        input_length.initViewParam(70, 100, 40);
    }

    /**
     * 選一個
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        hideOthers();
        //性別
        switch (checkedId){
            case R.id.boy://男
                gender_str = getResources().getString(R.string.boy);
                break;
            case R.id.girl://女
                gender_str = getResources().getString(R.string.girl);
                break;
            default:
                break;
        }
    }

    /**
     * hiden
     */
    private void hideOthers(){
        if (closeDataPicker == true){
            openPickerOrClose(false);

        }
        if (closeHeightPicker == true){
            openHeightPickerOrClose(false);

        }
        if (!nextShow){
            showNextBtn();
        }
        try{
            hideKeyBoard();
        } catch (Exception e)
        {
            Log.d("keybroad","null");
        }

    }
    /**
     * click event
     * @param v
     */
    @Override
    public void onClick(View v) {
        //======================================= 個人信息1/2 =======================================
        switch (v.getId()){
            case R.id.input_birthday://birthday
                openPickerOrClose(!closeDataPicker);
                if (!nextShow){
                    showNextBtn();
                }
                break;
            case R.id.input_height://height
                openHeightPickerOrClose(!closeHeightPicker);
                if (!nextShow){
                    showNextBtn();
                }
                break;
            case R.id.next://get  more information
                nick_str = input_nick.getText().toString();
                birthday_str = input_birthday.getText().toString();
                height_str = input_height.getText().toString();
                Log.e("information","性："+gender_str+"\t\t"+"nick name："+nick_str+"\t\t"+"Birthday："+birthday_str+"\t\theight："+height_str+"\t\tage："+custom_age+"age");
                if (!"".equals(nick_str) && !getString(R.string.please_write_birthday).equals(birthday_str) && !getString(R.string.please_write_height).equals(height_str)){
                    //save data
                    saveMessageOne();
                    //題示個人信息1/2  隱藏個人信息2/2
                    //1. 顯示 個人信息2
                    showAnimation(personal_information_page_one, R.anim.alpha_out);
                    personal_information_page_two.setVisibility(View.VISIBLE);
                    showAnimation(personal_information_page_two, R.anim.push_left_in).setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            //1. 顯示 個人信息1
                            personal_information_page_one.setVisibility(View.GONE);
                            back = setTitleLeft(getString(R.string.personal_information_two));
                            //設顯示的圖片
                            back.setImageResource(R.mipmap.all_back_black);
                            //設返回上一頁的圖片
                            back.setOnClickListener(MainActivity.this);
                            Toast.makeText(MainActivity.this, getString(R.string.back_black_image), Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }else {
                    Toast.makeText(this,getString(R.string.welcome_not_complate), Toast.LENGTH_SHORT).show();
                }
                break;
            //======================================= 個人信息2/2 =======================================
            case R.id.left_btn://返回上一步
                personal_information_page_two.setVisibility(View.GONE);
                showAnimation(personal_information_page_two, R.anim.push_left_out);
                personal_information_page_one.setVisibility(View.VISIBLE);
                showAnimation(personal_information_page_one, R.anim.wave_scale).setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setTitle(getString(R.string.personal_information_one));
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                Toast.makeText(this,getString(R.string.go_back), Toast.LENGTH_SHORT).show();
                break;
            case R.id.walk://先去逛逛
                saveMessageTwo();
                Toast.makeText(this,getString(R.string.take_a_look), Toast.LENGTH_SHORT).show();
                FunctionActivity.take_a_look = true;
                Intent functionIntent = new Intent(this, FunctionActivity.class);
                startActivity(functionIntent);
                finish();
                break;
            case R.id.make://制定計畫
                //saveMessageTwo();
                //Toast.makeText(this,getString(R.string.set_plan), Toast.LENGTH_SHORT).show();
                //Intent PlanningIntent = new Intent(this,PlanningActivity.class);
                //startActivity(PlanningIntent);
                //finish();
                break;
            default:
                break;
        }
    }

    /**
     * save 第一part 資料
     */
    private void saveMessageOne(){
        SaveKeyValues.putStringValues("gender", gender_str);//性別
        SaveKeyValues.putStringValues("nick",nick_str);//暱稱
        SaveKeyValues.putStringValues("birthday",birthday_str);//生日日期
        SaveKeyValues.putIntValues("birth_year" ,year);
        SaveKeyValues.putIntValues("birth_month",month);
        SaveKeyValues.putIntValues("birth_day",day);
        SaveKeyValues.putStringValues("height_str",height_str);//身高帶單位的文字
        SaveKeyValues.putIntValues("height ", Integer.parseInt(height_str.substring(0,height_str.length()-2)));//身高數值
        SaveKeyValues.putIntValues("age",custom_age);//年齡
    }
    /**
     * save 第二部份數據
     */
    private void saveMessageTwo(){
        SaveKeyValues.putIntValues("weight",weight);//體重值
        SaveKeyValues.putStringValues("weight_str", weight_str);//體重信息
        SaveKeyValues.putIntValues("length", length);//步長值
        SaveKeyValues.putStringValues( "length_str", length_str);//步長信息
        SaveKeyValues.putIntValues("count",1);//用於判斷不是第一次啟動
    }
    /**
     * 日歷資料
     * @param map
     */
    @Override
    public void getMessage(Map<String, Object> map) {
        year = (int) map.get("year");
        month = (int) map.get("month");
        day = (int) map.get("day");
        custom_age = Integer.parseInt(map.get("age").toString());
        input_birthday.setText(year + getString(R.string.year) + month + getString(R.string.month) + day + getString(R.string.day));
    }

    /**
     * monitor
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.input_nick:
                if (!nextShow){
                    showNextBtn();
                }
                break;
            case R.id.input_birthday:
                openPickerOrClose(hasFocus);
                break;
            case R.id.input_height:
                openHeightPickerOrClose(hasFocus);
                break;
            default:
                break;
        }
    }

    /**
     * 顯示或隱藏日期selector
     * @param flag
     */
    private void openPickerOrClose(boolean flag){
        if (flag){
            Log.e(TAG, "拎到生日");
            hideKeyBoard();
            choose_date.setVisibility(View.VISIBLE);
            hideNextBtn();
            showAnimation(choose_date, R.anim.push_up_in);
            closeDataPicker = true;
        }else {
            if (closeDataPicker == true){
                showAnimation(choose_date, R.anim.push_up_out);
                choose_date.setVisibility(View.GONE);
                showNextBtn();
                closeDataPicker = false;
            }
        }
    }
    /**
     * 顯示或隱藏身高selector
     * @param flag
     */
    private void openHeightPickerOrClose(boolean flag){
        if (flag){
            Log.e(TAG, "拎到身高");
            hideKeyBoard();
            choose_height.setVisibility(View.VISIBLE);
            hideNextBtn();
            showAnimation(choose_height, R.anim.push_left_in);
            closeHeightPicker = true;
        }else {
            if (closeHeightPicker == true){
                showAnimation(choose_height,R.anim.push_left_out);
                choose_height.setVisibility(View.GONE);
                showNextBtn();
                closeHeightPicker = false;
            }
        }
    }
    /**
     * show next step button
     */
    private void showNextBtn(){
        if (closeDataPicker == false && closeHeightPicker== false){
            next_action.setVisibility(View.VISIBLE);
            showAnimation(next_action, R.anim.fade_in);
            nextShow = true;
        }
    }

    /**
     * hide next step button
     */
    private void hideNextBtn(){
        showAnimation(next_action, R.anim.fade_out);
        next_action.setVisibility(View.INVISIBLE);
        nextShow = false;
    }

    /**
     * animal
     * @param view
     * @param animID
     */
    private Animation showAnimation(View view, int animID){
        Animation animation = AnimationUtils.loadAnimation(this,animID);
        view.setAnimation(animation);
        animation.start();
        return animation;
    }

    /**
     * height date
     * @param text
     */
    @Override
    public void onSelect(String text) {
        input_height.setText(text + getString(R.string.cm));
    }

    /**
     * hide keybroad
     */
    private void  hideKeyBoard(){
       ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * bide back
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
