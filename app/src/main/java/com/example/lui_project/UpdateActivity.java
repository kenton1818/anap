package com.example.lui_project;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lui_project.db.DatasDao;
import com.example.lui_project.service.ExecuteHealthyPlanService;
import com.example.lui_project.utils.Constant;
import com.example.lui_project.utils.DateUtils;

import java.util.Map;


public class UpdateActivity extends AppCompatActivity implements View.OnClickListener, TimePicker.OnTimeChangedListener{


    private Button change_stop_date,dimind_change_date;
    private TextView back_to_front;
    private TimePicker change_time;
    private int hour,minute;
    private int change_year,change_month,change_day;
    private int index;
    private int id;
    private DatasDao datasDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Intent intent = getIntent();
        index = intent.getIntExtra("position",0);
        id = intent.getIntExtra("id",1);
        Map<String,Object> time = DateUtils.getDate();
        change_year = (int) time.get("year");
        change_month = (int) time.get("month");
        change_day = (int) time.get("day");
        hour = (int) time.get("hour");
        minute = (int) time.get("minute");
        datasDao = new DatasDao(this);
        //Initialize control
        change_stop_date = (Button) findViewById(R.id.plan_stop);
        dimind_change_date = (Button) findViewById(R.id.set_clock);
        back_to_front = (TextView) findViewById(R.id.to_back);
        change_time = (TimePicker) findViewById(R.id.timePicker1);
        //Set the listen event
        change_stop_date.setOnClickListener(this);
        dimind_change_date.setOnClickListener(this);
        back_to_front.setOnClickListener(this);
        change_time.setOnTimeChangedListener(this);

        dimind_change_date.setText(UpdateActivity.this.getString(R.string.minePlan_update_comfirm1)+(++index)+UpdateActivity.this.getString(R.string.minePlan_update_comfirm2));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.plan_stop://設置日期
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        change_year = year;
                        change_month = monthOfYear + 1;
                        change_day = dayOfMonth;
                        Log.d("change_year",change_year+"");
                        Log.d("change_month",change_month+"");
                        Log.d("change_day",change_day+"");
                        Log.d("want to change" , DateUtils.getMillisecondValues(year,monthOfYear,dayOfMonth)+"");
                        Log.d("now" , DateUtils.getNowDateMillisecondValues()+"");

                        if(DateUtils.getNowDateMillisecondValues() > DateUtils.getMillisecondValues(year,monthOfYear,dayOfMonth)){
                            change_stop_date.setText(UpdateActivity.this.getString(R.string.minePlan_update_error));
                            change_stop_date.setTextColor(Color.parseColor("#ff0000"));
                        }else{
                            change_stop_date.setText(change_year+"-"+change_month+"-"+change_day);
                            change_stop_date.setTextColor(Color.parseColor("#ffffff"));
                        }
                    }
                }, (int)DateUtils.getDate().get("year"),(int)DateUtils.getDate().get("month")-1,(int)DateUtils.getDate().get("day"));
                datePickerDialog.show();
                break;
            case R.id.set_clock://確頂返回，並更新數據
                ContentValues values = new ContentValues();
                values.put("stop_year",change_year );
                values.put("stop_month",change_month );
                values.put("stop_day",change_day );
                values.put("hint_time",DateUtils.getMillisecondValues(hour,minute) );
                values.put("hint_hour",hour);
                values.put("hint_minute",minute);
                values.put("hint_str", hour + ":" + minute + "");
                Cursor cursor =  datasDao.selectAll("plans");
                boolean up = true;
                while (cursor.moveToNext()){
                    int get_hour = cursor.getInt(cursor.getColumnIndex("hint_hour"));
                    int get_minute = cursor.getInt(cursor.getColumnIndex("hint_minute"));
                    Log.d("get_hour",get_hour+"");
                    Log.d("get_minute",get_minute+"");
                    if (DateUtils.getMillisecondValues(get_hour,get_minute) == DateUtils.getMillisecondValues(hour,minute)){;
                        Log.d("get_hour",get_hour+"");
                        Log.d("get_minute",get_minute+"");
                        Toast.makeText(UpdateActivity.this,UpdateActivity.this.getString(R.string.minePlan_update_error2), Toast.LENGTH_SHORT).show();
                        Log.d("update","ERROR");
                        up = false;
                        break;
                    }
                }
                if (up){
                    int result = datasDao.updateValue("plans",values,"_id=?",new String[]{String.valueOf(id)});
                    if (result > 0){
                        startService(new Intent(UpdateActivity.this, ExecuteHealthyPlanService.class).putExtra("code", Constant.CHANGE_PLAN));
                        setResult(RESULT_OK);
                        finish();
                    }
                }
                break;
            case R.id.to_back://返回:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * time control
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
//        Log.e("時",this.hour + "");
//        Log.e("分",this.minute + "");
    }
}
