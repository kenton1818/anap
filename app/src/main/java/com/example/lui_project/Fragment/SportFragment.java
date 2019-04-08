package com.example.lui_project.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lui_project.PlayActivity;
import com.example.lui_project.circlebar.CircleBar;

import com.example.lui_project.R;
import com.example.lui_project.service.StepCounterService;
import com.example.lui_project.service.GpsLocation;
import com.example.lui_project.utils.Constant;
import com.example.lui_project.utils.SaveKeyValues;
import com.example.lui_project.utils.StepDetector;

import org.json.JSONObject;

import static com.android.volley.Request.Method.GET;


public class SportFragment extends Fragment{//此處直接繼承Fragment即可
    public static final int WEATHER_MESSAGE = 1;//顯示天氣信息
    public static final int STEP_PROGRESS = 2;//顯示步數信息
    private View view;//界面的佈局
    private TextView city_name,city_temperature,city_air_quality;//展示天氣相關控件
    //顯示精度的圓形進度條
    private CircleBar circleBar;//進度條
    private TextView show_mileage,show_heat,want_steps;//顯示里程和熱量
    private ImageButton warm_btn;//跳轉按鈕
    //展示進度、里程、熱量的相關參數
    private int custom_steps;//用戶的步數
    private int custom_step_length;//用戶的步長
    private int custom_weight;//用戶的體重
    private Thread get_step_thread; // 定義線程對象
    private Intent step_service;//計步服務
    private Intent gps_service;//計步服務
    private boolean isStop;//是否運行子線程
    private Double distance_values;// 路程：米
    private int steps_values;//步數
    private Double heat_values;//熱量
    private int duration;//動畫時間
    private Context context;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){

                case WEATHER_MESSAGE:
                    setDownLoadMessageToView();

                case STEP_PROGRESS://步數跟新後會調至這裡
                    //獲取計步的步數
                    steps_values = StepDetector.CURRENT_SETP;
                    //吧步數的進度顯示在進度條上
                    circleBar.update(steps_values,duration);
                    duration = 0;
                    //存儲當前的步數
                    SaveKeyValues.putIntValues("sport_steps", steps_values);
// Log.e("執行了", ":" + steps_values);
                    //計算里程
                    distance_values = steps_values *
                            custom_step_length * 0.01 *0.001;//km
// Log.e("里程", ":" + distance_values+"km");
                    show_mileage.setText(formatDouble(distance_values) + context.getString(R.string.km));
                    //存值
                    SaveKeyValues.putStringValues("sport_distance",
                            formatDouble(distance_values));
                    //消耗熱量:跑步熱量（kcal）＝體重（kg）×距離（公里）×1.036
                    heat_values = custom_weight * distance_values * 1.036;
                    //展示信息
                    show_heat.setText(formatDouble(heat_values) + context.getString(R.string.cal));
                    //存值
                    SaveKeyValues.putStringValues("sport_heat",
                            formatDouble(heat_values));
                    break;
            }
            return false;
        }
    });

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        SaveKeyValues.createSharePreferences(context);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sport, null);
        initView();//初始化控件
        initValues();//初始化數據
        setNature();//設置功能
        //提示

        if (StepDetector.CURRENT_SETP > custom_steps){
            Toast.makeText(getContext(),SportFragment.this.getString(R.string.Sport_final_target)
                    ,Toast.LENGTH_LONG).show();
        }
        //提示彈窗
        if (SaveKeyValues.getIntValues("do_hint",0) == 1
                && (System.currentTimeMillis() > (SaveKeyValues.
                getLongValues("show_hint",0)+Constant.DAY_FOR_24_HOURS))){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(SportFragment.this.getString(R.string.Sport_Dialog_title));
            alertDialog.setMessage(SportFragment.this.getString(R.string.Sport_Dialog_Msg));
            alertDialog.setPositiveButton(SportFragment.this.getString(R.string.Sport_Dialog_Button1),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SaveKeyValues.putIntValues("do_hint" , 0);
                        }
                    });
            alertDialog.create();//創建彈窗
            alertDialog.show();//顯示彈窗
        }
        return view;
    }

    private void initValues(){


        gps_service = new Intent(getContext(),GpsLocation.class);
        getContext().startService(gps_service);
        downLoadDataFromNet();
        GpsLocation.FLAG = true ;
        //2、獲取計算里程和熱量的相關參數-->默認步數：1000、步長：70cm、體重：50kg
        isStop = false;
        duration = 800;
        //獲取默認值用於計算公里數和消耗的熱量
        custom_steps = SaveKeyValues.getIntValues("step_plan",6000);//用戶的步數
        custom_step_length = SaveKeyValues.getIntValues("length",70);//用戶的步長
        custom_weight = SaveKeyValues.getIntValues("weight", 50);//用戶的體重
// Log.e("步數", custom_steps + "步");
// Log.e("步長", custom_step_length + "厘米");
// Log.e("體重", custom_weight + "公斤");
        //開啟計步服務
        int history_values = SaveKeyValues.getIntValues("sport_steps", 0);
        Log.e("獲取存儲的值", "" + history_values);
        int service_values = StepDetector.CURRENT_SETP;
         Log.e("關閉程序後的值",service_values+"");

         StepDetector.CURRENT_SETP = history_values + service_values;
         Log.d("updata current", "Is update");


        //開啟計步服務
        step_service = new Intent(getContext(),StepCounterService.class);
        getContext().startService(step_service);
    }



    /**
     * 把下載的數值解析後賦值給相關的控件
     * @param
     */
    public void setDownLoadMessageToView() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //下載天氣預報

                String url = "https://api.openweathermap.org/data/2.5/weather?lat="+GpsLocation.Latitude+"&lon="+GpsLocation.Longitude+"&APPID=9d46b6edb580df502b7705b9d07b342c";
                Log.d("weather",url);
                JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject main_object = null;
                        JSONObject sys = null;
                        Log.d("weather","start2");
                        try {

                            main_object = response.getJSONObject("main");
                            JSONArray array = response.getJSONArray("weather");
                            JSONObject object = array.getJSONObject(0);
                            String temp = String.valueOf(main_object.getDouble("temp"));
                            String description = object.getString("description");
                            sys = response.getJSONObject("sys");
                            String city = String.valueOf(sys.getString("country"));

                            city_name.setText(context.getString(R.string.city)+city);
                            city_air_quality.setText(context.getString(R.string.quality) + description);

                            double temp_int = Double.parseDouble(temp);
                            double centi = (temp_int-32)/1.8000;
                            centi = Math.round(centi);
                            int i = (int)centi;
                            if(isAdded()) {
                                city_temperature.setText(context.getString(R.string.temperature_hint) + i + getString(R.string.temperature_unit));
                            }
                            Log.d("weather",city);
                            Log.d("weather",description);
                            Message message = Message.obtain();
                            message.what = WEATHER_MESSAGE;
                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"weather cant receive, check your gps and network and re open the app"
                                    ,Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("weather",error.getMessage());
                    }
                }
                );
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                queue.add(jor);
                Log.d("weather","error");
            }
        }).start();
    }

    private void initView() {
        circleBar = (CircleBar) view.findViewById(R.id.show_progress);
        city_name = (TextView) view.findViewById(R.id.city_name);
        city_temperature = (TextView) view.findViewById(R.id.temperature);
        city_air_quality = (TextView) view.findViewById(R.id.air_quality);
        warm_btn = (ImageButton) view.findViewById(R.id.warm_up);
        show_mileage = (TextView) view.findViewById(R.id.mileage_txt);
        show_heat = (TextView) view.findViewById(R.id.heat_txt);
        want_steps = (TextView) view.findViewById(R.id.want_steps);
    }

    private void setNature(){
        //設置初始的進度
        circleBar.setcolor(R.color.theme_blue_two); //設置進度條的顏色
        circleBar.setMaxstepnumber(custom_steps); //設置進度條的最大值
        getServiceValue();
        //跳轉界面的按鈕
        warm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, SportFragment.this.getString(R.string.Sport_warmUp_click), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), PlayActivity.class).putExtra("play_type", 0).putExtra("what",0));
