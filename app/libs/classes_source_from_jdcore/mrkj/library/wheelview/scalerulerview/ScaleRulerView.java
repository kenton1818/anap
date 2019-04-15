package mrkj.library.wheelview.scalerulerview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import mrkj.library.R.styleable;








public class ScaleRulerView
  extends View
{
  public static final int MOD_TYPE = 5;
  private static final int ITEM_HEIGHT_DIVIDER = 12;
  private static final int ITEM_MAX_HEIGHT = 36;
  private static final int ITEM_MIN_HEIGHT = 25;
  private static final int TEXT_SIZE = 18;
  private float mDensity;
  private float mValue = 50.0F;
  private float mMaxValue = 100.0F;
  private float mDefaultMinValue = 0.0F;
  private int mModType = 5;
  private int mLineDivider = 12;
  
  private int mLastX;
  private int mMove;
  private int mWidth;
  private int mHeight;
  private int mMinVelocity;
  private Scroller mScroller;
  private VelocityTracker mVelocityTracker;
  private OnValueChangeListener mListener;
  private Paint mLinePaint = new Paint();
  private Paint mSelectPaint = new Paint();
  private int mSelectWidth = 8;
  private int mNormalLineWidth = 4;
  

  private int mSelectColor;
  
  private int mNormalLineColor;
  

  public ScaleRulerView(Context context)
  {
    this(context, null);
  }
  
  public ScaleRulerView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }
  
  public ScaleRulerView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleRulerView);
    mSelectColor = typedArray.getColor(R.styleable.ScaleRulerView_mSelectPaintColor, Color.parseColor("#F7577F"));
    mNormalLineColor = typedArray.getColor(R.styleable.ScaleRulerView_mNormalLineColor, Color.parseColor("#E8E8E8"));
    typedArray.recycle();
    init(context);
  }
  
  protected void init(Context context) {
    mScroller = new Scroller(context);
    mDensity = getResourcesgetDisplayMetricsdensity;
    
    mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
  }
  



  public void initViewParam(float defaultValue, float maxValue, float defaultMinValue)
  {
    mValue = defaultValue;
    mMaxValue = maxValue;
    mDefaultMinValue = defaultMinValue;
    
    invalidate();
    
    mLastX = 0;
    mMove = 0;
    notifyValueChange();
  }
  




  public void setValueChangeListener(OnValueChangeListener listener)
  {
    mListener = listener;
  }
  




  public float getValue()
  {
    return mValue;
  }
  
  protected void onLayout(boolean changed, int left, int top, int right, int bottom)
  {
    mWidth = getWidth();
    mHeight = getHeight();
    super.onLayout(changed, left, top, right, bottom);
  }
  
  protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    
    drawScaleLine(canvas);
    drawMiddleLine(canvas);
  }
  





  private void drawScaleLine(Canvas canvas)
  {
    canvas.save();
    
    mLinePaint.setStrokeWidth(mNormalLineWidth);
    mLinePaint.setColor(mNormalLineColor);
    
    TextPaint textPaint = new TextPaint(1);
    textPaint.setTextSize(18.0F * mDensity);
    float textWidth = Layout.getDesiredWidth("0", textPaint);
    
    int width = mWidth;
    int drawCount = 0;
    

    for (int i = 0; drawCount <= 4 * width; i++)
    {
      float xPosition = width / 2 - mMove + i * mLineDivider * mDensity;
      if ((xPosition + getPaddingRight() < mWidth) && (mValue + i <= mMaxValue)) {
        if ((mValue + i) % mModType == 0.0F) {
          canvas.drawLine(xPosition, getHeight(), xPosition, getHeight() - mDensity * 36.0F, mLinePaint);

        }
        else
        {

          canvas.drawLine(xPosition, getHeight(), xPosition, getHeight() - mDensity * 25.0F, mLinePaint);
        }
      }
      
      xPosition = width / 2 - mMove - i * mLineDivider * mDensity;
      if ((xPosition > getPaddingLeft()) && (mValue - i >= mDefaultMinValue)) {
        if ((mValue - i) % mModType == 0.0F) {
          canvas.drawLine(xPosition, getHeight(), xPosition, getHeight() - mDensity * 36.0F, mLinePaint);

        }
        else
        {

          canvas.drawLine(xPosition, getHeight(), xPosition, getHeight() - mDensity * 25.0F, mLinePaint);
        }
      }
      
      drawCount = (int)(drawCount + 2 * mLineDivider * mDensity);
    }
    
    canvas.restore();
  }
  







  private float countLeftStart(int value, float xPosition, float textWidth)
  {
    float xp = 0.0F;
    if (value < 20) {
      xp = xPosition - textWidth * 1.0F / 2.0F;
    } else {
      xp = xPosition - textWidth * 2.0F / 2.0F;
    }
    return xp;
  }
  




  private void drawMiddleLine(Canvas canvas)
  {
    canvas.save();
    
    mSelectPaint.setStrokeWidth(mSelectWidth);
    mSelectPaint.setColor(mSelectColor);
    canvas.drawLine(mWidth / 2, 0.0F, mWidth / 2, mHeight, mSelectPaint);
    canvas.restore();
  }
  
  public boolean onTouchEvent(MotionEvent event)
  {
    int action = event.getAction();
    int xPosition = (int)event.getX();
    
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(event);
    
    switch (action) {
    case 0: 
      mScroller.forceFinished(true);
      
      mLastX = xPosition;
      mMove = 0;
      break;
    case 2: 
      mMove += mLastX - xPosition;
      changeMoveAndValue();
      break;
    case 1: 
    case 3: 
      countMoveEnd();
      countVelocityTracker(event);
      return false;
    }
    
    


    mLastX = xPosition;
    return true;
  }
  
  private void countVelocityTracker(MotionEvent event) {
    mVelocityTracker.computeCurrentVelocity(1000);
    float xVelocity = mVelocityTracker.getXVelocity();
    if (Math.abs(xVelocity) > mMinVelocity) {
      mScroller.fling(0, 0, (int)xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
    }
  }
  
  private void changeMoveAndValue()
  {
    int tValue = (int)(mMove / (mLineDivider * mDensity));
    if (Math.abs(tValue) > 0) {
      mValue += tValue;
      mMove = ((int)(mMove - tValue * mLineDivider * mDensity));
      if ((mValue <= mDefaultMinValue) || (mValue > mMaxValue)) {
        mValue = (mValue <= mDefaultMinValue ? mDefaultMinValue : mMaxValue);
        mMove = 0;
        mScroller.forceFinished(true);
      }
      
      notifyValueChange();
    }
    postInvalidate();
  }
  
  private void countMoveEnd() {
    int roundMove = Math.round(mMove / (mLineDivider * mDensity));
    mValue += roundMove;
    mValue = (mValue <= 0.0F ? 0.0F : mValue);
    mValue = (mValue > mMaxValue ? mMaxValue : mValue);
    
    mLastX = 0;
    mMove = 0;
    
    notifyValueChange();
    postInvalidate();
  }
  
  private void notifyValueChange() {
    if ((null != mListener) && 
      (mModType == 5)) {
      mListener.onValueChange(mValue);
    }
  }
  

  public void computeScroll()
  {
    super.computeScroll();
    if (mScroller.computeScrollOffset()) {
      if (mScroller.getCurrX() == mScroller.getFinalX()) {
        countMoveEnd();
      } else {
        int xPosition = mScroller.getCurrX();
        mMove += mLastX - xPosition;
        changeMoveAndValue();
        mLastX = xPosition;
      }
    }
  }
  
  public static abstract interface OnValueChangeListener
  {
    public abstract void onValueChange(float paramFloat);
  }
}
