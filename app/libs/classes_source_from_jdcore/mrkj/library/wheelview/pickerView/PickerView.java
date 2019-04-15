package mrkj.library.wheelview.pickerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



















public class PickerView
  extends View
{
  public static final String TAG = "PickerView";
  public static final float MARGIN_ALPHA = 3.5F;
  public static final float SPEED = 3.0F;
  private List<String> mDataList;
  private int mCurrentSelected;
  private Paint mPaint;
  private float mMaxTextSize;
  private float mMinTextSize;
  private float mMaxTextAlpha = 250.0F;
  private float mMinTextAlpha = 100.0F;
  private int mColorText = 3355443;
  
  private int mViewHeight;
  
  private int mViewWidth;
  
  private float mLastDownX;
  private float mMoveLen = 0.0F;
  private boolean isInit = false;
  
  private onSelectListener mSelectListener;
  private Timer timer;
  private MyTimerTask mTask;
  private float baseline;
  Handler updateHandler = new Handler()
  {


    public void handleMessage(Message msg)
    {

      if (Math.abs(mMoveLen) < 3.0F)
      {
        mMoveLen = 0.0F;
        if (mTask != null)
        {
          mTask.cancel();
          mTask = null;
          PickerView.this.performSelect();
        }
      }
      else {
        mMoveLen = (mMoveLen - mMoveLen / Math.abs(mMoveLen) * 3.0F); }
      invalidate();
    }
  };
  

  public PickerView(Context context)
  {
    super(context);
    init();
  }
  
  public PickerView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init();
  }
  




  public void setOnSelectListener(onSelectListener listener)
  {
    mSelectListener = listener;
  }
  
  private void performSelect()
  {
    if (mSelectListener != null) {
      mSelectListener.onSelect((String)mDataList.get(mCurrentSelected));
    }
  }
  
  public void setData(List<String> datas) {
    mDataList = datas;
    mCurrentSelected = (datas.size() / 2);
    invalidate();
  }
  
  private List<String> initData() {
    List<String> list = new ArrayList();
    for (int i = 1; i <= 100; i++) {
      list.add(i + "");
    }
    mCurrentSelected = (list.size() / 2);
    return list;
  }
  
  public void setSelected(int selected) {
    mCurrentSelected = selected;
  }
  
  private void moveHeadToTail()
  {
    String head = (String)mDataList.get(0);
    mDataList.remove(0);
    mDataList.add(head);
  }
  
  private void moveTailToHead()
  {
    String tail = (String)mDataList.get(mDataList.size() - 1);
    mDataList.remove(mDataList.size() - 1);
    mDataList.add(0, tail);
  }
  

  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    mViewHeight = getMeasuredHeight();
    mViewWidth = getMeasuredWidth();
    
    mMaxTextSize = (mViewWidth / 12.0F);
    mMinTextSize = (mMaxTextSize / 2.0F);
    isInit = true;
    invalidate();
  }
  
  private void init()
  {
    timer = new Timer();
    
    mDataList = initData();
    mPaint = new Paint(1);
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setTextAlign(Paint.Align.CENTER);
    mPaint.setColor(mColorText);
  }
  

  protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    
    if (isInit) {
      drawData(canvas);
    }
  }
  
  private void drawData(Canvas canvas)
  {
    float scale = parabola(mViewWidth / 2.0F, mMoveLen);
    float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize + 5.0F;
    
    mPaint.setTextSize(size);
    
    mPaint.setAlpha((int)((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
    
    float y = (float)(mViewHeight / 2.0D);
    float x = (float)(mViewWidth / 2.0D + mMoveLen);
    Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
    baseline = ((float)(y - (bottom / 2.0D + top / 2.0D)));
    
    canvas.drawText((String)mDataList.get(mCurrentSelected), x, baseline, mPaint);
    for (int i = 1; mCurrentSelected - i >= 0; i++)
    {

      drawOtherText(canvas, i, -1);
    }
    
    for (int i = 1; mCurrentSelected + i < mDataList.size(); i++)
    {

      drawOtherText(canvas, i, 1);
    }
  }
  









  private void drawOtherText(Canvas canvas, int position, int type)
  {
    float d = 3.5F * mMinTextSize * position + type * mMoveLen;
    
    float scale = parabola(mViewWidth / 2.0F, d);
    float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
    mPaint.setTextSize(size);
    mPaint.setAlpha((int)((mMaxTextAlpha - mMinTextAlpha) * scale));
    float x = (float)(mViewWidth / 2.0D + type * d);
    
    Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
    

    canvas.drawText((String)mDataList.get(mCurrentSelected + type * position), x, baseline, mPaint);
  }
  










  private float parabola(float zero, float x)
  {
    float f = (float)(1.0D - Math.pow(x / zero, 2.0D));
    return f < 0.0F ? 0.0F : f;
  }
  

  public boolean onTouchEvent(MotionEvent event)
  {
    switch (event.getActionMasked())
    {
    case 0: 
      doDown(event);
      break;
    case 2: 
      doMove(event);
      break;
    case 1: 
      doUp(event);
    }
    
    return true;
  }
  
  private void doDown(MotionEvent event)
  {
    if (mTask != null)
    {
      mTask.cancel();
      mTask = null;
    }
    mLastDownX = event.getX();
  }
  

  private void doMove(MotionEvent event)
  {
    mMoveLen += event.getX() - mLastDownX;
    System.out.println("second:" + mMoveLen);
    if (mMoveLen > 3.5F * mMinTextSize / 2.0F)
    {

      moveTailToHead();
      mMoveLen -= 3.5F * mMinTextSize;
    } else if (mMoveLen < -3.5F * mMinTextSize / 2.0F)
    {

      moveHeadToTail();
      mMoveLen += 3.5F * mMinTextSize;
    }
    
    mLastDownX = event.getX();
    invalidate();
  }
  

  private void doUp(MotionEvent event)
  {
    if (Math.abs(mMoveLen) < 1.0E-4D)
    {
      mMoveLen = 0.0F;
      return;
    }
    if (mTask != null)
    {
      mTask.cancel();
      mTask = null;
    }
    mTask = new MyTimerTask(updateHandler);
    timer.schedule(mTask, 0L, 10L);
  }
  
  public static abstract interface onSelectListener {
    public abstract void onSelect(String paramString);
  }
  
  class MyTimerTask extends TimerTask { Handler handler;
    
    public MyTimerTask(Handler handler) { this.handler = handler; }
    


    public void run()
    {
      handler.sendMessage(handler.obtainMessage());
    }
  }
}
