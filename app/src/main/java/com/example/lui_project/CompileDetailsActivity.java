package com.example.lui_project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.lui_project.base.BaseActivity;
import com.example.lui_project.circlebar.CircleImageView;
import com.example.lui_project.utils.DateUtils;
import com.example.lui_project.utils.SaveKeyValues;

import java.util.Map;


/**
 * Change user information
 */
public class CompileDetailsActivity extends BaseActivity implements View.OnClickListener{
    //1、更換頭像
    private CircleImageView head_image;//顯示頭像
    // 2、修改暱稱
    private String nick_str;//用戶暱稱
    private EditText change_nick;//修改暱稱
    // 3、修改性別
    private RadioGroup change_gender;//更改性別
    String gender; //當前性別
    private String sex_str;//性別
    // 4、修改生日
    private TextView change_birthDay;//更改
    private String date; //生日日期
    private int birth_year;
    private int birth_month; private int birth_day;//當日日期
    private int now_year ; private int now_month; private int now_day;
    //5、修改身高
    private EditText change_height;
    private int height;
    //6、修改體重
    private EditText change_weight;
    private int weight;
    //7、修改步長
    private EditText change_length;
    private int length;
    //用戶年齡
    // 確定修改
    private Button change_OK_With_Save;
    //確定保存
    @Override
    protected void setActivityTitle() {
        initTitle();
        setTitle("更改個人信息", this);
        setMyBackGround(R.color.watm_background_gray);
        setTitleTextColor(R.color.theme_blue_two);
        setTitleLeftImage(R.mipmap.sport_back_blue);
        setResult(RESULT_OK);
    }

    @Override
    protected void getLayoutToView() {
        setContentView(R.layout.activity_compile_details);
    }

    @Override
    protected void initValues() {
        nick_str = SaveKeyValues.getStringValues("nick","未填");
        sex_str = SaveKeyValues.getStringValues("gender","M");
        Log.d("getsex", sex_str+"");
        //獲取今日日期
        getTodayDate();
        birth_year = SaveKeyValues.getIntValues("birth_year",now_year);
        birth_month = SaveKeyValues.getIntValues("birth_month",now_month);
        birth_day = SaveKeyValues.getIntValues("birth_day",now_day);
        date = birth_year+"-"+birth_month+"-"+birth_day;

        height = SaveKeyValues.getIntValues("height",0);
        weight = SaveKeyValues.getIntValues("weight",0);
        length = SaveKeyValues.getIntValues("length",0);
    }

    /**
     * 獲取當日日期
     */
    private void getTodayDate() {
        Map<String,Object> map = DateUtils.getDate();
        now_year = (int) map.get("year");
        now_month = (int) map.get("month");
        now_day = (int) map.get("day");
    }

    @Override
    protected void initViews() {

        head_image = (CircleImageView) findViewById(R.id.head_pic);
        //2、改名
        change_nick = (EditText) findViewById(R.id.change_nick);
        //3、改性别
        change_gender = (RadioGroup) findViewById(R.id.change_gender);

        //4、改生日
        change_birthDay = (TextView) findViewById(R.id.change_date);
        //確定退出
        change_OK_With_Save = (Button) findViewById(R.id.change_ok);

        //改参數
        change_height = (EditText) findViewById(R.id.change_height);
        change_weight = (EditText) findViewById(R.id.change_weight);
        change_length = (EditText) findViewById(R.id.change_length);
    }

    @Override
    protected void setViewsListener() {
        change_OK_With_Save.setOnClickListener(this);
        change_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    hideKeyBoard();
                }
                catch (Exception e)
                {
                    Log.d("keybroad","null");
                }
                switch (checkedId) {
                    case R.id.change_girl:
                        sex_str = getResources().getString(R.string.girl);
                        break;
                    case R.id.change_boy:
                        sex_str = getResources().getString(R.string.boy);;
                        break;
                    default:
                        sex_str = gender;
                        break;
                }
            }
        });
        change_birthDay.setOnClickListener(this);
    }

    @Override
    protected void setViewsFunction() {
        gender = SaveKeyValues.getStringValues("gender", "M");//獲取圖片路徑
        // 設置顯示和功能
        Log.d("sex!!!",gender+"");
        if (gender == getResources().getString(R.string.boy))
        {
            Log.d("sex!!!","set boy");
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.mipmap.welcome_boy_blue);
            head_image.setImageBitmap(bitmap);
            change_gender.check(R.id.change_boy);
        }
        else
        {
            Log.d("sex!!!","set girl");
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.mipmap.welcome_girl_blue);
            head_image.setImageBitmap(bitmap);
            change_gender.check(R.id.change_girl);
        }
        change_birthDay.setText(date);
        change_nick.setHint(nick_str);
        change_nick.setHintTextColor(getResources().getColor(R.color.btn_gray));
        change_height.setHint(String.valueOf(height));
        change_height.setHintTextColor(getResources().getColor(R.color.btn_gray));
        change_length.setHint(String.valueOf(length));
        change_length.setHintTextColor(getResources().getColor(R.color.btn_gray));
        change_weight.setHint(String.valueOf(weight));
        change_weight.setHintTextColor(getResources().getColor(R.color.btn_gray));

    }


    /**
     * click event
     * * @param v
     */
    @Override
    public void onClick(View v) {
        try {
            hideKeyBoard();
        }
        catch (Exception e)
        {
            Log.d("keybroad","null");
        }
        switch (v.getId()){

            case R.id.change_date://更改日期-->更改年龄
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birth_day = year;
                        birth_month = monthOfYear + 1;
                        birth_day = dayOfMonth;
                        date = birth_year+"-"+birth_month+"-"+birth_day;
                        change_birthDay.setText(date);
                    }
                },birth_year,birth_month - 1,birth_day);
                datePickerDialog.setTitle("請設置生日日期");
                datePickerDialog.show();
                break;

            case R.id.change_ok:
             {
                 if(!"".equals(change_nick.getText().toString())) {
                     SaveKeyValues.putStringValues("nick", change_nick.getText().toString());//保存nick name
                 }
                 SaveKeyValues.putStringValues("gender", sex_str);//保存性别
                 Log.d("sex!!!!!!!!!1change", sex_str+"");
                 SaveKeyValues.putStringValues("birthday", birth_year + "年" + birth_month + "月" + birth_day + "日");//保存生日日期
                 SaveKeyValues.putIntValues("birth_year", birth_year);
                 SaveKeyValues.putIntValues("birth_month", birth_month);
                 SaveKeyValues.putIntValues("birth_day", birth_day);
                 SaveKeyValues.putIntValues("age", now_year - birth_year);//保存age
                 if (!"".equals(change_height.getText().toString())){
                     SaveKeyValues.putIntValues("height", Integer.parseInt(change_height.getText().toString().trim()));//保存height
                 }
                 if (!"".equals(change_length.getText().toString())){
                     SaveKeyValues.putIntValues("length", Integer.parseInt(change_length.getText().toString().trim()));//保存步長
                 }
                 if (!"".equals(change_weight.getText().toString())){
                     SaveKeyValues.putIntValues("weight", Integer.parseInt(change_weight.getText().toString().trim()));//保存體重
                 }
                 Log.d("update","start");
                 startActivity(new Intent(this, PlanningActivity.class));
                 finish();
             }
            default:
                break;
        }
    }

    /**
     * hide keybroad
     */
    private void  hideKeyBoard(){
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(CompileDetailsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
