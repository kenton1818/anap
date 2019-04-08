package com.example.lui_project;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.lui_project.Fragment.MineFragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public class Test_mineActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.activity_test);
        //添加SportFragment
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_launch", false);
        MineFragment sportFragment = new MineFragment();
        sportFragment.setArguments(bundle);
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.frag,sportFragment).
                commit();
            }




        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.

};



