package br.com.v8developmentstudio.rccguarulhos.util;

import br.com.v8developmentstudio.rccguarulhos.R;

/**
 * Created by cleiton.dantas on 02/12/2016.
 */

public class CircleColorDrawable {

    public static int CirclesColor(int i){
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

}
