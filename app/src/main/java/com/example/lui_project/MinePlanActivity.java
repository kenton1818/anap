package com.example.lui_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lui_project.Fragment.FindFragment;
import com.example.lui_project.base.BaseActivity;
import com.example.lui_project.db.DatasDao;
import com.example.lui_project.service.ExecuteHealthyPlanService;
import com.example.lui_project.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MinePlanActivity extends BaseActivity {

    public static Bitmap[] bitmaps = new Bitmap[5];
    public static String[] warm_up_exercise = new String[5];
    private List<Map<String,Object>> plan_List;//儲存計劃數據
    private ListView listView;//列表
    private DatasDao datasDao;//數據庫操作
    private Cursor cursor;//cursor
    private MyAdapter myAdapter;//Adapter
    @Override
    protected void setActivityTitle() {
        initTitle();//初始化標題欄
        setTitle(MinePlanActivity.this.getString(R.string.minePlan_title), this) ;
        setMyBackGround(R.color.watm_background_gray);//設置標題欄的背景顏色
        setTitleTextColor(R.color.theme_blue_two);//設置字體的顏色
        setTitleLeftImage(R.mipmap.sport_back_blue);//設施返回鍵的圖片
    }

    /**
     * init布局
     */
    @Override
    protected void getLayoutToView() {
        setContentView(R.layout.activity_mine_plan);
    }

    /**
     * init data
     */
    @Override
    protected void initValues() {
        plan_List = new ArrayList<>();
        datasDao = new DatasDao(this);
        cursor = datasDao.selectAll("plans");//獲取游標用來查詢
        //遍歷所有數據
        while (cursor.moveToNext()){
            Map<String,Object> map = new HashMap<>();
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int type = cursor.getInt(cursor.getColumnIndex("sport_type"));
            int s_year = cursor.getInt(cursor.getColumnIndex("start_year"));
            int s_month = cursor.getInt(cursor.getColumnIndex("start_month"));
            int s_day = cursor.getInt(cursor.getColumnIndex("start_day"));
            int t_year = cursor.getInt(cursor.getColumnIndex("stop_year"));
            int t_month = cursor.getInt(cursor.getColumnIndex("stop_month"));
            int t_day = cursor.getInt(cursor.getColumnIndex("stop_day"));
            String h_time = cursor.getString(cursor.getColumnIndex("hint_str"));
            if (s_year == t_year && s_month == t_month & s_day == t_day){
                map.put("date",s_year+"-"+s_month+"-"+s_day);
            }else {
                map.put("date",s_year+"-"+s_month+"-"+s_day+"~"+t_year+"-"+t_month+"-"+t_day);
            }
            map.put("id",id);
            map.put("type",type);
            map.put("time",h_time);
            //向list中添加數據
            plan_List.add(map);
        }
        //close cursor
        cursor.close();
    }

    /**
     * init control view
     */
    @Override
    protected void initViews() {
        listView = (ListView) findViewById(R.id.plan_list);
    }

    /**
     * 設置控件的監聽
     */
    @Override
    protected void setViewsListener() {

    }

    /**
     * 設置控件的功能
     */
    @Override
    protected void setViewsFunction() {
        if (plan_List.size() > 0){
            myAdapter = new MyAdapter();//Build adapter
            listView.setAdapter(myAdapter);//Binding adapter
        }
        //Set EmptyView to display when the data in the List is empty.
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setText(this.getString(R.string.sportmsg_nodata));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(50);
        addContentView(textView,params);
        listView.setEmptyView(textView);
    }

    /**
     * 返回界面
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Refresh the data list after returning
        if (requestCode == 3000 && resultCode ==RESULT_OK){
            Log.e("提示", "Successful setup");
            plan_List.clear();//清空數據
            List<Map<String,Object>> update = new ArrayList<>();
            cursor = datasDao.selectAll("plans");
            while (cursor.moveToNext()){
                Map<String,Object> map = new HashMap<>();
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int type = cursor.getInt(cursor.getColumnIndex("sport_type"));
                int s_year = cursor.getInt(cursor.getColumnIndex("start_year"));
                int s_month = cursor.getInt(cursor.getColumnIndex("start_month"));
                int s_day = cursor.getInt(cursor.getColumnIndex("start_day"));
                int t_year = cursor.getInt(cursor.getColumnIndex("stop_year"));
                int t_month = cursor.getInt(cursor.getColumnIndex("stop_month"));
                int t_day = cursor.getInt(cursor.getColumnIndex("stop_day"));
                String h_time = cursor.getString(cursor.getColumnIndex("hint_str"));
                if (s_year == t_year && s_month == t_month & s_day == t_day){
                    map.put("date",s_year+"-"+s_month+"-"+s_day);
                }else {
                    map.put("date",s_year+"-"+s_month+"-"+s_day+"~"+t_year+"-"+t_month+"-"+t_day);
                }
                map.put("id",id);
                map.put("type",type);
                map.put("time",h_time);
                update.add(map);
            }
            plan_List.addAll(update);
            myAdapter.noti();//Notification adapter update
            cursor.close();//Close cursor
        }
    }

    /**
     * Custom adapter
     */
    class MyAdapter extends BaseAdapter {


        private void noti(){
            this.notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return plan_List.size();
        }

        @Override
        public Object getItem(int position) {
            return plan_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.plan_item,null);
                holder = new ViewHolder();
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.hint_time = (TextView) convertView.findViewById(R.id.hint_time);
                holder.name = (TextView) convertView.findViewById(R.id.planitem);
                holder.count = (TextView) convertView.findViewById(R.id.planTime);
                holder.change = (TextView) convertView.findViewById(R.id.add_plan);
                holder.delete = (TextView) convertView.findViewById(R.id.delete_plan);
                holder.image = (ImageView) convertView.findViewById(R.id.image_show);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            Map<String,Object> map = plan_List.get(position);
            holder.date.setText(map.get("date").toString());//時間段
            holder.hint_time.setText(MinePlanActivity.this.getString(R.string.minePlan_tips)+map.get("time").toString());//時間點
            warm_up_exercise[0]=MinePlanActivity.this.getString(R.string.warm_up_exercise0);
            warm_up_exercise[1]=MinePlanActivity.this.getString(R.string.warm_up_exercise1);
            warm_up_exercise[2]=MinePlanActivity.this.getString(R.string.warm_up_exercise2);
            warm_up_exercise[3]=MinePlanActivity.this.getString(R.string.warm_up_exercise3);
            warm_up_exercise[4]=MinePlanActivity.this.getString(R.string.warm_up_exercise4);
            bitmaps[0] = BitmapFactory.decodeResource(MinePlanActivity.this.getResources(), R.mipmap.sport_image1);
            bitmaps[1] = BitmapFactory.decodeResource(MinePlanActivity.this.getResources(),R.mipmap.sport_image2);
            bitmaps[2] = BitmapFactory.decodeResource(MinePlanActivity.this.getResources(),R.mipmap.sport_image3);
            bitmaps[3] = BitmapFactory.decodeResource(MinePlanActivity.this.getResources(),R.mipmap.sport_image4);
            bitmaps[4] = BitmapFactory.decodeResource(MinePlanActivity.this.getResources(),R.mipmap.sport_image5);
            holder.name.setText(warm_up_exercise[((int) map.get("type"))]);//類型
            holder.count.setText(MinePlanActivity.this.getString(R.string.find_training_time));
            holder.change.setText(MinePlanActivity.this.getString(R.string.minePlan_change));
            holder.delete.setText(MinePlanActivity.this.getString(R.string.minePlan_delete2));
            holder.image.setImageBitmap(bitmaps[((int) map.get("type"))]);//類型

            final int id = (int) map.get("id");
            holder.change.setTag(id);
            holder.delete.setTag(id);
            holder.change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (id == (int)holder.change.getTag()){
                        startActivityForResult(new Intent(MinePlanActivity.this,UpdateActivity.class).putExtra("position",position).putExtra("id",id),3000);
                        Toast.makeText(getApplicationContext(), MinePlanActivity.this.getString(R.string.minePlan_change), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //***************************** DELETE *****************************
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (id == (int)holder.delete.getTag()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MinePlanActivity.this);
                        builder.setTitle(MinePlanActivity.this.getString(R.string.minePlan_delete3));
                        builder.setMessage(MinePlanActivity.this.getString(R.string.minePlan_delete_comfirm));
                        builder.setPositiveButton(MinePlanActivity.this.getString(R.string.minePlan_delete_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                datasDao.deleteValue("plans", "_id=?", new String[]{String.valueOf(id)});
                                startService(new Intent(MinePlanActivity.this, ExecuteHealthyPlanService.class).putExtra("code", Constant.CHANGE_PLAN));
                                plan_List.remove(position);
                                noti();
                                Toast.makeText(getApplicationContext(), MinePlanActivity.this.getString(R.string.minePlan_delete3), Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                }
            });
            return convertView;
        }
    }

    class ViewHolder{
        TextView date,name,count,change,delete,hint_time;//日期，名稱，次數，更改，刪除
        ImageView image;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
