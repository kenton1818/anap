package com.example.lui_project.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lui_project.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.lui_project.PlayActivity;
import com.example.lui_project.adapter.MyAdapter;
import com.example.lui_project.db.DatasDao;
import com.example.lui_project.service.ExecuteHealthyPlanService;
import com.example.lui_project.utils.Constant;



public class FindFragment extends Fragment {
    private View view;//layout
    private Context context;
    public static Bitmap[] bitmaps = new Bitmap[5];
    public static String[] warm_up_exercise = new String[5];


    private ListView listView;
    private List<Map<String,Object>> list;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find,null);
        listView = (ListView) view.findViewById(R.id.find_list);

        if (isAdded()){
            list = new ArrayList<>();
            for (int i =0 ; i< 5 ; i++){

                Map<String,Object> map =new HashMap<>();
                warm_up_exercise[0]=this.getString(R.string.warm_up_exercise0);
                warm_up_exercise[1]=this.getString(R.string.warm_up_exercise1);
                warm_up_exercise[2]=this.getString(R.string.warm_up_exercise2);
                warm_up_exercise[3]=this.getString(R.string.warm_up_exercise3);
                warm_up_exercise[4]=this.getString(R.string.warm_up_exercise4);
                bitmaps[0] = BitmapFactory.decodeResource(this.getResources(), R.mipmap.sport_image1);
                bitmaps[1] = BitmapFactory.decodeResource(this.getResources(),R.mipmap.sport_image2);
                bitmaps[2] = BitmapFactory.decodeResource(this.getResources(),R.mipmap.sport_image3);
                bitmaps[3] = BitmapFactory.decodeResource(this.getResources(),R.mipmap.sport_image4);
                bitmaps[4] = BitmapFactory.decodeResource(this.getResources(),R.mipmap.sport_image5);
                map.put("tu",bitmaps[i]);
                map.put("xm",warm_up_exercise[i]);
                map.put("cs", this.getString(R.string.find_training_time));
                map.put("tj",context.getString(R.string.find_map_put));
                list.add(map);
            }
            MyAdapter adapter = new MyAdapter(context,list,this);
            listView.setAdapter(adapter);
            DatasDao datasDao = new DatasDao(getContext());
            Cursor cursor = datasDao.selectAll("plans");

        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,context.getString(R.string.find_toast_start_sport), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), PlayActivity.class).putExtra("play_type", position).putExtra("what",1));
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 2000){
            DatasDao datasDao = new DatasDao(getContext());
            Cursor cursor = datasDao.selectAll("plans");
            if (cursor.getCount() == 1){
                 // Open the execution plan service
                Log.e("start service","service init");
                getContext().startService(new Intent(getContext(), ExecuteHealthyPlanService.class).putExtra("code", Constant.START_PLAN));
            }else{
                Log.e("start service","start service");
                Log.e("current mission", cursor.getCount() + "tasks");
                getContext().startService(new Intent(getContext(), ExecuteHealthyPlanService.class).putExtra("code", Constant.CHANGE_PLAN));
            }
            cursor.close();
        }
    }



}