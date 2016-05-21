package pro.luxun.luxunanimation.utils;

import android.graphics.Color;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public static String commentTimeFormat(long l){
        StringBuilder formatedTime = new StringBuilder();
        int dif = (int) (System.currentTimeMillis() - l) / 1000;
        if(dif < 5){
            formatedTime.append("刚刚");
        }else if(dif < 60){
            formatedTime.append(dif).append("秒前");
        }else if(dif < (60 * 60)){
            formatedTime.append(dif).append("分前");
        }else if(dif < (60 * 60 * 24)){
            formatedTime.append(dif).append("时前");
        }else if(dif < (60 * 60 * 24 * 7)){
            formatedTime.append(dif).append("天前");
        }else if(dif < (60 * 60 * 24 * 7 * 2)){
            formatedTime.append(dif).append("周前");
        }else if(dif < (60 * 60 * 24 * 365)){
            SimpleDateFormat sf = new SimpleDateFormat("MM月dd日");
            Date date = new Date();
            date.setTime(l);
            formatedTime.append(sf.format(date));
        }else {
            SimpleDateFormat sf = new SimpleDateFormat("YYYY年MM月dd日");
            Date date = new Date();
            date.setTime(l);
            formatedTime.append(sf.format(date));
        }
        return formatedTime.toString();
    }

    public static boolean isColorDark(int color){
        double darkness = 1-(0.299* Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }

    public static String encodeURIComponent(String s){
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