// Random random = new Random（）;
// for（int i = 0; i <5; i ++）{
// int a = random.nextInt（5）;
// Log.e（“隨機數”，“【”+ a +“）”）;
//}
// startActivity（new Intent（getContext（），WarmUpActivity.class）
// .putExtra（“random”，new Random（）。nextInt（5）））;
            }
        });
        want_steps.setText("Today Target is："+custom_steps+"");

    }
    /**
      * 獲取計步服務的信息
      */
    private void getServiceValue() {
// Log.e("開啟子線程", "進入Method！");
        if (get_step_thread == null) {

            get_step_thread = new Thread() {// 子線程用於監聽當前步數的變化

                @Override
                public void run() {
                    super.run();
                    while (!isStop) {
                        try {
// Log.e("開啟子線程", "進入Method！");
                            Thread.sleep(1000);//每個一秒發送一條信息給UI線程
                            if (StepCounterService.FLAG) {
                                handler.sendEmptyMessage(STEP_PROGRESS);// 通知主線程
                            }


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            };
            get_step_thread.start();
        }

    }


    /**
     * 如果經偉度更新, 便重新下載最新天氣
     * @param
     */
    private void downLoadDataFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if (GpsLocation.FLAG == true)
                    {
                        Message message = Message.obtain();
                        message.what = WEATHER_MESSAGE;
                        handler.sendMessage(message);
                        GpsLocation.FLAG = false;
                    }
            }

            }
        }).start();
    }

    /**
      * 計算並格式化doubles數值，保留兩位有效數字
      *
      * @param doubles
      * @return 返回當前路程
      */
    private String formatDouble(Double doubles) {
        DecimalFormat format = new DecimalFormat("####.##");
        String distanceStr = format.format(doubles);
        return distanceStr.equals("0") ? "0.00" : distanceStr;
    }

    /**
      * 在當前Fragment結束之前，銷毀一些不需要的變量
      */
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(get_step_thread);
        isStop = true;
        get_step_thread = null;
        steps_values = 0;
        duration = 800;
    }
}
