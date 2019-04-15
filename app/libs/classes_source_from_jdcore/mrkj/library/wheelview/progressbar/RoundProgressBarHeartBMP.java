package mrkj.library.wheelview.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import mrkj.library.R.styleable;





public class RoundProgressBarHeartBMP
  extends HorizontalProgressBarWithNumber
{
  private int mRadius = dp2px(30);
  private int mMaxPaintWidth;
  private String atext = "000";
  
  public RoundProgressBarHeartBMP(Context context)
  {
    this(context, null);
  }
  
  public RoundProgressBarHeartBMP(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    
    mReachedProgressBarHeight = ((int)(mUnReachedProgressBarHeight * 2.5F));
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWidthNumber);
    
    mRadius = ((int)ta.getDimension(R.styleable.RoundProgressBarWidthNumber_radius, mRadius));
    
    ta.recycle();
    
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setAntiAlias(true);
    mPaint.setDither(true);
    mPaint.setStrokeCap(Paint.Cap.ROUND);
  }
  
  public void setText(String str)
  {
    String t = "000";
    if (str.length() == 1) {
      t = "00" + str;
    }
    if (str.length() == 2) {
      t = "0" + str;
    }
    if (str.length() == 3) {
      t = str;
    }
    atext = t;
  }
  





  protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    mMaxPaintWidth = Math.max(mReachedProgressBarHeight, mUnReachedProgressBarHeight);
    
    int expect = mRadius * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();
    
    int width = resolveSize(expect, widthMeasureSpec);
    int height = resolveSize(expect, heightMeasureSpec);
    int realWidth = Math.min(width, height);
    
    mRadius = ((realWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2);
    
    setMeasuredDimension(realWidth, realWidth);
  }
  



  protected synchronized void onDraw(Canvas canvas)
  {
    String text = atext + "BMP";
    float textWidth = mPaint.measureText(text);
    float textHeight = (mPaint.descent() + mPaint.ascent()) / 2.0F;
    
    canvas.save();
    canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
    
    mPaint.setStyle(Paint.Style.STROKE);
    
    mPaint.setColor(mUnReachedBarColor);
    mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
    canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    
    mPaint.setColor(mReachedBarColor);
    mPaint.setStrokeWidth(mReachedProgressBarHeight);
    float sweepAngle = getProgress() * 1.0F / getMax() * 360.0F;
    canvas.drawArc(new RectF(0.0F, 0.0F, mRadius * 2, mRadius * 2), 0.0F, sweepAngle, false, mPaint);
    

    mPaint.setStyle(Paint.Style.FILL);
    canvas.drawText(text, mRadius - textWidth / 2.0F, mRadius - textHeight, mPaint);
    

    canvas.restore();
  }
}
