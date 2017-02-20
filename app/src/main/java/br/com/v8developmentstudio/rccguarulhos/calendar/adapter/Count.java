package br.com.v8developmentstudio.rccguarulhos.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;

import br.com.v8developmentstudio.rccguarulhos.calendar.R;

/**
 * Created by cleiton.dantas on 01/02/2017.
 */

public class Count extends Drawable {
    private Paint paint1, paint2, paint3;
    private Rect rect1 = new Rect();
    private String count1 = "";
    private boolean draw1;
    private static Integer contValue;

    public Count(Context context) {
        float mTextSize = context.getResources().getDimension(R.dimen.dayOfMonthTextSize);
        paint1 = new Paint();
        paint1.setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.dark_red));
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.FILL);
        paint2 = new Paint();
        paint2.setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.dark_red));
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL);

        paint3 = new Paint();
        paint3.setColor(Color.WHITE);
        paint3.setTypeface(Typeface.DEFAULT);
        paint3.setTextSize(mTextSize);
        paint3.setAntiAlias(true);
        paint3.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {

        if (!draw1) {
            return;
        }
        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        // Position the badge in the top-right quadrant of the icon.

	        /*Using Math.max rather than Math.min */

        float radius = ((Math.max(width, height) / 2)) / 2;
        float centerX = (width - radius - 1) +5;
        float centerY = radius -5;
        if(count1.length() <= 2){
            // Draw badge circle.
            canvas.drawCircle(centerX, centerY, (int)(radius+7.5), paint2);
            canvas.drawCircle(centerX, centerY, (int)(radius+5.5), paint1);
        }
        else{
            canvas.drawCircle(centerX, centerY, (int)(radius+8.5), paint2);
            canvas.drawCircle(centerX, centerY, (int)(radius+6.5), paint1);
//	        	canvas.drawRoundRect(radius, radius, radius, radius, 10, 10, paint1);
        }
        // Draw badge count text inside the circle.
        paint3.getTextBounds(count1, 0, count1.length(), rect1);
        float textHeight = rect1.bottom - rect1.top;
        float textY = centerY + (textHeight / 2f);
        if(count1.length() > 2)
            canvas.drawText("99+", centerX, textY, paint3);
        else
            canvas.drawText(count1, centerX, textY, paint3);
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    public void setCount(String count) {
        count1 = count;

        // Only draw a badge if there are notifications.
        draw1 = !count.equalsIgnoreCase("0");
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


    public static void setCounting(Context context, LayerDrawable icon, Integer count) {
        contValue = count;
        Count badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof Count) {
            badge = (Count) reuse;
        } else {
            badge = new Count(context);
        }

        badge.setCount(""+count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
    public static Integer getCont(){
        return contValue;
    }

}