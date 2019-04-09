package com.example.lui_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lui_project.Fragment.FindFragment;

public class Test_findActivity extends AppCompatActivity {
    public static Bitmap[] bitmaps = new Bitmap[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        bitmaps[0] = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.mrkj_fushen1);
        bitmaps[1] = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.mrkj_fuwocheng1);
        bitmaps[2] = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.mrkj_gunlun1);
        bitmaps[3] = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.mrkj_wotui1);
        bitmaps[4] = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.mrkj_sanwanju1);
        //添加SportFragment
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_launch", false);
        FindFragment sportFragment = new FindFragment();
        sportFragment.setArguments(bundle);
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.frag,sportFragment).
                commit();
            }
        };



