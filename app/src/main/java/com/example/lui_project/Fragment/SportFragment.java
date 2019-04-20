package com.example.lui_project.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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


public class SportFragment extends Fragment{//
    public static final int WEATHER_MESSAGE = 1;//Show weather information
    public static final int STEP_PROGRESS = 2;//Display step information
    private View view;////latout
    private TextView city_name,city_temperature,city_air_quality;//Show weather related controls
    //Circular progress bar showing accuracy
    private CircleBar circleBar;//progress bar of step target
    private TextView show_mileage,show_heat,want_steps;//Show mileage and heat
    private ImageButton warm_btn;//warm up
    //Show relevant parameters of schedule, mileage and heat
    private int custom_steps;//User's steps
    private int custom_step_length;//User step size
    private int custom_weight;//User's weight
    private Thread get_step_thread; // Defining thread objects
    private Intent step_service;//Step counter service
    private Intent gps_service;//gps service
    private boolean isStop;//Whether to run child threads
    private Double distance_values;// Distance: meter
    private int steps_values;//step
    private Double heat_values;//energy
    private int duration;//Animation
    private Context context;
    public String weather_city;
    public int weather_tmp;
    public String weather_description;
    public double temp_int;
    public double centi;
    public String description;
    public int i;
    public String city;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){

                case WEATHER_MESSAGE:
                    setDownLoadMessageToView();
                    if(isAdded()) {
                        city_temperature.setText(context.getString(R.string.temperature_hint) + SaveKeyValues.getIntValues("weather_tmp",0) + getString(R.string.temperature_unit));
                        city_name.setText(context.getString(R.string.city) + SaveKeyValues.getStringValues("weather_city","loading..."));
                        city_air_quality.setText(context.getString(R.string.quality) + SaveKeyValues.getStringValues("weather_description","loading"));
                    }
                case STEP_PROGRESS://The number of steps will be transferred here after the new one.
                    //Get the number of steps in the step
                    steps_values = StepDetector.CURRENT_SETP;
                    //put The progress of the steps is displayed on the progress bar.
                    circleBar.update(steps_values,duration);
                    duration = 0;
                    //Store the current number of steps
                    SaveKeyValues.putIntValues("sport_steps", steps_values);
// Log.e("執行了", ":" + steps_values);
                    //Calculate mileage
                    distance_values = steps_values *
                            custom_step_length * 0.01 *0.001;//km
// Log.e("里程", ":" + distance_values+"km");
                    show_mileage.setText(formatDouble(distance_values) + context.getString(R.string.km));
                    //存值
                    SaveKeyValues.putStringValues("sport_distance",
                            formatDouble(distance_values));
                    //Calories burned: running calories (kcal) = weight (kg) × distance (km) × 1.036
                    heat_values = custom_weight * distance_values * 1.036;
                    //Display information
                    show_heat.setText(formatDouble(heat_values) + context.getString(R.string.cal));
                    //save
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



    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sport, null);
        initView();//Initialize control
        initValues();//Initialize data
        setNature();//set function
        //tips

        if (StepDetector.CURRENT_SETP > custom_steps){
            Toast.makeText(getContext(),SportFragment.this.getString(R.string.Sport_final_target)
                    ,Toast.LENGTH_LONG).show();
        }
        //Tip popup
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
            alertDialog.create();//Create popup
            alertDialog.show();//Display popup
        }
        return view;
    }

    public void initValues(){


        gps_service = new Intent(getContext(),GpsLocation.class);
        getContext().startService(gps_service);
        downLoadDataFromNet();
        //2、Get the relevant parameters for calculating mileage and heat --> Default steps: 1000, step size: 70cm, weight: 50kg
        isStop = false;
        duration = 800;
        //Get default values for calculating kilometers and calories burned
        custom_steps = SaveKeyValues.getIntValues("step_plan",6000);//用戶的步數
        custom_step_length = SaveKeyValues.getIntValues("length",70);//用戶的步長
        custom_weight = SaveKeyValues.getIntValues("weight", 50);//用戶的體重
// Log.e("步數", custom_steps + "步");
// Log.e("步長", custom_step_length + "厘米");
// Log.e("體重", custom_weight + "公斤");
        //Turn on the step counter service
        int history_values = SaveKeyValues.getIntValues("sport_steps", 0);
        Log.e("獲取存儲的值1", "" + history_values);
        int service_values = StepDetector.CURRENT_SETP;
        Log.e("關閉程序後的值1",service_values+"");
        boolean isLaunch = getArguments().getBoolean("is_launch",false);
        if (isLaunch){
            StepDetector.CURRENT_SETP = history_values ;
            Log.e("isLaunch","isLaunch");
        }

        weather_city = SaveKeyValues.getStringValues("weather_city","Loading...");
        weather_tmp = SaveKeyValues.getIntValues("weather_tmp",000);
        weather_description = SaveKeyValues.getStringValues("weather_description","Loading...");
        city_name.setText(context.getString(R.string.city)+weather_city);
        city_air_quality.setText(context.getString(R.string.quality) + weather_description);
        city_temperature.setText(context.getString(R.string.temperature_hint) + weather_tmp + getString(R.string.temperature_unit));
        Log.d("updata current", "Is update");


        //Turn on the step counter service
        step_service = new Intent(getContext(),StepCounterService.class);
        getContext().startService(step_service);
    }



    /**
     * Parse the downloaded value and assign it to the relevant control
     * @param
     */
    public void setDownLoadMessageToView() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //下載天氣預報
                double lat = GpsLocation.Latitude;
                double longti = GpsLocation.Latitude;

                String url = "https://api.openweathermap.org/data/2.5/weather?lat="+GpsLocation.Latitude+"&lon="+GpsLocation.Longitude+"&APPID=9d46b6edb580df502b7705b9d07b342c";
                //Log.d("weather",url);
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

                            double temp_int = Double.parseDouble(temp);
                            double centi = temp_int-273;
                            centi = Math.round(centi);
                            int i = (int)centi;

                            Log.d("weather",city);
                            Log.d("weather",description);
                            Message message = Message.obtain();
                            message.what = WEATHER_MESSAGE;
                            SaveKeyValues.putStringValues("weather_city",city);
                            SaveKeyValues.putIntValues("weather_tmp",i);
                            SaveKeyValues.putStringValues("weather_description",description);
                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"weather cant receive, check your gps and network and re open the app"
                                    ,Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("weather",error.getMessage()+"");
                    }
                }
                );
                if(isAdded()) {
                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    queue.add(jor);
                }
                Log.d("weather","error");
            }
        }).start();
    }
    public void initView() {
        circleBar = (CircleBar) view.findViewById(R.id.show_progress);
        city_name = (TextView) view.findViewById(R.id.city_name);
        city_temperature = (TextView) view.findViewById(R.id.temperature);
        city_air_quality = (TextView) view.findViewById(R.id.air_quality);
        warm_btn = (ImageButton) view.findViewById(R.id.warm_up);
        show_mileage = (TextView) view.findViewById(R.id.mileage_txt);
        show_heat = (TextView) view.findViewById(R.id.heat_txt);
        want_steps = (TextView) view.findViewById(R.id.want_steps);
    }

    public void setNature(){
        //Set initial progress
        circleBar.setcolor(R.color.theme_blue_two); //Set the color of the progress bar
        circleBar.setMaxstepnumber(custom_steps); //Set the maximum value of the progress bar
        getServiceValue();
        //Jump interface button
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
        want_steps.setText(context.getString(R.string.Today_target)+"："+custom_steps+"");

    }
    /**
      * Get information about the step-by-step service
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
                            Thread.sleep(1000);//Send a message to the UI thread every second
                            if (StepCounterService.FLAG) {
                                handler.sendEmptyMessage(STEP_PROGRESS);// Notify the main thread
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
     * If the latitude and longitude is updated, re-download the latest weather
     * @param
     */
    private void downLoadDataFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if (GpsLocation.FLAG==true)
                    {
                        Log.d("gps", "update");
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
      * Calculate and format the doubles value, retaining two significant digits
      *
      * @param doubles
      * @return Return current route
      */
    private String formatDouble(Double doubles) {
        DecimalFormat format = new DecimalFormat("####.##");
        String distanceStr = format.format(doubles);
        return distanceStr.equals("0") ? "0.00" : distanceStr;
    }

    /**
      *
     Destroy some unneeded variables before the current Fragment ends
      */
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(get_step_thread);
        isStop = true;
        get_step_thread = null;
        steps_values = 0;
        duration = 800;
        Log.d("close activity", "Close");
    }
    @Override
    public void onPause() {
        super.onPause();

    }
}
