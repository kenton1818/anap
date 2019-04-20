package com.example.lui_project;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.lui_project.utils.SaveKeyValues;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import com.example.lui_project.utils.SaveKeyValues;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SharedPreferenceDao_unittest {
    SaveKeyValues sharedpreference ;
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        SaveKeyValues.createSharePreferences(appContext);
        Boolean result = check();
        Boolean test = true;
        assertEquals(test, result);
    }

    public Boolean check()
    {
        String test = "1234567891.10";
        String stringresult;
        SaveKeyValues.putStringValues("test",test);
        stringresult = SaveKeyValues.getStringValues("test","0");


        if (stringresult == test) {
                return true;

        }
        return false;
    }
}
