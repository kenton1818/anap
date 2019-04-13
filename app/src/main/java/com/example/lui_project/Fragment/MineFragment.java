package com.example.lui_project.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lui_project.R;
import com.example.lui_project.circlebar.CircleImageView;
import com.example.lui_project.db.DatasDao;
import com.example.lui_project.utils.SaveKeyValues;
import com.example.lui_project.FoodHotListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


public class MineFragment extends Fragment implements View.OnClickListener {
    private static final int CHANGE = 200;
    private View view;//界面的佈局
    private Context context;
    //上半部分
    private CircleImageView head_image;//顯示頭像
    private ImageButton change_values;//更改信息按鈕
    private TextView custom_name;//用戶名稱
    private TextView want;
    //中間部分
    private LineChartView lineChartView;//統計圖
    private LineChartData data;//數據集
    private float[] points = new float[7];//折線點的數組
    private DatasDao datasDao;//讀取數據工具
    private TextView show_steps;//顯示今日已走的步數

    //下部分
    private TextView food;//食物热量表
    private EditText steps;//步數
    private TextView sport_message;//運動信息
    private TextView plan_btn;//計畫

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        //1、第一部分顯示頭像、暱稱
        head_image = (CircleImageView) view.findViewById(R.id.head_pic);
        custom_name = (TextView) view.findViewById(R.id.show_name);
        change_values = (ImageButton) view.findViewById(R.id.change_person_values);
        //點擊跳轉到編輯個人信息界面
        change_values.setOnClickListener(this);
        //2、第二部分顯示當日的步數和歷史統計圖
        show_steps = (TextView) view.findViewById(R.id.show_steps);
        lineChartView = (LineChartView) view.findViewById(R.id.step_chart);
        if (isAdded()) {
            datasDao = new DatasDao(getContext());
        }
        //顯示信息
        showMessage();

        food = (TextView) view.findViewById(R.id.food_hot);
        food.setOnClickListener(this);
        want = (TextView) view.findViewById(R.id.want);
        want.setText(this.getString(R.string.Mine_want_text1) + SaveKeyValues.getStringValues("plan_stop_date",this.getString(R.string.Mine_want_text2))+this.getString(R.string.Mine_want_text3)+SaveKeyValues.getIntValues("weight",0)+this.getString(R.string.Mine_want_text4));

