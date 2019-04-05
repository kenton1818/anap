package com.example.lui_project.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import com.example.lui_project.db.DatasDao;
import com.example.lui_project.entity.HealthyPlan;
import com.example.lui_project.receiver.FunctionBroadcastReceiver;
import com.example.lui_project.utils.Constant;
import com.example.lui_project.utils.DateUtils;
import com.example.lui_project.utils.SaveKeyValues;

/**
   * Services for implementing sports programs
   */
public class ExecuteHealthyPlanService extends Service {
    public static final String planSaveService = "PLAN";
    //Used to operate the database
    private DatasDao datasDao;
    private Intent toBroadReciver;
    private AlarmManager manager;
    private PendingIntent senser;
    //data
    private List<HealthyPlan> plansList;//planned collection
    //The first data prompt time | id | serial number | prompt type
    private long first_hint_time;
    private int first_hint_id;
    private int first_hint_num;
    private int first_hint_type;


    private int finish_plans;//complete the plan
    public ExecuteHealthyPlanService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        datasDao = new DatasDao(this);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        SaveKeyValues.createSharePreferences(this);
        finish_plans = SaveKeyValues.getIntValues("finish_plan" , 0);
        Log.d("createing","i am creating");
//        toBroadReciver = new Intent(this, FunctionBroadcastReceiver.class);
//        senser = PendingIntent.getBroadcast(this, 0, toBroadReciver, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            int code = intent.getIntExtra("code", 0);
            switch (code){
                case Constant.START_PLAN://Open the task
                    //First set the first scheduled task-->Applicable to only one piece of data in the database
                    Log.e("Afteropeningtheservice","Set the first timing service!");
                    setTaskAtOnlyOneDataInDataList();
                    break;
                case Constant.CHANGE_PLAN://Update execution order
                    //Execute this method to arrange the data when the data is greater than 1.，Then set up the timing service
                    Log.e("Update data","Set the order of execution");
                    Cursor cursor = datasDao.selectAll("plans");
                    int count = cursor.getCount();
                    cursor.close();
                    if (count == 0){
                        sendBroadcast(new Intent(this, FunctionBroadcastReceiver.class).setAction("PLAN").putExtra("mode", 3));
                    }else {
//                        compareAllData();
                        if (count > 1){
                            getHealthyDataAndSortDataToSetAlarm();//Sort task and then set
                        }else {
                            setTaskAtOnlyOneDataInDataList();//Set up a single task
                        }
                    }
                    cursor.close();
                    break;
                case Constant.NEXT_PLAN://Perform the next task
                    Log.e("complate task","Set the next timed operation");
                    int started_id = intent.getIntExtra("started_id",0);//Executed ID
                    int started_num = intent.getIntExtra("started_num",0);//Executed serial number
                    //Set the next scheduled task-->Determine if the next scheduled task can be started
                    setToExecuteNextAlarmTask(started_id , started_num);
                    break;
                case Constant.ONE_PLAN://Perform a task
                    setCirculationTaskAtOnlyOneDataInDataList();
                    break;
                case Constant.STOP_PLAN://Close service
                    break;
                default:
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    //================================ 设置下一个定时任务 ================================
    /**
     * 設置下一個定時任務
     * @param oldID
     * @param oldNum
     */
    private void setToExecuteNextAlarmTask(int oldID,int oldNum) {
        Log.e("Just completed","【"+ oldID +"】");
        Log.e("Just completed ","【"+ oldNum +"】");
        //1, first determine whether the data of the executed task is still valid --> determine the end date of the task
        long nowDate = DateUtils.getNowDateMillisecondValues();//current date
        compareVerificationData(nowDate, oldID);//Verify the data that was just played
        //2、Determine if it is the last mission plan
        Cursor cursor = datasDao.selectAll("plans");//Full query
        if (cursor.getCount() == 1){//If there is only one task left at this time
            //Then set up a single mission plan
            setTaskAtOnlyOneDataInDataList();
            return;
        }
        if (oldNum == cursor.getCount()){//At this point, the last task is executed.
            //Reorder and set up tasks
            getHealthyDataAndSortDataToSetAlarm();
            return;
        }
        //3、Set the timing plan based on the serial number to find the corresponding data.
        int nextNum = oldNum + 1;//Sequence number of the next task
        long hintTime;
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));//id
            int hour = cursor.getInt(cursor.getColumnIndex("hint_hour"));//Reminder time --> hour
            int minute = cursor.getInt(cursor.getColumnIndex("hint_minute"));//Reminder time --> points
            int number = cursor.getInt(cursor.getColumnIndex("number_values"));//order
            int hint_type = cursor.getInt(cursor.getColumnIndex("sport_type"));//type
            if (number == nextNum){//Query the result
                hintTime = DateUtils.getMillisecondValues(hour,minute);
                setAlarmService(2, id, number, hint_type, hintTime); //set the timing task
                break;
            }
        }
    }

    /**
     * Check data
     * @param nowDate
     */
    private void compareVerificationData(long nowDate ,int oldID){
        Cursor cursor = datasDao.selectValue2("plans",null,"_id=?",new String[]{String.valueOf(oldID)},null,null,null);
        while (cursor.moveToNext()){
            int stop_year = cursor.getInt(cursor.getColumnIndex("stop_year"));//end date--->year
            int stop_month = cursor.getInt(cursor.getColumnIndex("stop_month"));//End Date--->Month
            int stop_day = cursor.getInt(cursor.getColumnIndex("stop_day"));//End Date--->Day
            Long stopDate = DateUtils.getMillisecondValues(stop_year, stop_month, stop_day);
            if (stop_year == (int)(DateUtils.getDate().get("year"))
                   && stop_month == (int)(DateUtils.getDate().get("month"))
                    && stop_day == (int)(DateUtils.getDate().get("day"))){//This means that the current data will no longer be saved in the data --> delete the data
                finish_plans = SaveKeyValues.getIntValues("finish_plan" , 0);
                SaveKeyValues.putIntValues("finish_plan",++finish_plans);
                datasDao.deleteValue("plans", "_id=?", new String[]{String.valueOf(oldID)});
            }
        }
        cursor.close();
    }
    /**
           * Verify all data
           */
    private void compareAllData(){
// long nowDate = DateUtils.getNowDateMillisecondValues();//current date
// Map<String,Object> times = DateUtils.getDate();
        Cursor cursor = datasDao.selectAll("plans");
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int stop_year = cursor.getInt(cursor.getColumnIndex("stop_year"));//end date--->year
            int stop_month = cursor.getInt(cursor.getColumnIndex("stop_month"));//End Date--->Month
            int stop_day = cursor.getInt(cursor.getColumnIndex("stop_day"));//End Date--->Day
            Log.e("end date",stop_year +"-"+stop_month+"-"+stop_day );
//            long stopDate = DateUtils.getMillisecondValues(stop_year, stop_month, stop_day);

            Log.e("==","**************************" );

            if (stop_year == (int)(DateUtils.getDate().get("year"))
                    && stop_month == (int)(DateUtils.getDate().get("month"))
                    && stop_day == (int)(DateUtils.getDate().get("day"))){//This means that the current data will no longer be saved in the data --> delete the data
                Log.e("=="," executed");
                finish_plans = SaveKeyValues.getIntValues("finish_plan" , 0);
                SaveKeyValues.putIntValues("finish_plan",++finish_plans);
                datasDao.deleteValue("plans", "_id=?", new String[]{String.valueOf(id)});
                break;
            }
        }
        cursor.close();
    }//================================================================================================= ==========================
    /**
           * Arrange the data --> At this time there are at least two data
           */
    private void getHealthyDataAndSortDataToSetAlarm() {
        Log.e ("Set Timing", "Sort Tasks");
        plansList = new ArrayList<>();
        // First query data
        Cursor cursor = datasDao.selectColumn("plans",new String[]{"_id","hint_hour","hint_minute"});
        int counts = cursor.getCount();//number of data
        Log.e ("number of tasks", counts + "one");
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int hour = cursor.getInt(cursor.getColumnIndex("hint_hour"));
            int minute = cursor.getInt(cursor.getColumnIndex("hint_minute"));
            HealthyPlan plan = new HealthyPlan();
            plan.setId_Num(id);
            plan.setPlan_Time(DateUtils.getMillisecondValues(hour, minute));
            plansList.add(plan);
        }
        cursor.close();//Close cursor
        Log.e ("number of elements", plansList.size() + "pieces");
