package com.example.lui_project;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lui_project.base.BaseActivity;
import com.example.lui_project.Fragment.FindFragment;
import com.example.lui_project.Fragment.MineFragment;
import com.example.lui_project.Fragment.SportFragment;
import com.example.lui_project.utils.BringData;
import com.example.lui_project.utils.Constant;
import com.example.lui_project.utils.SaveKeyValues;

import java.io.IOException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;

/**
 * Function view
 */
public class FunctionActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{


    private long exitTime;//第一次單機退出鍵的時間
    private int load_values;//判斷加載
    // fragment的變量
    // 控件
    private RadioGroup radioGroup;//切換按鈕的容器
    private RadioButton sport_btn,find_btn,mine_btn;//切換按鈕//碎片
    private SportFragment sportFragment;//sport
    private FindFragment findFragment;//find
    private MineFragment mineFragment;//my
    public static boolean take_a_look = false;
    /**
     * sitting title
     */
    @Override
    protected void setActivityTitle() {

    }

    /**
     *init ui
     */
    @Override
    protected void getLayoutToView() {
        setContentView(R.layout.activity_function);
    }

    /**
     * init variable
     */
    @Override
    protected void initValues() {


        //GPS permission
        if (!checkPermission()) {
            requestPermission();
        }
        //如果這個值等於1就加載運動界面，等於2就加載發現界面
        load_values = SaveKeyValues.getIntValues("launch_which_fragment", 0);

        Log.e("加載判斷值", load_values + "");
        //new fragment
        sportFragment = new SportFragment();
        findFragment = new FindFragment();
        mineFragment = new MineFragment();
        //init ui
        if (take_a_look == true) {
            Log.d("take a look", "1");
            getSupportFragmentManager().beginTransaction().add(R.id.frag_home,mineFragment,Constant.MINE_TAG).commit();

        } else {

            Log.d("take a look", "0");
            if (load_values == Constant.TURN_MAIN) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_launch", true);
                sportFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.frag_home, sportFragment, Constant.SPORT_TAG).commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.frag_home, findFragment, Constant.FIND_TAG).commit();
            }
        }
    }
    /**
     * init
     */
    @Override
    protected void initViews() {
        radioGroup = (RadioGroup) findViewById(R.id.ui_btn_group);
        sport_btn = (RadioButton) findViewById(R.id.sport_btn);
        find_btn = (RadioButton) findViewById(R.id.find_btn);
        mine_btn = (RadioButton) findViewById(R.id.mine_btn);

    }

    /**
     * 設置監聽
     */
    @Override
    protected void setViewsListener() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 設置功能
     */
    @Override
    protected void setViewsFunction() {

        if (load_values == Constant.TURN_MAIN){
            sport_btn.setChecked(true);
        }else {
            find_btn.setChecked(true);
        }
        if (take_a_look==true)
        {
            mine_btn.setChecked(true);
            take_a_look = false;
        }
    }

    /**
     * 切換界面
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (checkedId){
            case R.id.sport_btn://運舫
                if (!sportFragment.isAdded()){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("is_launch", false);
                    sportFragment.setArguments(bundle);
                    transaction.replace(R.id.frag_home,sportFragment,Constant.SPORT_TAG);
                }
                break;
            case R.id.find_btn://發熱運動
                if (!findFragment.isAdded()){
                    transaction.replace(R.id.frag_home, findFragment,Constant.FIND_TAG);
                }
                break;

            case R.id.mine_btn://我的信息
                if (!mineFragment.isAdded()){
                    transaction.replace(R.id.frag_home,mineFragment,Constant.MINE_TAG);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 按兩次退出按鈕退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // System.currentTimeMillis()無論何時調用，肯定大於2000
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, FunctionActivity.this.getString(R.string.Function_exit_app), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED ;
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_NETWORK_STATE}, 200);

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(FunctionActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void Take_look()
    {
        getSupportFragmentManager().beginTransaction().add(R.id.frag_home,mineFragment,"mine").commit();
        take_a_look = false;
    }
}
