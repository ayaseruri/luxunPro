package pro.luxun.luxunanimation.utils;

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
}