        sport_message = (TextView) view.findViewById(R.id.sport_btn);
        sport_message.setOnClickListener(this);
        steps = (EditText) view.findViewById(R.id.change_step);
        steps.setText(SaveKeyValues.getIntValues("step_plan" , 6000) + "");
        plan_btn = (TextView) view.findViewById(R.id.plan_btn);
        plan_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * click event
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_person_values:
                break;
            case R.id.sport_btn:
                break;
            case R.id.plan_btn:
                break;
            case R.id.food_hot:
                startActivity(new Intent(context, FoodHotListActivity.class));
                break;
            default:
                break;
        }
    }



    /**
     * 繪製折線圖
     * @param count
     */
    private void getDataValues(int count) {

        //used to the x
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);//获取日期
        //list of date
        List<PointValue> list;
        int[] dateArray = new int[7];
        dateArray[6] = day;
        if (count == 0){
            Log.e("無數據","1");
            getNestDayDate(dateArray, dateArray.length - 2);
            Log.e("date of 6 day ", Arrays.toString(dateArray));
            //x raix description
            List<AxisValue> axisValues = new ArrayList<>();
            for (int i = 0; i < points.length; i++) {
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel(dateArray[i] + "");
                axisValues.add(axisValue);
            }
            Axis axisx = new Axis();//X
            Axis axisy = new Axis();//Y
            axisx.setTextColor(Color.BLACK)
                    .setName(this.getString(R.string.Mine_draw_x))
                    .setValues(axisValues);
            axisy.setTextColor(Color.BLACK)
                    .setName(this.getString(R.string.Mine_draw_y))
                    .setHasLines(true)
                    .setMaxLabelChars(5);
            // add dot
            list = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                list.add(new PointValue(i, 0));
            }
            list.add(new PointValue(6, SaveKeyValues.getIntValues("sport_steps", 0)));
            //設置折線圖的集合
            List<Line> lines = new ArrayList<>();
            //添加折線並設置折線
            Line line = new Line(list)
                    .setColor(Color.parseColor("#4592F3"))
                    .setCubic(false)
                    .setHasPoints(false);
            line.setHasLines(true);
            line.setHasLabels(true);
            line.setHasPoints(true);
            lines.add(line);
            //顯示折線圖
            data = new LineChartData();
            data.setLines(lines);
            data.setAxisYLeft(axisy);
            data.setAxisXBottom(axisx);
        }else if(count > 0 && count < 7){//數據庫中的大於0小於6
            Log.e("有數據","7個以下");
            getNestDayDate(dateArray, dateArray.length - 2);
            Log.e("element", Arrays.toString(dateArray));
            List<AxisValue> axisValues = new ArrayList<>();
            for (int i = 0; i < points.length; i++) {
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel(dateArray[i] + "");
                axisValues.add(axisValue);
            }
            Axis axisx = new Axis();
            Axis axisy = new Axis();
            axisx.setTextColor(Color.BLACK)
                    .setName(this.getString(R.string.Mine_draw_x))
                    .setValues(axisValues);
            axisy.setTextColor(Color.BLACK)
                    .setName(this.getString(R.string.Mine_draw_y))
                    .setHasLines(true)
                    .setMaxLabelChars(5);
            //set data point
            list = new ArrayList<>();
            if (count != 6){
                for (int i = 0; i < 6 - count; i++) {
                    list.add(new PointValue(i, 0));
                }
            }
            //獲取游標用來檢索數據
            Cursor cursor = datasDao.selectAll("step");
            int i = count;
            while (cursor.moveToNext()){
                int a = cursor.getInt(cursor.getColumnIndex("steps"));
                list.add(new PointValue(6 - (i--), a));
            }
            cursor.close();
            //加入當天數據
            list.add(new PointValue(6, SaveKeyValues.getIntValues("sport_steps", 0)));
            List<Line> lines = new ArrayList<>();
            Line line = new Line(list)
                    .setColor(Color.parseColor("#4592F3"))
                    .setCubic(false)
                    .setHasPoints(false);
            line.setHasLines(true);
            line.setHasLabels(true);
            line.setHasPoints(true);
            lines.add(line);
            data = new LineChartData();
            data.setLines(lines);
            data.setAxisYLeft(axisy);
            data.setAxisXBottom(axisx);
        }else{
            Log.e("有data","7個以上");
            getNestDayDate(dateArray, dateArray.length - 2);
            Log.e("element", Arrays.toString(dateArray));
            List<AxisValue> axisValues = new ArrayList<>();
            for (int i = 0; i < points.length; i++) {
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel(dateArray[i] + "");
                axisValues.add(axisValue);
            }
            Axis axisx = new Axis();
            Axis axisy = new Axis();
            axisx.setTextColor(Color.BLACK)
                    .setName(this.getString(R.string.Mine_draw_x))
                    .setValues(axisValues);
            axisy.setTextColor(Color.BLACK)
                    .setName(this.getString(R.string.Mine_draw_y))
                    .setHasLines(true)
                    .setMaxLabelChars(5);
            list = new ArrayList<>();
            int length = count - 6 + 1;
            for (int i = length; i <= count ; i ++){
                int b = 0;
                Cursor cursor = datasDao.selectValue2("step",null,"_id=?",new String[]{String.valueOf(i)},null,null,null);
                while (cursor.moveToNext()){
                    int a = cursor.getInt(cursor.getColumnIndex("steps"));
                    list.add(new PointValue(b, a));
                }
                cursor.close();
                b++;
            }
        }
        lineChartView.setLineChartData(data);
    }

    /**
     * 獲取今天之後六天的日期
     *
     * @param dateList
     */
    private void getNestDayDate(int[] dateList, int k) {
        Calendar calendar = Calendar.getInstance();
        for (int i = k; i >= 0; i--) {
            calendar.add(Calendar.DATE, -1);
            dateList[i] = calendar.get(Calendar.DAY_OF_MONTH);
        }
    }
    /**
     * 顯示上部分和顯示上部分
     */
    public void showMessage() {
        //上
        String name = SaveKeyValues.getStringValues("nick", "未填");//獲取名稱
        String image_path = SaveKeyValues.getStringValues("path", "path");//獲取圖片路徑
        // 設置顯示和功能
        custom_name.setText(name);

        Bitmap bitmap = BitmapFactory.decodeFile(image_path);
        head_image.setImageBitmap(bitmap);

        //中
        int today_steps = SaveKeyValues.getIntValues("sport_steps", 0);
        show_steps.setText(today_steps+"");
         //設置圖表
        // 獲取保存的數據
        Cursor cursor = datasDao.selectAll("step");
        int counts = cursor.getCount();
        getDataValues(counts);
    }


    /**
     * 返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE && resultCode == Activity.RESULT_OK) {
            showMessage();
            Log.e("返回", "success");
        }
    }
}
