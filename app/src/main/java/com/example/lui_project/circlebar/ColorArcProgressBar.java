package com.example.lui_project.circlebar;

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
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.example.lui_project.R;


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
  private int[] colors = { -16711936, -256, -65536, -65536 };
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
    this.colors = new int[] { color1, color2, color3, color3 };

    this.sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270);
    this.bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx(2.0F));
    this.progressWidth = a.getDimension(R.styleable.ColorArcProgressBar_front_width, dipToPx(10.0F));
    this.isNeedTitle = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_title, false);
    this.isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, false);
    this.isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false);
    this.isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false);
    this.hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit);
    this.titleString = a.getString(R.styleable.ColorArcProgressBar_string_title);
    this.curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0.0F);
    this.maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60.0F);
    setCurrentValues(this.curValues);
    setMaxValues(this.maxValues);
    a.recycle();
  }

  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    int width = (int)(2.0F * this.longdegree + this.progressWidth + this.diameter + 2 * this.DEGREE_PROGRESS_DISTANCE);
    int height = (int)(2.0F * this.longdegree + this.progressWidth + this.diameter + 2 * this.DEGREE_PROGRESS_DISTANCE);
    setMeasuredDimension(width, height);
  }

  private void initView()
  {
    this.diameter = (3 * getScreenWidth() / 5);

    this.bgRect = new RectF();
    this.bgRect.top = (this.longdegree + this.progressWidth / 2.0F + this.DEGREE_PROGRESS_DISTANCE);
    this.bgRect.left = (this.longdegree + this.progressWidth / 2.0F + this.DEGREE_PROGRESS_DISTANCE);
    this.bgRect.right = (this.diameter + (this.longdegree + this.progressWidth / 2.0F + this.DEGREE_PROGRESS_DISTANCE));
    this.bgRect.bottom = (this.diameter + (this.longdegree + this.progressWidth / 2.0F + this.DEGREE_PROGRESS_DISTANCE));

    this.centerX = ((2.0F * this.longdegree + this.progressWidth + this.diameter + 2 * this.DEGREE_PROGRESS_DISTANCE) / 2.0F);
    this.centerY = ((2.0F * this.longdegree + this.progressWidth + this.diameter + 2 * this.DEGREE_PROGRESS_DISTANCE) / 2.0F);

    this.degreePaint = new Paint();
    this.degreePaint.setColor(Color.parseColor(this.longDegreeColor));

    this.allArcPaint = new Paint();
    this.allArcPaint.setAntiAlias(true);
    this.allArcPaint.setStyle(Paint.Style.STROKE);
    this.allArcPaint.setStrokeWidth(this.bgArcWidth);
    this.allArcPaint.setColor(Color.parseColor(this.bgArcColor));
    this.allArcPaint.setStrokeCap(Paint.Cap.ROUND);

    this.progressPaint = new Paint();
    this.progressPaint.setAntiAlias(true);
    this.progressPaint.setStyle(Paint.Style.STROKE);
    this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
    this.progressPaint.setStrokeWidth(this.progressWidth);
    this.progressPaint.setColor(-16711936);

    this.vTextPaint = new Paint();
    this.vTextPaint.setTextSize(this.textSize);
    this.vTextPaint.setColor(Color.parseColor(this.hintColor));
    this.vTextPaint.setTextAlign(Paint.Align.CENTER);

    this.hintPaint = new Paint();
    this.hintPaint.setTextSize(this.hintSize);
    this.hintPaint.setColor(Color.parseColor(this.hintColor));
    this.hintPaint.setTextAlign(Paint.Align.CENTER);

    this.curSpeedPaint = new Paint();
    this.curSpeedPaint.setTextSize(this.curSpeedSize);
    this.curSpeedPaint.setColor(Color.parseColor(this.hintColor));
    this.curSpeedPaint.setTextAlign(Paint.Align.CENTER);

    this.mDrawFilter = new PaintFlagsDrawFilter(0, 3);
    this.sweepGradient = new SweepGradient(this.centerX, this.centerY, this.colors, null);
    this.rotateMatrix = new Matrix();
  }

  protected void onDraw(Canvas canvas)
  {
    canvas.setDrawFilter(this.mDrawFilter);

    if (this.isNeedDial)
    {
      for (int i = 0; i < 40; i++) {
        if ((i > 15) && (i < 25)) {
          canvas.rotate(9.0F, this.centerX, this.centerY);
        }
        else {
          if (i % 5 == 0) {
            this.degreePaint.setStrokeWidth(dipToPx(2.0F));
            this.degreePaint.setColor(Color.parseColor(this.longDegreeColor));
            canvas.drawLine(this.centerX, this.centerY - this.diameter / 2 - this.progressWidth / 2.0F - this.DEGREE_PROGRESS_DISTANCE, this.centerX, this.centerY - this.diameter / 2 - this.progressWidth / 2.0F - this.DEGREE_PROGRESS_DISTANCE - this.longdegree, this.degreePaint);
          }
          else {
            this.degreePaint.setStrokeWidth(dipToPx(1.4F));
            this.degreePaint.setColor(Color.parseColor(this.shortDegreeColor));
            canvas.drawLine(this.centerX, this.centerY - this.diameter / 2 - this.progressWidth / 2.0F - this.DEGREE_PROGRESS_DISTANCE - (this.longdegree - this.shortdegree) / 2.0F, this.centerX, this.centerY - this.diameter / 2 - this.progressWidth / 2.0F - this.DEGREE_PROGRESS_DISTANCE - (this.longdegree - this.shortdegree) / 2.0F - this.shortdegree, this.degreePaint);
          }

          canvas.rotate(9.0F, this.centerX, this.centerY);
        }
      }
    }

    canvas.drawArc(this.bgRect, this.startAngle, this.sweepAngle, false, this.allArcPaint);

    this.rotateMatrix.setRotate(130.0F, this.centerX, this.centerY);
    this.sweepGradient.setLocalMatrix(this.rotateMatrix);
    this.progressPaint.setShader(this.sweepGradient);

    canvas.drawArc(this.bgRect, this.startAngle, this.currentAngle, false, this.progressPaint);

    if (this.isNeedContent) {
      canvas.drawText(String.format("%.0f", new Object[] { Float.valueOf(this.curValues) }), this.centerX, this.centerY + this.textSize / 3.0F, this.vTextPaint);
    }
    if (this.isNeedUnit) {
      canvas.drawText(this.hintString, this.centerX, this.centerY + 2.0F * this.textSize / 3.0F, this.hintPaint);
    }
    if (this.isNeedTitle) {
      canvas.drawText(this.titleString, this.centerX, this.centerY - 2.0F * this.textSize / 3.0F, this.curSpeedPaint);
    }

    invalidate();
  }

  public void setMaxValues(float maxValues)
  {
    this.maxValues = maxValues;
    this.k = (this.sweepAngle / maxValues);
  }

  public void setCurrentValues(float currentValues)
  {
    if (currentValues > this.maxValues) {
      currentValues = this.maxValues;
    }
    if (currentValues < 0.0F) {
      currentValues = 0.0F;
    }
    this.curValues = currentValues;
    this.lastAngle = this.currentAngle;
    setAnimation(this.lastAngle, currentValues * this.k, this.aniSpeed);
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
    this.titleString = title;
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
    this.progressAnimator = ValueAnimator.ofFloat(new float[] { last, current });
    this.progressAnimator.setDuration(length);
    this.progressAnimator.setTarget(Float.valueOf(this.currentAngle));
    this.progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator animation)
      {
        ColorArcProgressBar.this.currentAngle = ((Float)animation.getAnimatedValue()).floatValue();
        ColorArcProgressBar.this.curValues = (ColorArcProgressBar.this.currentAngle / ColorArcProgressBar.this.k);
      }
    });
    this.progressAnimator.start();
  }

  private int dipToPx(float dip)
  {
    float density = getContext().getResources().getDisplayMetrics().density;
    return (int)(dip * density + 0.5F * (dip >= 0.0F ? 1 : -1));
  }

  private int getScreenWidth()
  {
    WindowManager windowManager = (WindowManager)getContext().getSystemService("window");
    DisplayMetrics displayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    return displayMetrics.widthPixels;
  }
}