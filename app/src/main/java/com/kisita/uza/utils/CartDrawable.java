package com.kisita.uza.utils;

/**
 * Created by Hugues on 25/04/2017.
 */
        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.ColorFilter;
        import android.graphics.Paint;
        import android.graphics.PixelFormat;
        import android.graphics.Rect;
        import android.graphics.Typeface;
        import android.graphics.drawable.Drawable;
        import android.support.v4.content.ContextCompat;

        import com.kisita.uza.R;


public class CartDrawable extends Drawable {

    private Paint mBadgePaint;
    private Paint mBadgePaint1;
    private Paint mTextPaint;
    private Rect mTxtRect = new Rect();
    private float mdiff = 0;
    private Context mContext;

    private String mCount = "";
    private boolean mWillDraw;

    public CartDrawable(Context context) {
        float mTextSize = context.getResources().getDimension(R.dimen.abc_action_bar_content_inset_material);

        mBadgePaint = new Paint();
        mBadgePaint.setColor(Color.RED);
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);
        mBadgePaint1 = new Paint();
        mBadgePaint1.setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.red_light));
        mBadgePaint1.setAntiAlias(true);
        mBadgePaint1.setStyle(Paint.Style.FILL);

        mContext = context;

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {



        if (!mWillDraw) {
            return;
        }
        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        // Position the badge in the top-right quadrant of the icon.

	        /*Using Math.max rather than Math.min */

        float radius = (((Math.max(width, height) / 2)) / 2) + mdiff;
        float centerX = (width - radius - 1);
        float centerY = radius ;
        if(mCount.length() <= 2){
            // Draw badge circle.
            canvas.drawCircle(centerX, centerY, (int)(radius+7.5), mBadgePaint1);
            canvas.drawCircle(centerX, centerY, (int)(radius+5.5), mBadgePaint);
        }
        else{
            canvas.drawCircle(centerX, centerY, (int)(radius+8.5), mBadgePaint1);
            canvas.drawCircle(centerX, centerY, (int)(radius+6.5), mBadgePaint);
//	        	canvas.drawRoundRect(radius, radius, radius, radius, 10, 10, mBadgePaint);
        }
        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
        float textHeight = mTxtRect.bottom - mTxtRect.top;
        float textY = centerY + (textHeight / 2f);
        if(mCount.length() > 2)
            canvas.drawText("99+", centerX, textY, mTextPaint);
        else
            canvas.drawText(mCount, centerX, textY, mTextPaint);
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    public void setCount(String count,float diff) {
        mCount = count;
        mdiff = diff;

        // Only draw a badge if there are notifications.
        mWillDraw = true;
        if(mCount.equalsIgnoreCase("0")){
            mBadgePaint.setColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.main_color));
            mBadgePaint1.setColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.main_color));
        }else{
            mBadgePaint.setColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.red_light));
            mBadgePaint1.setColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.red_light));
        }
        invalidateSelf();
    }

    public void setColor(int color){
        mBadgePaint.setColor(ContextCompat.getColor(mContext.getApplicationContext(),color));
        mBadgePaint1.setColor(ContextCompat.getColor(mContext.getApplicationContext(),color));
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
