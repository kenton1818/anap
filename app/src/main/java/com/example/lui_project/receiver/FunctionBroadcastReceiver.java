package com.example.lui_project.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.lui_project.PlayActivity;
import com.example.lui_project.R;
import com.example.lui_project.service.ExecuteHealthyPlanService;
import com.example.lui_project.service.RecordedSaveService;
import com.example.lui_project.service.StepCounterService;
import com.example.lui_project.utils.Constant;
import com.example.lui_project.utils.SaveKeyValues;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Broadcast for receiving scheduled service transmissions
   * Remind users after receiving
 */
public class FunctionBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (RecordedSaveService.cancelSaveService.equals(action)){

            //Turn off the record step service
            Intent cancel= new Intent(context,RecordedSaveService.class);
            context.stopService(cancel);
        }else if (StepCounterService.alarmSaveService.equals(action)){

            //Open record step counter service
            Intent start= new Intent(context,RecordedSaveService.class);
            context.startService(start);
        }else if (ExecuteHealthyPlanService.planSaveService.equals(action)){
            int taskID;
            int taskNum;
            int taskType;

            int mode = intent.getIntExtra("mode", 1);
            String[] warm_up_exercise = new String[5];
            warm_up_exercise[0]=context.getString(R.string.warm_up_exercise0);
            warm_up_exercise[1]=context.getString(R.string.warm_up_exercise1);
            warm_up_exercise[2]=context.getString(R.string.warm_up_exercise2);
            warm_up_exercise[3]=context.getString(R.string.warm_up_exercise3);
            warm_up_exercise[4]=context.getString(R.string.warm_up_exercise4);
            switch (mode){
                case 1://Perform a single task
                    taskType = intent.getIntExtra("hint_type",0);
                    Log.e("通知","通知用戶進行運動");
                    Log.e("通知","提示類型" + taskType);

                    Log.e("通知","提醒計畫" + warm_up_exercise[taskType]);
                    sendNotification(context , taskType ,warm_up_exercise[taskType]);
                    context.startService(new Intent(context, ExecuteHealthyPlanService.class).putExtra("code", Constant.ONE_PLAN));
                    break;
                case 2://Executing multiple timing tasks
                    Log.e("Multiple timed tasks "," the number of data at this time is greater than 1");
                    taskType = intent.getIntExtra("hint_type",0);
                    taskID = intent.getIntExtra("id",0);
                    taskNum = intent.getIntExtra("number",0);
                    //Get current data
                    Log.e ("Notification", "Notify the user to exercise");
                    Log.e ("Notification", "Prompt Type" + taskType);
                    Log.e ("Notification", "Prompt Plan" + warm_up_exercise[taskType]);
                    Log.e ("data", "data_ID" + taskID);
                    Log.e ("data", "data serial number" + taskNum);
                    sendNotification(context , taskType ,warm_up_exercise[taskType]);
                    //The number of tasks is greater than 1
                    context.startService(new Intent(context, ExecuteHealthyPlanService.class).putExtra("code", Constant.NEXT_PLAN).putExtra("started_num",taskNum).putExtra("started_id",taskID));
                    break;
                case 3://Close service
                    Log.e("Notification", "will be executed to close the service");
                    int finish_plans = SaveKeyValues.getIntValues("finish_plan", 0);
                    SaveKeyValues.putIntValues("finish_plan", ++finish_plans);
                    context.stopService(new Intent(context, ExecuteHealthyPlanService.class));
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * Send notification
     * @param context
     * @param type
     * @param messages
     */
    private void sendNotification(Context context , int type , String messages){
        SaveKeyValues.putIntValues("do_hint",1);
        SaveKeyValues.putLongValues("show_hint", System.currentTimeMillis());
        Notification.Builder builder;
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "notification_sport";
            NotificationChannel channel = new NotificationChannel(channelId, "simple", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder = new Notification.Builder(context, channelId);
        } else {
            builder = new Notification.Builder(context);
        }
        Intent intent = new Intent(context , PlayActivity.class);
        intent.putExtra("play_type", type);
        intent.putExtra("what",1);
        intent.putExtra("do_hint",1);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle("KeepFit");
        builder.setContentText(messages);
        builder.setSmallIcon(R.mipmap.sport_do_sport);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        manager.notify(0, builder.getNotification());


    }
}
