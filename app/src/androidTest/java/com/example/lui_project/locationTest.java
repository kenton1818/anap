package com.example.lui_project;



        import android.content.Context;
        import android.support.test.InstrumentationRegistry;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;

        import org.junit.Before;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import static org.junit.Assert.*;
import com.example.lui_project.Fragment.SportFragment;
import com.example.lui_project.service.GpsLocation;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class locationTest {
    ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class);
    TestActivity set_location;

    @Before
    public void setUp() throws Exception {
        activityRule.launchActivity(null);
        set_location = activityRule.getActivity();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        GpsLocation GpsLocation = new GpsLocation();
        GpsLocation.Latitude =22.3193;
        GpsLocation.Longitude = 114.1694;
        SportFragment SportFragment = new SportFragment();

        SportFragment.setDownLoadMessageToView();

        assertEquals("HK", SportFragment.weather_city);
    }
}
