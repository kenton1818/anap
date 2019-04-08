package com.example.lui_project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Map;


import com.example.lui_project.db.DatasDao;
import com.example.lui_project.utils.Constant;
import com.example.lui_project.utils.DateUtils;

/**
 * 加運動計畫介面
 */
public class SettingHealthyHealthyActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener, View.OnClickListener {

    private TextView back;
    private TextView title;//運動類型
    private int type;
    private String title_name;
    private TimePicker timePicker;//set time
    private int alarmhour;// 提示時
    private int alarmminute;//提示分
    private DatePickerDialog datePickerDialog;
    private Button start, stop;
    private int index;//區分開始和結束
    private int start_year,start_month,start_day,stop_year,stop_month,stop_day;
    private Button finish_setting;
    //存數據
    private DatasDao datasDao;
    private boolean isToSave;

    public static String[] warm_up_exercise = new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_healthy_healthy);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);// set布局填充activity界面

        datasDao = new DatasDao(this);
        Intent intent = getIntent();

        //返回
        back = (TextView) findViewById(R.id.to_back);
        back.setOnClickListener(this);
        //類型
        title = (TextView) findViewById(R.id.plan_type);
        type = intent.getIntExtra("type", 0);
        warm_up_exercise[0]=this.getString(R.string.warm_up_exercise0);
        warm_up_exercise[1]=this.getString(R.string.warm_up_exercise1);
        warm_up_exercise[2]=this.getString(R.string.warm_up_exercise2);
        warm_up_exercise[3]=this.getString(R.string.warm_up_exercise3);
        warm_up_exercise[4]=this.getString(R.string.warm_up_exercise4);
        title_name = warm_up_exercise[type];
        title.setText(title_name);
        //時間
        timePicker = (TimePicker) findViewById(R.id.timePicker1);
        timePicker.setOnTimeChangedListener(this);
        //日期
        Map<String, Object> timeMap = DateUtils.getDate();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                
                switch (index){
                    case 0://start
                        start_year = year;
                        start_month = monthOfYear + 1;
                        start_day = dayOfMonth;
                        start.setText(SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_start_point)+start_year + "-" + start_month + "-" + start_day);
                        break;
                    case 1://stop
                        stop_year = year;
                        stop_month = monthOfYear + 1;
                        stop_day = dayOfMonth;
                        stop.setText(SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_end_point)+stop_year + "-" + stop_month + "-" + stop_day);
                        break;
                    default:
                        break;

                }
            }
        }, (Integer) timeMap.get("year"), (Integer) timeMap.get("month") - 1, (Integer) timeMap.get("day"));
        start = (Button) findViewById(R.id.plan_start);
        stop = (Button) findViewById(R.id.plan_stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        //完成
        finish_setting = (Button) findViewById(R.id.set_clock);
        finish_setting.setOnClickListener(this);
    }

    /**
     * 點擊時間
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_back://返回
                finish();
                break;
            case R.id.plan_start://set開始日期
                index = 0;
                datePickerDialog.setTitle(SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_dialogstart_title));
                datePickerDialog.show();
                break;
            case R.id.plan_stop://set結束時間
                index = 1;
                datePickerDialog.setTitle(SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_dialogend_title));
                datePickerDialog.show();
                break;
            case R.id.set_clock://set 完成
                Log.e("分割","====================");
//                Log.e("分割","");
                int isAdd_24_hours = 0;
                long nowTime = DateUtils.getMillisecondValues((int) DateUtils.getDate().get("hour"), (int) DateUtils.getDate().get("minute"));
                //對要save的數值先做處理
                long wantSaveTime = DateUtils.getMillisecondValues(alarmhour,alarmminute);
                int selectID = 0;
//                Log.e("增加前", wantSaveTime + "");
                if (wantSaveTime <= nowTime ){
                    wantSaveTime += Constant.DAY_FOR_24_HOURS;
                    isAdd_24_hours = 1;
//                    Log.e("有無執行？","加上了24個小時");
                }
                //無set 結束時間
                if ( start_year != 0 && stop_year == 0){
                    stop_year = start_year;
                    stop_month = start_month;
                    stop_day = start_day;
                }
                //無set開始時間
                if( start_year == 0 && stop_year != 0){
                    start_year = (int) DateUtils.getDate().get("year");
                    start_month = (int) DateUtils.getDate().get("month");
                    start_day = (int) DateUtils.getDate().get("day");
                }
                //無實質計畫時間
                if(start_year == 0 && stop_year == 0){
                    start_year = (int) DateUtils.getDate().get("year");
                    start_month = (int) DateUtils.getDate().get("month");
                    start_day = (int) DateUtils.getDate().get("day");
                    stop_year = start_year;
                    stop_month = start_month;
                    stop_day = start_day;
                }
                //set的時間不規範
                if (DateUtils.getMillisecondValues(start_year,start_month,start_day) > DateUtils.getMillisecondValues(stop_year,stop_month,stop_day)){
                    Toast.makeText(this,SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_dialog_error1), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DateUtils.getMillisecondValues((Integer) DateUtils.getDate().get("year"),(int) DateUtils.getDate().get("month"),(int) DateUtils.getDate().get("day")) > DateUtils.getMillisecondValues(stop_year,stop_month,stop_day)){
                    Toast.makeText(this,SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_dialog_error2), Toast.LENGTH_SHORT).show();
                    return;
                }
                final ContentValues values = new ContentValues();
                // 1。存入運動類型
                values.put("sport_type" , type);//1
                // 2。存入運動類型名稱
                values.put("sport_name" , title_name);//2
                //3。存入開始年月日
                values.put("start_year" , start_year);//3
                values.put("start_month" , start_month);//4
                values.put("start_day" , start_day);//5
                // 4。存入結束年月日
                values.put("stop_year" , stop_year);//6
                values.put("stop_month" , stop_month);//7
                values.put("stop_day" , stop_day);//8
                //5。存入設置的時間
                values.put("set_time", nowTime);//9
                //6。存入提醒時間
                if (alarmminute == 0 && alarmhour == 0){
                    alarmhour = (int) DateUtils.getDate().get("hour");
                    alarmminute = (int) DateUtils.getDate().get("minute");
                }
                values.put("hint_time" , wantSaveTime);//10
//                Log.e（“想要設置的時間”，wantSaveTime +“”）;
                values.put("hint_str" , alarmhour + "点" + alarmminute + "分");
                values.put("hint_hour",alarmhour);
                values.put("hint_minute",alarmminute);
                //7。存入順序
                values.put("number_values" , 0);
                values.put("add_24_hour",isAdd_24_hours);
                //插入前先做個判斷如果想要設置的提醒時間已存在則提示用戶是否覆蓋之前的設置時間
                Cursor cursor = datasDao.selectColumn("plans", new String[]{"_id", "hint_time"});
                //如果游標的值大於0，則說明數據庫的計劃表中一有數據存入到當中
                if (cursor.getCount() != 0){

                    //查詢數據是否有這個值已被存入在數據表中
                    while (cursor.moveToNext()){

                        int id = cursor.getInt(cursor.getColumnIndex("_id"));
                        long alarmTime = cursor.getLong(cursor.getColumnIndex("hint_time"));
//                        Log.e（“查詢出的ID”，id +“”）;
//// Log.e（“查詢的時間”，alarmTime +“”）;
//// Log.e（“比較”，wantSaveTime +“<--->”+ alarmTime）;
                        if (wantSaveTime == alarmTime){//兩個時間相等說明改時間點已被佔用
// Log.e（“是否進入判斷”，（wantSaveTime  -  alarmTime）+“是”）;
                            selectID = id;
                            break;
                        }else{
//                           Log.e（“是否進入判斷”，（wantSaveTime  -  alarmTime）+“no”）;
                        }
                    }

//                  Log.e（“執行完循環體”，“是”）;
//                    cursor.close（）;
//// Log.e（“查詢出的相同數據ID”，selectID +“”）;
                    if (selectID != 0){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setTitle(SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_dialog_settingError_Title));
                        dialog.setMessage(SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_dialog_settingError_Message));
                        final int finalSelectID = selectID;
                        dialog.setPositiveButton(SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_dialog_EnterButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                datasDao.deleteValue("plans","_id=?",new String[]{String.valueOf(finalSelectID)});
                                isToSave = true;
                                insertData(values);
                            }
                        });
                        dialog.setNegativeButton(SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_dialog_CancelButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isToSave = false;
                                Toast.makeText(SettingHealthyHealthyActivity.this, SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_dialog_CancelMsg), Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.create();
                        dialog.show();
                    }else{
                        isToSave = true;
                        insertData(values);
                    }
                }else {
                    isToSave = true;
                    insertData(values);
                }

                break;
            default:
                break;
        }
    }

    private void insertData(ContentValues values){
        if (isToSave){
           // 向數據庫中插入數據
            long result = datasDao.insertValue("plans",values);
            if (result > 0){
                Toast.makeText(this,SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_settingSuccess_Msg), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }else {
                Toast.makeText(this,SettingHealthyHealthyActivity.this.getString(R.string.Setting_plan_settingFalse_Msg), Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 改時間
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//        Log.e("hourOfDay",hourOfDay + "時");
//        Log.e("minute", minute + " 分");
        alarmhour = hourOfDay;
        alarmminute = minute;
    }
}
