package mrkj.library.wheelview.circlebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
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
  private int stepnumbernow; private float pressExtraStrokeWidth; private BarAnimation anim; private int stepnumbermax = 6000;
  private float mPercent_y;
  private float stepnumber_y; private float Text_y; private DecimalFormat fnum = new DecimalFormat("#.0");
  
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
    mColorWheelPaint = new Paint();
    mColorWheelPaint.setColor(Color.rgb(249, 135, 49));
    mColorWheelPaint.setStyle(Paint.Style.STROKE);
    mColorWheelPaint.setStrokeCap(Paint.Cap.ROUND);
    mColorWheelPaint.setAntiAlias(true);
    
    mColorWheelPaintCentre = new Paint();
    mColorWheelPaintCentre.setColor(Color.rgb(250, 250, 250));
    mColorWheelPaintCentre.setStyle(Paint.Style.STROKE);
    mColorWheelPaintCentre.setStrokeCap(Paint.Cap.ROUND);
    mColorWheelPaintCentre.setAntiAlias(true);
    
    mDefaultWheelPaint = new Paint();
    mDefaultWheelPaint.setColor(Color.rgb(127, 127, 127));
    mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
    mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);
    mDefaultWheelPaint.setAntiAlias(true);
    
    mTextP = new Paint();
    mTextP.setAntiAlias(true);
    mTextP.setColor(Color.rgb(249, 135, 49));
    
    mTextnum = new Paint();
    mTextnum.setAntiAlias(true);
    mTextnum.setColor(-16777216);
    
    mTextch = new Paint();
    mTextch.setAntiAlias(true);
    mTextch.setColor(-16777216);
    
    anim = new BarAnimation();
  }
  
  protected void onDraw(Canvas canvas)
  {
    canvas.drawArc(mColorWheelRectangle, 0.0F, 359.0F, false, mDefaultWheelPaint);
    canvas.drawArc(mColorWheelRectangle, 0.0F, 359.0F, false, mColorWheelPaintCentre);
    
    canvas.drawArc(mColorWheelRectangle, 90.0F, mSweepAnglePer, false, mColorWheelPaint);
    
    canvas.drawText(mPercent + "%", mColorWheelRectangle.centerX() - mTextP.measureText(String.valueOf(mPercent) + "%") / 2.0F, mPercent_y, mTextP);
    

    canvas.drawText(stepnumbernow + "", mColorWheelRectangle.centerX() - mTextnum.measureText(String.valueOf(stepnumbernow)) / 2.0F, stepnumber_y, mTextnum);
    

    canvas.drawText("步数", mColorWheelRectangle.centerX() - mTextch.measureText(String.valueOf("步数")) / 2.0F, Text_y, mTextch);
  }
  




  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    
    int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
    int min = Math.min(width, height);
    setMeasuredDimension(min, min);
    circleStrokeWidth = Textscale(40.0F, min);
    pressExtraStrokeWidth = Textscale(2.0F, min);
    mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth, circleStrokeWidth + pressExtraStrokeWidth, min - circleStrokeWidth - pressExtraStrokeWidth, min - circleStrokeWidth - pressExtraStrokeWidth);
    


    mTextP.setTextSize(Textscale(80.0F, min));
    mTextnum.setTextSize(Textscale(160.0F, min));
    mTextch.setTextSize(Textscale(50.0F, min));
    mPercent_y = Textscale(190.0F, min);
    stepnumber_y = Textscale(330.0F, min);
    Text_y = Textscale(400.0F, min);
    mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
    mColorWheelPaintCentre.setStrokeWidth(circleStrokeWidth);
    mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth - Textscale(2.0F, min));
    
    mDefaultWheelPaint.setShadowLayer(Textscale(10.0F, min), 0.0F, 0.0F, Color.rgb(127, 127, 127));
  }
  






  public class BarAnimation
    extends android.view.animation.Animation
  {
    public BarAnimation() {}
    






    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
      super.applyTransformation(interpolatedTime, t);
      if (interpolatedTime < 1.0F) {
        mPercent = Float.parseFloat(fnum.format(interpolatedTime * stepnumber * 100.0F / stepnumbermax));
        
        mSweepAnglePer = (interpolatedTime * stepnumber * 360.0F / stepnumbermax);
        
        stepnumbernow = ((int)(interpolatedTime * stepnumber));
      } else {
        mPercent = Float.parseFloat(fnum.format(stepnumber * 100.0F / stepnumbermax));
        
        mSweepAnglePer = (stepnumber * 360 / stepnumbermax);
        stepnumbernow = stepnumber;
      }
      postInvalidate();
    }
  }
  






  public float Textscale(float n, float m)
  {
    return n / 500.0F * m;
  }
  





  public void update(int stepnumber, int time)
  {
    this.stepnumber = stepnumber;
    anim.setDuration(time);
    
    startAnimation(anim);
  }
  




  public void setMaxstepnumber(int Maxstepnumber)
  {
    stepnumbermax = Maxstepnumber;
  }
  






  public void setColor(int red, int green, int blue)
  {
    mColorWheelPaint.setColor(Color.rgb(red, green, blue));
  }
  



  public void setcolor(int resID)
  {
    mColorWheelPaint.setColor(getResources().getColor(resID));
    mTextP.setColor(getResources().getColor(resID));
  }
  



  public void setAnimationTime(int time)
  {
    anim.setDuration(time * stepnumber / stepnumbermax);
  }
}
