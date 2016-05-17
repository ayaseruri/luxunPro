package pro.luxun.luxunanimation.utils;

import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wufeiyang on 16/5/10.
 */
public class Utils {

    public static String num2Str(int num){
        switch (num){
            case 0:
                return "零";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "七";
            case 8:
                return "八";
            case 9:
                return "九";
            default:
                return "";
        }
    }

    public static String videoTimeFormat(long l){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Date date = new Date();
        date.setTime(l);
        return simpleDateFormat.format(date);
    }

    public static boolean isColorDark(int color){
        double darkness = 1-(0.299* Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }
}
