package br.com.v8developmentstudio.rccguarulhos.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import br.com.v8developmentstudio.rccguarulhos.R;

/**
 * Created by cleiton.dantas on 02/12/2016.
 */

public class ColorDrawables {
    private Activity activity;
    public ColorDrawables(){

    }
    public ColorDrawables(Activity activity){
    this.activity = activity;
    }
    public  int color(int i){
        switch (i) {
            case 1:
                return R.color.red;
            case 2:
                return R.color.green;
            case 3:
                return R.color.dark_blue;
            case 4:
                return R.color.silver;
            case 5:
                return R.color.blue;
            case 6:
                return R.color.yelow;
            case 7:
                return R.color.dark_green;
            case 8:
                return R.color.dark_red;
            case 9:
                return R.color.pink;
            case 10:
                return R.color.sky;
            case 11:
                return R.color.verde_agua;
            case 12:
                return R.color.laranja;
            case 13:
                return R.color.marron;
            case 14:
                return R.color.verde_musgo;
            case 15:
                return R.color.verde_folha;
        }
        return R.color.red;
    }

    public  int colorString(int i){
        switch (i) {
            case 1:
                return R.string.red;
            case 2:
                return R.string.green;
            case 3:
                return R.string.dark_blue;
            case 4:
                return R.string.silver;
            case 5:
                return R.string.blue;
            case 6:
                return R.string.yelow;
            case 7:
                return R.string.dark_green;
            case 8:
                return R.string.dark_red;
            case 9:
                return R.string.pink;
            case 10:
                return R.string.sky;
            case 11:
                return R.string.verde_agua;
            case 12:
                return R.string.laranja;
            case 13:
                return R.string.marron;
            case 14:
                return R.string.verde_musgo;
            case 15:
                return R.string.verde_folha;

        }
        return R.string.red;
    }

    public int circleColor(int i){
        switch (i) {
            case 1:
                return R.drawable.circle_red;
            case 2:
                return R.drawable.circle_green;
            case 3:
                return R.drawable.circle_pink;
            case 4:
                return R.drawable.circle_silver;
            case 5:
                return R.drawable.circle_blue;
            case 6:
                return R.drawable.circle_yelow;
            case 7:
                return R.drawable.circle_darkgreen;
            case 8:
                return R.drawable.circle_darkred;
            case 9:
                return R.drawable.circle_darkblue;
        }
        return R.drawable.circle_red;
    }




    private GradientDrawable shape;
    public Drawable customView(int gradientDrawable,int width,int height ,int backgroundColor) {
        shape = new GradientDrawable();
        shape.setShape(gradientDrawable);
        shape.setColor(Color.parseColor(activity.getResources().getString(colorString(backgroundColor))));
        shape.setSize(width,height);
        return shape;
    }

}
