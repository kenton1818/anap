package com.example.lui_project.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 讀取asset下的數據庫到local
 */
public class BringData {
   //數據庫路徑
    public static final String DATA_PATH = "/data/data/com.example.lui_project/databases/";
    //文件名
    public static final String DATA_NAME = "keepfit";
    public static void getDataFromAssets(Context context) throws IOException {
        Log.e("input length","perform！");
        AssetManager assetManager = context.getAssets();
        InputStream is;
        is = assetManager.open("db/" + DATA_NAME);
        Log.e("文件", "data/" + DATA_NAME);
        is.available();
        Log.e("input length", "perform！");
        Log.e("input length", is.available() + "byte");
        File file = new File(DATA_PATH);
        boolean have = file.exists();
        Log.e("Is there a folder?",have+"");
        if (have == false){
            //create folder
            file.mkdir();
        }
        //文件夾存在時進行讀寫操作
        if (file.exists()){
            Log.e("folder create success",file.exists()+"");
            BufferedInputStream bIs = new BufferedInputStream(is);
            BufferedOutputStream bOs = new BufferedOutputStream(new FileOutputStream(new File(DATA_PATH+DATA_NAME)));

            byte[] buff = new byte[1024];
            int len;
            while ((len = bIs.read(buff))!=-1){
                bOs.write(buff,0,len);
                bOs.flush();
            }
            is.close();
            bIs.close();
            bOs.close();
        }

    }
}
