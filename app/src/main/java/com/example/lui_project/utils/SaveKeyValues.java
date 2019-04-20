package com.example.lui_project.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SaveKeyValues {

    public static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    /**
      * init SharedPreferences
      * @param context
      */
    public static void createSharePreferences(Context context){
        String appName = context.getPackageName();
        Log.e("儲存的文件名",appName);
        sharedPreferences = context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
      * Determine if SharedPreferences is created
      * @return
      */
    public static boolean isUnCreate(){
        boolean result = (sharedPreferences == null) ? true : false;
        if (result){
            Log.e("提醒","sharedPreferences未被創建！");
        }
        return result;
    }
    /**
      * save string
      * @param key
      * @param values
      * @return
      */
    public static boolean putStringValues(String key,String values){
        if (isUnCreate()){
            return false;
        }
        editor.putString(key,values);
        return editor.commit();
    }

    /**
      * get string
      * @param key
      * @param defValue
      * @return
      */
    public static String getStringValues(String key,String defValue){
        if (isUnCreate()){
            return null;
        }
        String string_value = sharedPreferences.getString(key,defValue);
        return string_value;
    }

    /**
      * save int
      * @param key
      * @param values
      * @return
      */
    public static boolean putIntValues(String key,int values){
        if (isUnCreate()){
            return false;
        }
        editor.putInt(key, values);
        return editor.commit();
    }

    /**
      * get int
      * @param key
      * @param defValue
      * @return
      */
    public static int getIntValues(String key,int defValue){
        if (isUnCreate()){
            return 0;
        }
        int int_value = sharedPreferences.getInt(key, defValue);
        return int_value;
    }

    /**
      * save long
      * @param key
      * @param values
      * @return
      */
    public static boolean putLongValues(String key,long values){
        if (isUnCreate()){
            return false;
        }
        editor.putLong(key, values);
        return editor.commit();
    }

    /**
      * get long
      * @param key
      * @param defValue
      * @return
      */
    public static long getLongValues(String key,long defValue){
        if (isUnCreate()){
            return 0;
        }
        long long_value = sharedPreferences.getLong(key, defValue);
        return long_value;
    }
    /**
      * save float
      * @param key
      * @param values
      * @return
      */
    public static boolean putFloatValues(String key,float values){
        if (isUnCreate()){
            return false;
        }
        editor.putFloat(key, values);
        return editor.commit();
    }

    /**
      * get float
      * @param key
      * @param defValue
      * @return
      */
    public static float getFloatValues(String key,float defValue){
        if (isUnCreate()){
            return 0;
        }
        float float_value = sharedPreferences.getFloat(key, defValue);
        return float_value;
    }


    /**
      * Store the value of the boolean type
      * @param key
      * @param values
      * @return
      */
    public static boolean putBooleanValues(String key,boolean values){
        if (isUnCreate()){
            return false;
        }
        editor.putBoolean(key, values);
        return editor.commit();
    }

    /**
      * Take out the value of the boolean type
      * @param key
      * @param defValue
      * @return
      */
    public static boolean getFloatValues(String key,boolean defValue){
        if (isUnCreate()){
            return false;
        }
        boolean boolean_value = sharedPreferences.getBoolean(key, defValue);
        return boolean_value;
    }

    /**
      * Clear data
      */
    public static boolean deleteAllValues(){
        if (isUnCreate()){
            return false;
        }
        editor.clear();
        return editor.commit();
    }

    /**
      * Delete specified data
      * @param key
      * @return
      */
    public static boolean removeKeyForValues(String key){
        if (isUnCreate()){
            return false;
        }
        editor.remove(key);
        return editor.commit();
    }
}