// Log.e ("Before the elements in the collection are arranged"," ====================");
// for (HealthyPlan plan : plansList){
// Log.e("Element","_id = " + plan.getId_Num() + "\t\t" + "Prompt time milliseconds = " + plan.getPlan_Time());
// }
        if (plansList.size() == counts){
            //Ascending the collection in ascending order
            Collections.sort(plansList, new Comparator<HealthyPlan>() {
                @Override
                public int compare(HealthyPlan lhs, HealthyPlan rhs) {
                    Long timeL = lhs.getPlan_Time();
                    Long timeR = rhs.getPlan_Time();
                    return timeL.compareTo(timeR);
                }
            });
            Log.e("after arranged", "====================");
//            for (HealthyPlan plan : plansList){
//              Log.e("Element","_id = " + plan.getId_Num() + "\t\t" + "Prompt time milliseconds = " + plan.getPlan_Time());
//            }
            Log.e("after arranged", "Set the order value for the data in the data table");

            //Set according to the data in the list of the order data table
            for (int i = 0; i < counts; i++ ){
                setNumberValuerToDataValues(plansList.get(i).getId_Num() ,(i + 1));
            }
            //verification
            Log.e ("after arranged", "Verify");
            functionalVerification();
            // Set the timing service after the arrangement --> Set the timing service according to the sort value --> from the first one
            Long result =accordanceNumberSetAlarmTask(1);
            Log.e ("timed return value", "["+result+"]");
        }
    }

    /**
     * Set up timing tasks according to the sort order
     * @param index
     */
    private long accordanceNumberSetAlarmTask(int index) {
        long nowTime = DateUtils.getNowMillisecondValues();//current time
        long nowDate = DateUtils.getNowDateMillisecondValues();//current date
        return getvirtualValueIndex(index ,nowTime ,nowDate);
    }
    private long getvirtualValueIndex(int index , long time , long date){
        long hintTime;//Prompt time
        Long stopDate; //prompt date
        Cursor cursor = datasDao.selectAll("plans");
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));//id
            int hour = cursor.getInt(cursor.getColumnIndex("hint_hour"));//Reminder time --> hour
            int minute = cursor.getInt(cursor.getColumnIndex("hint_minute"));//Reminder time --> points
            int number = cursor.getInt(cursor.getColumnIndex("number_values"));//order
            int hint_type = cursor.getInt(cursor.getColumnIndex("sport_type"));//type
            int stop_year = cursor.getInt(cursor.getColumnIndex("stop_year"));//end date--->year
            int stop_month = cursor.getInt(cursor.getColumnIndex("stop_month"));//End Date--->Month
            int stop_day = cursor.getInt(cursor.getColumnIndex("stop_day"));//End Date--->Day
            if (index == number){
                // Determine whether the current data is valid
                hintTime = DateUtils.getMillisecondValues(hour,minute);
                stopDate = DateUtils.getMillisecondValues(stop_year,stop_month,stop_day);
                //1, judging whether the plan is valid according to the date, if it is valid, judging whether the time is valid
                // If valid: then determine whether the time is valid --> time is set to set the time service | time exceeds the current time to execute the next cycle
                // If invalid: delete this data to execute the next cycle
                if(date <= stopDate) {//If the current date is greater than the set stop date, the judgment time is valid.
                if (time >= hintTime) {//If the current time is greater than the prompt time, execute the next loop
                    // Find data larger than the current index one
                    if (index == 1) {//record the relevant data with index one
                        first_hint_time = hintTime;
                        first_hint_id = id;
                        first_hint_num = number;
                        first_hint_type = hint_type;
                        Log.e("number 1 is _ID", "[" + first_hint_id + "]");
                        Log.e("Serial number 1", "[" + first_hint_num + "]");
                        Log.e("time number 1", "[" + first_hint_time + "]");
                        Log.e("Type number 1", "[" + first_hint_type + "]");
                    }
                        if (index == cursor.getCount()) {//If the prompt time to the last data is less than the current time, set the data with index 1 to set the timed reminder.
                            setAlarmService(2, first_hint_id, first_hint_num, first_hint_type, first_hint_time + Constant.DAY_FOR_24_HOURS);
                            compareAllData();
                            return 2000;
                        }
                        cursor.close();//Close cursor
                        index += 1;//Query the next data
                    return accordanceNumberSetAlarmTask(index);
                } else {//Set the time greater than the current time to set the timed task
                        setAlarmService(2, id, number, hint_type, hintTime);
                    }
                }
            }
        }
        return 1000;
    }

    /**
     * update data
     * @param index
     */
    private void setNumberValuerToDataValues(int id,int index){
        ContentValues values = new ContentValues();
        values.put("number_values" , index);
        datasDao.updateValue("plans", values, "_id=?", new String[]{String.valueOf(id)});
    }
    //======================================== Set a single task ============= ===================
    /**
           * Set the timing service in this way when there is only one data in the data table.
           */
    private void setTaskAtOnlyOneDataInDataList() {
        Cursor cursor = datasDao.selectAll("plans");
        while (cursor.moveToNext()){
            //Query the relevant values
            int id = cursor.getInt(cursor.getColumnIndex("_id"));//id
            Long hintTime = cursor.getLong(cursor.getColumnIndex("hint_time"));//Prompt time
            int number = cursor.getInt(cursor.getColumnIndex("number_values"));//sort order
            int hint_type = cursor.getInt(cursor.getColumnIndex("sport_type"));//Prompt type
            setAlarmService(1, id, number, hint_type,hintTime);
            break;
        }
        cursor.close();
    }
    /**
     * Loop a task
     */
    private void setCirculationTaskAtOnlyOneDataInDataList() {
        Long nowTime = DateUtils.getNowMillisecondValues();//current time
        Long nowDate = DateUtils.getNowDateMillisecondValues();//current date
        Long hintTime; ///prompt time
        Long stopDate; /// prompt date
        Log.e ("Setting Timing", "The only task in the loop data table");
        Cursor cursor = datasDao.selectAll("plans");
        int counts = cursor.getCount();
        while (cursor.moveToNext()){
            // Query the relevant values
            int id = cursor.getInt(cursor.getColumnIndex("_id"));//id
            int number = cursor.getInt(cursor.getColumnIndex("number_values"));//sort order
            int hint_type = cursor.getInt(cursor.getColumnIndex("sport_type"));//Prompt type
            int hour = cursor.getInt(cursor.getColumnIndex("hint_hour"));//Prompt time--->
            int minute = cursor.getInt(cursor.getColumnIndex("hint_minute"));//Prompt time--->minute
            int stop_year = cursor.getInt(cursor.getColumnIndex("stop_year"));//end date--->year
            int stop_month = cursor.getInt(cursor.getColumnIndex("stop_month"));//End Date--->Month
            int stop_day = cursor.getInt(cursor.getColumnIndex("stop_day"));//End Date--->Day
            // Set the prompt time --> Re-acquisition of the long type of millisecond value to be prompted to set the start time of the timing
            hintTime = DateUtils.getMillisecondValues(hour, minute);
            stopDate = DateUtils.getMillisecondValues(stop_year,stop_month,stop_day);
            if (counts == 1){
                if (hintTime < nowTime){
                    hintTime += Constant.DAY_FOR_24_HOURS;
                }
                if (stopDate < nowDate){//Set the next timing of the loop
                    Log.e ("Notification", "Setup Service");
                    // Set the timing
                    setAlarmService(1, id, number, hint_type, hintTime);
                }else{//Notify broadcast close service
                    Log.e ("Notification", "Close Service");
                    // Clear the data in the table
                    datasDao.clear("plans");
                    sendBroadcast(new Intent(this, FunctionBroadcastReceiver.class).setAction("PLAN").putExtra("mode", 3));
                }
            }else{
                Log.e ("Data to increase", "will be executed after setting the timing!");
            }
            break;
        }
        cursor.close();
    }
    //======================================================================================== =================
    /**
           * Features
           */
    private void functionalVerification(){
        Cursor cursor = datasDao.selectColumn("plans", new String[]{"_id", "hint_hour", "hint_minute", "number_values"});
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int hour = cursor.getInt(cursor.getColumnIndex("hint_hour"));
            int minute = cursor.getInt(cursor.getColumnIndex("hint_minute"));
            int nunber = cursor.getInt(cursor.getColumnIndex("number_values"));
            Log.e("Element","_id = " + id + "\t\t" + "time = " + hour + "point" + minute + "minute" +"\t\t" + "serial number=" + nunber);

        }
        cursor.close();
    }

    /**
      * Set up scheduled tasks
      * @ id
      * @ Arrange number number
      * @ hint type hint_type
      * @ hintTime
      */
    private void setAlarmService(int mode , int id , int number , int hint_type , long hintTime){
        toBroadReciver = new Intent(this, FunctionBroadcastReceiver.class);
        toBroadReciver.setAction(planSaveService);
        toBroadReciver.putExtra("mode",mode);//int
        toBroadReciver.putExtra("id", id);//int
        toBroadReciver.putExtra("number", number);//int
        toBroadReciver.putExtra("hint_type",hint_type);//int
        senser = PendingIntent.getBroadcast(this, 0, toBroadReciver, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP, hintTime, senser);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e (" all plann executed", "The service is over");
    }
}