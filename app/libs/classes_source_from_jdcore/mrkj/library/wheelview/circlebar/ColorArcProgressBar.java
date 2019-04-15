package mrkj.library.wheelview.circlebar;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import mrkj.library.R.styleable;

public class ColorArcProgressBar extends View
{
  private int mWidth;
  private int mHeight;
  private int diameter = 500;
  
  private float centerX;
  
  private float centerY;
  
  private Paint allArcPaint;
  
  private Paint progressPaint;
  private Paint vTextPaint;
  private Paint hintPaint;
  private Paint degreePaint;
  private Paint curSpeedPaint;
  private RectF bgRect;
  private ValueAnimator progressAnimator;
  private PaintFlagsDrawFilter mDrawFilter;
  private SweepGradient sweepGradient;
  private Matrix rotateMatrix;
  private float startAngle = 135.0F;
  private float sweepAngle = 270.0F;
  private float currentAngle = 0.0F;
  private float lastAngle;
  private int[] colors = { -16711936, 65280, -65536, -65536 };
  private float maxValues = 60.0F;
  private float curValues = 0.0F;
  private float bgArcWidth = dipToPx(2.0F);
  private float progressWidth = dipToPx(10.0F);
  private float textSize = dipToPx(60.0F);
  private float hintSize = dipToPx(15.0F);
  private float curSpeedSize = dipToPx(13.0F);
  private int aniSpeed = 1000;
  private float longdegree = dipToPx(13.0F);
  private float shortdegree = dipToPx(5.0F);
  private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8.0F);
  
  private String hintColor = "#4592F3";
  private String longDegreeColor = "#111111";
  private String shortDegreeColor = "#111111";
  private String bgArcColor = "#111111";
  
  private String titleString;
  private String hintString;
  private boolean isShowCurrentSpeed = true;
  
  private boolean isNeedTitle;
  private boolean isNeedUnit;
  private boolean isNeedDial;
  private boolean isNeedContent;
  private float k;
  
  public ColorArcProgressBar(Context context)
  {
    super(context, null);
    initView();
  }
  
  public ColorArcProgressBar(Context context, AttributeSet attrs) {
    super(context, attrs, 0);
    initCofig(context, attrs);
    initView();
  }
  
  public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initCofig(context, attrs);
    initView();
  }
  




  private void initCofig(Context context, AttributeSet attrs)
  {
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorArcProgressBar);
    int color1 = a.getColor(R.styleable.ColorArcProgressBar_front_color1, -16711936);
    int color2 = a.getColor(R.styleable.ColorArcProgressBar_front_color2, color1);
    int color3 = a.getColor(R.styleable.ColorArcProgressBar_front_color3, color1);
    colors = new int[] { color1, color2, color3, color3 };
    
    sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270);
    bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx(2.0F));
    progressWidth = a.getDimension(R.styleable.ColorArcProgressBar_front_width, dipToPx(10.0F));
    isNeedTitle = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_title, false);
    isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, false);
    isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false);
    isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false);
    hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit);
    titleString = a.getString(R.styleable.ColorArcProgressBar_string_title);
    curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0.0F);
    maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60.0F);
    setCurrentValues(curValues);
    setMaxValues(maxValues);
    a.recycle();
  }
  

  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    int width = (int)(2.0F * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
    int height = (int)(2.0F * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
    setMeasuredDimension(width, height);
  }
  
  private void initView()
  {
    diameter = (3 * getScreenWidth() / 5);
    
    bgRect = new RectF();
    bgRect.top = (longdegree + progressWidth / 2.0F + DEGREE_PROGRESS_DISTANCE);
    bgRect.left = (longdegree + progressWidth / 2.0F + DEGREE_PROGRESS_DISTANCE);
    bgRect.right = (diameter + (longdegree + progressWidth / 2.0F + DEGREE_PROGRESS_DISTANCE));
    bgRect.bottom = (diameter + (longdegree + progressWidth / 2.0F + DEGREE_PROGRESS_DISTANCE));
    

    centerX = ((2.0F * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2.0F);
    centerY = ((2.0F * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2.0F);
    

    degreePaint = new Paint();
    degreePaint.setColor(Color.parseColor(longDegreeColor));
    

    allArcPaint = new Paint();
    allArcPaint.setAntiAlias(true);
    allArcPaint.setStyle(Paint.Style.STROKE);
    allArcPaint.setStrokeWidth(bgArcWidth);
    allArcPaint.setColor(Color.parseColor(bgArcColor));
    allArcPaint.setStrokeCap(Paint.Cap.ROUND);
    

    progressPaint = new Paint();
    progressPaint.setAntiAlias(true);
    progressPaint.setStyle(Paint.Style.STROKE);
    progressPaint.setStrokeCap(Paint.Cap.ROUND);
    progressPaint.setStrokeWidth(progressWidth);
    progressPaint.setColor(-16711936);
    

    vTextPaint = new Paint();
    vTextPaint.setTextSize(textSize);
    vTextPaint.setColor(Color.parseColor(hintColor));
    vTextPaint.setTextAlign(Paint.Align.CENTER);
    

    hintPaint = new Paint();
    hintPaint.setTextSize(hintSize);
    hintPaint.setColor(Color.parseColor(hintColor));
    hintPaint.setTextAlign(Paint.Align.CENTER);
    

    curSpeedPaint = new Paint();
    curSpeedPaint.setTextSize(curSpeedSize);
    curSpeedPaint.setColor(Color.parseColor(hintColor));
    curSpeedPaint.setTextAlign(Paint.Align.CENTER);
    
    mDrawFilter = new PaintFlagsDrawFilter(0, 3);
    sweepGradient = new SweepGradient(centerX, centerY, colors, null);
    rotateMatrix = new Matrix();
  }
  


  protected void onDraw(Canvas canvas)
  {
    canvas.setDrawFilter(mDrawFilter);
    
    if (isNeedDial)
    {
      for (int i = 0; i < 40; i++) {
        if ((i > 15) && (i < 25)) {
          canvas.rotate(9.0F, centerX, centerY);
        }
        else {
          if (i % 5 == 0) {
            degreePaint.setStrokeWidth(dipToPx(2.0F));
            degreePaint.setColor(Color.parseColor(longDegreeColor));
            canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2.0F - DEGREE_PROGRESS_DISTANCE, centerX, centerY - diameter / 2 - progressWidth / 2.0F - DEGREE_PROGRESS_DISTANCE - longdegree, degreePaint);
          }
          else {
            degreePaint.setStrokeWidth(dipToPx(1.4F));
            degreePaint.setColor(Color.parseColor(shortDegreeColor));
            canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2.0F - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2.0F, centerX, centerY - diameter / 2 - progressWidth / 2.0F - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2.0F - shortdegree, degreePaint);
          }
          

          canvas.rotate(9.0F, centerX, centerY);
        }
      }
    }
    
    canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);
    

    rotateMatrix.setRotate(130.0F, centerX, centerY);
    sweepGradient.setLocalMatrix(rotateMatrix);
    progressPaint.setShader(sweepGradient);
    

    canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);
    
    if (isNeedContent) {
      canvas.drawText(String.format("%.0f", new Object[] { Float.valueOf(curValues) }), centerX, centerY + textSize / 3.0F, vTextPaint);
    }
    if (isNeedUnit) {
      canvas.drawText(hintString, centerX, centerY + 2.0F * textSize / 3.0F, hintPaint);
    }
    if (isNeedTitle) {
      canvas.drawText(titleString, centerX, centerY - 2.0F * textSize / 3.0F, curSpeedPaint);
    }
    
    invalidate();
  }
  




  public void setMaxValues(float maxValues)
  {
    this.maxValues = maxValues;
    k = (sweepAngle / maxValues);
  }
  



  public void setCurrentValues(float currentValues)
  {
    if (currentValues > maxValues) {
      currentValues = maxValues;
    }
    if (currentValues < 0.0F) {
      currentValues = 0.0F;
    }
    curValues = currentValues;
    lastAngle = currentAngle;
    setAnimation(lastAngle, currentValues * k, aniSpeed);
  }
  



  public void setBgArcWidth(int bgArcWidth)
  {
    this.bgArcWidth = bgArcWidth;
  }
  



  public void setProgressWidth(int progressWidth)
  {
    this.progressWidth = progressWidth;
  }
  



  public void setTextSize(int textSize)
  {
    this.textSize = textSize;
  }
  



  public void setHintSize(int hintSize)
  {
    this.hintSize = hintSize;
  }
  



  public void setUnit(String hintString)
  {
    this.hintString = hintString;
    invalidate();
  }
  



  public void setDiameter(int diameter)
  {
    this.diameter = dipToPx(diameter);
  }
  



  private void setTitle(String title)
  {
    titleString = title;
  }
  



  private void setIsNeedTitle(boolean isNeedTitle)
  {
    this.isNeedTitle = isNeedTitle;
  }
  



  private void setIsNeedUnit(boolean isNeedUnit)
  {
    this.isNeedUnit = isNeedUnit;
  }
  



  private void setIsNeedDial(boolean isNeedDial)
  {
    this.isNeedDial = isNeedDial;
  }
  




  private void setAnimation(float last, float current, int length)
  {
    progressAnimator = ValueAnimator.ofFloat(new float[] { last, current });
    progressAnimator.setDuration(length);
    progressAnimator.setTarget(Float.valueOf(currentAngle));
    progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator animation)
      {
        currentAngle = ((Float)animation.getAnimatedValue()).floatValue();
        curValues = (currentAngle / k);
      }
    });
    progressAnimator.start();
  }
  




  private int dipToPx(float dip)
  {
    float density = getContextgetResourcesgetDisplayMetricsdensity;
    return (int)(dip * density + 0.5F * (dip >= 0.0F ? 1 : -1));
  }
  



  private int getScreenWidth()
  {
    WindowManager windowManager = (WindowManager)getContext().getSystemService("window");
    DisplayMetrics displayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    return widthPixels;
  }
}
