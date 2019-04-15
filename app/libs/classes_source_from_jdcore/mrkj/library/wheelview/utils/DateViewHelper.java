package mrkj.library.wheelview.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import mrkj.library.R.id;
import mrkj.library.R.layout;
import mrkj.library.wheelview.OnWheelScrollListener;
import mrkj.library.wheelview.WheelView;
import mrkj.library.wheelview.adapter.NumericWheelAdapter;


public class DateViewHelper
{
  private int mYear = 1996;
  private int mMonth = 0;
  private int mDay = 1;
  
  private WheelView year;
  private WheelView month;
  private WheelView day;
  private WheelView time;
  private WheelView min;
  private WheelView sec;
  private View view;
  private Context context;
  private OnResultMessageListener listener;
  
  public void setOnResultMessageListener(OnResultMessageListener onResultMessageListener)
  {
    listener = onResultMessageListener;
  }
  
  public DateViewHelper(Context context) { this.context = context; }
  
  public View getDataPick(LayoutInflater inflater) {
    Calendar c = Calendar.getInstance();
    int norYear = c.get(1);
    


    int curYear = norYear;
    int curMonth = c.get(2) + 1;
    int curDate = c.get(5);
    
    view = inflater.inflate(R.layout.wheel_date_picker, null);
    
    year = ((WheelView)view.findViewById(R.id.year));
    NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(context, 1950, norYear);
    numericWheelAdapter1.setLabel("年");
    year.setViewAdapter(numericWheelAdapter1);
    year.setCyclic(true);
    year.addScrollingListener(scrollListener);
    
    month = ((WheelView)view.findViewById(R.id.month));
    NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(context, 1, 12, "%02d");
    numericWheelAdapter2.setLabel("月");
    month.setViewAdapter(numericWheelAdapter2);
    month.setCyclic(true);
    month.addScrollingListener(scrollListener);
    
    day = ((WheelView)view.findViewById(R.id.day));
    initDay(curYear, curMonth);
    day.setCyclic(true);
    day.addScrollingListener(scrollListener);
    






















    year.setVisibleItems(7);
    month.setVisibleItems(7);
    day.setVisibleItems(7);
    



    year.setCurrentItem(curYear - 1950);
    month.setCurrentItem(curMonth - 1);
    day.setCurrentItem(curDate - 1);
    
    return view;
  }
  
  OnWheelScrollListener scrollListener = new OnWheelScrollListener()
  {
    public void onScrollingStarted(WheelView wheel) {}
    


    public void onScrollingFinished(WheelView wheel)
    {
      int n_year = year.getCurrentItem() + 1950;
      int n_month = month.getCurrentItem() + 1;
      int n_day = day.getCurrentItem() + 1;
      DateViewHelper.this.initDay(n_year, n_month);
      
      String birthday = year.getCurrentItem() + 1950 + "-" + (month.getCurrentItem() + 1 < 10 ? "0" + (month.getCurrentItem() + 1) : Integer.valueOf(month.getCurrentItem() + 1)) + "-" + (day.getCurrentItem() + 1 < 10 ? "0" + (day.getCurrentItem() + 1) : Integer.valueOf(day.getCurrentItem() + 1));
      Map<String, Object> map = new HashMap();
      map.put("age", DateViewHelper.calculateDatePoor(birthday));
      map.put("astro", getAstro(month.getCurrentItem() + 1, day.getCurrentItem() + 1));
      map.put("year", Integer.valueOf(n_year));
      map.put("month", Integer.valueOf(n_month));
      map.put("day", Integer.valueOf(n_day));
      if (listener != null) {
        listener.getMessage(map);
      }
    }
  };
  





  private int getDay(int year, int month)
  {
    int day = 30;
    boolean flag = false;
    switch (year % 4) {
    case 0: 
      flag = true;
      break;
    default: 
      flag = false;
    }
    
    switch (month) {
    case 1: 
    case 3: 
    case 5: 
    case 7: 
    case 8: 
    case 10: 
    case 12: 
      day = 31;
      break;
    case 2: 
      day = flag ? 29 : 28;
      break;
    case 4: case 6: case 9: case 11: default: 
      day = 30;
    }
    
    return day;
  }
  

  private void initDay(int arg1, int arg2)
  {
    NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context, 1, getDay(arg1, arg2), "%02d");
    numericWheelAdapter.setLabel("日");
    day.setViewAdapter(numericWheelAdapter);
  }
  



  public static final String calculateDatePoor(String birthday)
  {
    try
    {
      if (TextUtils.isEmpty(birthday))
        return "0";
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date birthdayDate = sdf.parse(birthday);
      String currTimeStr = sdf.format(new Date());
      Date currDate = sdf.parse(currTimeStr);
      if (birthdayDate.getTime() > currDate.getTime()) {
        return "0";
      }
      long age = (currDate.getTime() - birthdayDate.getTime()) / 86400000L + 1L;
      
      String year = new DecimalFormat("0.00").format((float)age / 365.0F);
      if (TextUtils.isEmpty(year))
        return "0";
      return String.valueOf(new Double(year).intValue());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return "0";
  }
  





  public String getAstro(int month, int day)
  {
    String[] astro = { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };
    
    int[] arr = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
    int index = month;
    
    if (day < arr[(month - 1)]) {
      index -= 1;
    }
    
    return astro[index];
  }
  
  public static abstract interface OnResultMessageListener
  {
    public abstract void getMessage(Map<String, Object> paramMap);
  }
}
