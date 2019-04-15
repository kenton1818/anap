package mrkj.library.wheelview.progressbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.widget.ProgressBar;
import mrkj.library.R.styleable;





public class HorizontalProgressBarWithNumber
  extends ProgressBar
{
  private static final int DEFAULT_TEXT_SIZE = 10;
  private static final int DEFAULT_TEXT_COLOR = -261935;
  private static final int DEFAULT_COLOR_UNREACHED_COLOR = -2894118;
  private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 2;
  private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 2;
  private static final int DEFAULT_SIZE_TEXT_OFFSET = 10;
  protected Paint mPaint = new Paint();
  


  protected int mTextColor = -261935;
  


  protected int mTextSize = sp2px(10);
  



  protected int mTextOffset = dp2px(10);
  



  protected int mReachedProgressBarHeight = dp2px(2);
  



  protected int mReachedBarColor = -261935;
  


  protected int mUnReachedBarColor = -2894118;
  


  protected int mUnReachedProgressBarHeight = dp2px(2);
  

  protected int mRealWidth;
  

  protected boolean mIfDrawText = true;
  
  protected static final int VISIBLE = 0;
  
  public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }
  

  public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    obtainStyledAttributes(attrs);
    mPaint.setTextSize(mTextSize);
    mPaint.setColor(mTextColor);
  }
  



  protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    int width = View.MeasureSpec.getSize(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);
    setMeasuredDimension(width, height);
    
    mRealWidth = (getMeasuredWidth() - getPaddingRight() - getPaddingLeft());
  }
  
  private int measureHeight(int measureSpec)
  {
    int result = 0;
    int specMode = View.MeasureSpec.getMode(measureSpec);
    int specSize = View.MeasureSpec.getSize(measureSpec);
    if (specMode == 1073741824)
    {
      result = specSize;
    }
    else {
      float textHeight = mPaint.descent() - mPaint.ascent();
      result = (int)(getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mReachedProgressBarHeight, mUnReachedProgressBarHeight), Math.abs(textHeight)));
      

      if (specMode == Integer.MIN_VALUE)
      {
        result = Math.min(result, specSize);
      }
    }
    return result;
  }
  






  private void obtainStyledAttributes(AttributeSet attrs)
  {
    TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBarWithNumber);
    

    mTextColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_text_color, -261935);
    


    mTextSize = ((int)attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_size, mTextSize));
    


    mReachedBarColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_reached_color, mTextColor);
    


    mUnReachedBarColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_color, -2894118);
    


    mReachedProgressBarHeight = ((int)attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_reached_bar_height, mReachedProgressBarHeight));
    


    mUnReachedProgressBarHeight = ((int)attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_bar_height, mUnReachedProgressBarHeight));
    


    mTextOffset = ((int)attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_offset, mTextOffset));
    



    int textVisible = attributes.getInt(R.styleable.HorizontalProgressBarWithNumber_progress_text_visibility, 0);
    

    if (textVisible != 0)
    {
      mIfDrawText = false;
    }
    attributes.recycle();
  }
  


  protected synchronized void onDraw(Canvas canvas)
  {
    canvas.save();
    canvas.translate(getPaddingLeft(), getHeight() / 2);
    
    boolean noNeedBg = false;
    float radio = getProgress() * 1.0F / getMax();
    float progressPosX = (int)(mRealWidth * radio);
    String text = getProgress() + "%";
    

    float textWidth = mPaint.measureText(text);
    float textHeight = (mPaint.descent() + mPaint.ascent()) / 2.0F;
    
    if (progressPosX + textWidth > mRealWidth)
    {
      progressPosX = mRealWidth - textWidth;
      noNeedBg = true;
    }
    

    float endX = progressPosX - mTextOffset / 2;
    if (endX > 0.0F)
    {
      mPaint.setColor(mReachedBarColor);
      mPaint.setStrokeWidth(mReachedProgressBarHeight);
      canvas.drawLine(0.0F, 0.0F, endX, 0.0F, mPaint);
    }
    

    if (mIfDrawText)
    {
      mPaint.setColor(mTextColor);
      canvas.drawText(text, progressPosX, -textHeight, mPaint);
    }
    

    if (!noNeedBg)
    {
      float start = progressPosX + mTextOffset / 2 + textWidth;
      mPaint.setColor(mUnReachedBarColor);
      mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
      canvas.drawLine(start, 0.0F, mRealWidth, 0.0F, mPaint);
    }
    
    canvas.restore();
  }
  






  protected int dp2px(int dpVal)
  {
    return (int)TypedValue.applyDimension(1, dpVal, getResources().getDisplayMetrics());
  }
  







  protected int sp2px(int spVal)
  {
    return (int)TypedValue.applyDimension(2, spVal, getResources().getDisplayMetrics());
  }
}
