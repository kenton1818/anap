package com.example.lui_project.circlebar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import java.text.DecimalFormat;

public class CircleBar extends View
{
  private RectF mColorWheelRectangle = new RectF();
  private Paint mDefaultWheelPaint;
  private Paint mColorWheelPaint;
  private Paint mColorWheelPaintCentre;
  private Paint mTextP;
  private Paint mTextnum;
  private Paint mTextch;
  private float circleStrokeWidth;
  private float mSweepAnglePer;
  private float mPercent;
  private int stepnumber;
  private int stepnumbernow;
  private float pressExtraStrokeWidth;
  private BarAnimation anim;
  private int stepnumbermax = 6000;
  private float mPercent_y;
  private float stepnumber_y;
  private float Text_y;
  private DecimalFormat fnum = new DecimalFormat("#.0");

  public CircleBar(Context context) {
    super(context);
    init(null, 0);
  }

  public CircleBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public CircleBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs, defStyle);
  }

  private void init(AttributeSet attrs, int defStyle)
  {
    this.mColorWheelPaint = new Paint();
    this.mColorWheelPaint.setColor(Color.rgb(249, 135, 49));
    this.mColorWheelPaint.setStyle(Paint.Style.STROKE);
    this.mColorWheelPaint.setStrokeCap(Paint.Cap.ROUND);
    this.mColorWheelPaint.setAntiAlias(true);

    this.mColorWheelPaintCentre = new Paint();
    this.mColorWheelPaintCentre.setColor(Color.rgb(250, 250, 250));
    this.mColorWheelPaintCentre.setStyle(Paint.Style.STROKE);
    this.mColorWheelPaintCentre.setStrokeCap(Paint.Cap.ROUND);
    this.mColorWheelPaintCentre.setAntiAlias(true);

    this.mDefaultWheelPaint = new Paint();
    this.mDefaultWheelPaint.setColor(Color.rgb(127, 127, 127));
    this.mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
    this.mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);
    this.mDefaultWheelPaint.setAntiAlias(true);

    this.mTextP = new Paint();
    this.mTextP.setAntiAlias(true);
    this.mTextP.setColor(Color.rgb(249, 135, 49));

    this.mTextnum = new Paint();
    this.mTextnum.setAntiAlias(true);
    this.mTextnum.setColor(-16777216);

    this.mTextch = new Paint();
    this.mTextch.setAntiAlias(true);
    this.mTextch.setColor(-16777216);

    this.anim = new BarAnimation();
  }

  protected void onDraw(Canvas canvas)
  {
    canvas.drawArc(this.mColorWheelRectangle, 0.0F, 359.0F, false, this.mDefaultWheelPaint);
    canvas.drawArc(this.mColorWheelRectangle, 0.0F, 359.0F, false, this.mColorWheelPaintCentre);

    canvas.drawArc(this.mColorWheelRectangle, 90.0F, this.mSweepAnglePer, false, this.mColorWheelPaint);

    canvas.drawText(this.mPercent + "%", this.mColorWheelRectangle.centerX() - this.mTextP.measureText(String.valueOf(this.mPercent) + "%") / 2.0F, this.mPercent_y, this.mTextP);

    canvas.drawText(this.stepnumbernow + "", this.mColorWheelRectangle.centerX() - this.mTextnum.measureText(String.valueOf(this.stepnumbernow)) / 2.0F, this.stepnumber_y, this.mTextnum);
    canvas.drawText("Steps", this.mColorWheelRectangle.centerX() - this.mTextch.measureText(String.valueOf("步数")) / 2.0F, this.Text_y, this.mTextch);

  }

  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

    int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
    int min = Math.min(width, height);
    setMeasuredDimension(min, min);
    this.circleStrokeWidth = Textscale(40.0F, min);
    this.pressExtraStrokeWidth = Textscale(2.0F, min);
    this.mColorWheelRectangle.set(this.circleStrokeWidth + this.pressExtraStrokeWidth, this.circleStrokeWidth + this.pressExtraStrokeWidth, min - this.circleStrokeWidth - this.pressExtraStrokeWidth, min - this.circleStrokeWidth - this.pressExtraStrokeWidth);

    this.mTextP.setTextSize(Textscale(80.0F, min));
    this.mTextnum.setTextSize(Textscale(160.0F, min));
    this.mTextch.setTextSize(Textscale(50.0F, min));
    this.mPercent_y = Textscale(190.0F, min);
    this.stepnumber_y = Textscale(330.0F, min);
    this.Text_y = Textscale(400.0F, min);
    this.mColorWheelPaint.setStrokeWidth(this.circleStrokeWidth);
    this.mColorWheelPaintCentre.setStrokeWidth(this.circleStrokeWidth);
    this.mDefaultWheelPaint.setStrokeWidth(this.circleStrokeWidth - Textscale(2.0F, min));

    this.mDefaultWheelPaint.setShadowLayer(Textscale(10.0F, min), 0.0F, 0.0F, Color.rgb(127, 127, 127));
  }

  public float Textscale(float n, float m)
  {
    return n / 500.0F * m;
  }

  public void update(int stepnumber, int time)
  {
    this.stepnumber = stepnumber;
    this.anim.setDuration(time);

    startAnimation(this.anim);
  }

  public void setMaxstepnumber(int Maxstepnumber)
  {
    this.stepnumbermax = Maxstepnumber;
  }

  public void setColor(int red, int green, int blue)
  {
    this.mColorWheelPaint.setColor(Color.rgb(red, green, blue));
  }

  public void setcolor(int resID)
  {
    this.mColorWheelPaint.setColor(getResources().getColor(resID));
    this.mTextP.setColor(getResources().getColor(resID));
  }

  public void setAnimationTime(int time)
  {
    this.anim.setDuration(time * this.stepnumber / this.stepnumbermax);
  }

  public class BarAnimation extends Animation
  {
    public BarAnimation()
    {
    }

    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
      super.applyTransformation(interpolatedTime, t);
      if (interpolatedTime < 1.0F) {
        CircleBar.this.mPercent = Float.parseFloat(CircleBar.this.fnum.format(interpolatedTime * CircleBar.this.stepnumber * 100.0F / CircleBar.this.stepnumbermax));

        CircleBar.this.mSweepAnglePer = (interpolatedTime * CircleBar.this.stepnumber * 360.0F / CircleBar.this.stepnumbermax);

        CircleBar.this.stepnumbernow = ((int)(interpolatedTime * CircleBar.this.stepnumber));
      } else {
        CircleBar.this.mPercent = Float.parseFloat(CircleBar.this.fnum.format(CircleBar.this.stepnumber * 100.0F / CircleBar.this.stepnumbermax));

        CircleBar.this.mSweepAnglePer = (CircleBar.this.stepnumber * 360 / CircleBar.this.stepnumbermax);
        CircleBar.this.stepnumbernow = CircleBar.this.stepnumber;
      }
      CircleBar.this.postInvalidate();
    }
  }
}