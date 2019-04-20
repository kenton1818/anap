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
import android.widget.Toast;

import com.example.lui_project.CompileDetailsActivity;
import com.example.lui_project.MinePlanActivity;
import com.example.lui_project.R;
import com.example.lui_project.circlebar.CircleImageView;
import com.example.lui_project.db.DatasDao;
import com.example.lui_project.utils.SaveKeyValues;
import com.example.lui_project.FoodHotListActivity;
import com.example.lui_project.SportMessageActivity;
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
    private View view;//layout
    private Context context;
    //upper half
    private CircleImageView head_image;//icon
    private ImageButton change_values;//Change info button
    private TextView custom_name;//use name
    private TextView want;
    //Middle part
    private LineChartView lineChartView;//summary graph
    private LineChartData data;//data set
    private float[] points = new float[7];//Array of polyline points
    private DatasDao datasDao;//Read data tool
    private TextView show_steps;//Show the number of steps taken today

    //the next part
    private TextView food;//Food energy meter
    private EditText steps;//step
    private TextView sport_message;//Motion information
    private TextView plan_btn;//plan

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        //1, the first part shows the avatar, nickname
        head_image = (CircleImageView) view.findViewById(R.id.head_pic);
        custom_name = (TextView) view.findViewById(R.id.show_name);
        change_values = (ImageButton) view.findViewById(R.id.change_person_values);
        //Click to jump to the edit personal information interface
        change_values.setOnClickListener(this);
        //2、The second part shows the number of steps and historical charts of the day.
        show_steps = (TextView) view.findViewById(R.id.show_steps);
        lineChartView = (LineChartView) view.findViewById(R.id.step_chart);
        if (isAdded()) {
            datasDao = new DatasDao(getContext());
        }
        //show message
        showMessage();

        food = (TextView) view.findViewById(R.id.food_hot);
        food.setOnClickListener(this);
        want = (TextView) view.findViewById(R.id.want);
        want.setText(this.getString(R.string.Mine_want_text1) + SaveKeyValues.getStringValues("plan_stop_date",this.getString(R.string.Mine_want_text2))+this.getString(R.string.Mine_want_text3)+SaveKeyValues.getIntValues("plan_want_weight_values",0)+this.getString(R.string.Mine_want_text4));

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
        if (!"".equals(steps.getText().toString())){
            SaveKeyValues.putIntValues("step_plan",Integer.parseInt(steps.getText().toString()));
        }else {
            SaveKeyValues.putIntValues("step_plan",6000);
        }
    }

    /**
     * click event
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_person_values:
                startActivityForResult(new Intent(context, CompileDetailsActivity.class), CHANGE);
                Toast.makeText(context, getString(R.string.Modify_information), Toast.LENGTH_SHORT).show();
                break;
            case R.id.sport_btn:
                startActivity(new Intent(context, SportMessageActivity.class));
                break;
            case R.id.plan_btn:
                startActivity(new Intent(context, MinePlanActivity.class));
                break;
            case R.id.food_hot:
                startActivity(new Intent(context, FoodHotListActivity.class));
                break;
            default:
                break;
        }
    }



    /**
     * Draw a line chart
     * @param count
     */
    private void getDataValues(int count) {

        //used to the x
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("current day", day+"");
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
            //Set up a collection of line charts
            List<Line> lines = new ArrayList<>();
            //Add a polyline and set a polyline
            Line line = new Line(list)
                    .setColor(Color.parseColor("#4592F3"))
                    .setCubic(false)
                    .setHasPoints(false);
            line.setHasLines(true);
            line.setHasLabels(true);
            line.setHasPoints(true);
            lines.add(line);
            //Display line chart
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
            //Get cursor to retrieve data
            Cursor cursor = datasDao.selectAll("step");
            int i = count;
            while (cursor.moveToNext()){
                int a = cursor.getInt(cursor.getColumnIndex("steps"));
                list.add(new PointValue(6 - (i--), a));
            }
            cursor.close();
            //Join current data
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
     * Get the date six days after today
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
     * Show upper part and display upper part
     */
    public void showMessage() {
        //top side
        String name = SaveKeyValues.getStringValues("nick", "未填");//Get the name
        String gender = SaveKeyValues.getStringValues("gender", "M");//Get image path
        // Set display and function
        Log.d("sex!!!",gender+"");
        custom_name.setText(name);
        if (gender == getResources().getString(R.string.boy))
        {
            Log.d("sex!!!","set boy");
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.mipmap.welcome_boy_blue);
            head_image.setImageBitmap(bitmap);
        }
        else
        {
            Log.d("sex!!!","set girl");
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.mipmap.welcome_girl_blue);
            head_image.setImageBitmap(bitmap);
        }

        //mid
        int today_steps = SaveKeyValues.getIntValues("sport_steps", 0);
        show_steps.setText(today_steps+"");
         //Setting up the chart
        // Get saved data
        Cursor cursor = datasDao.selectAll("step");
        int counts = cursor.getCount();
        getDataValues(counts);
    }


    /**
     * return
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
