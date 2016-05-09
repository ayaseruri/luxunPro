package pro.luxun.luxunanimation.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;

/**
 * Created by wufeiyang on 16/5/8.
 */
public class MainJsonUtils {

    public static final int TYPE_BOTTOM = 10080;
    public static final int TYPE_HEAD = 10081;

    public static List<MainJson.UpdatingEntity> formatMF(Context context, MainJson mainJson){
        List<MainJson.UpdatingEntity> updatingEntities = new ArrayList<>();
        updatingEntities.addAll(mainJson.getUpdating());

        MainJson.UpdatingEntity updatingEntity = new MainJson.UpdatingEntity();
        updatingEntity.setType(TYPE_BOTTOM);

        int setCount = 0;
        for(MainJson.UpdatingEntity temp: mainJson.getUpdating()){
            setCount = setCount + temp.getSets().size();
        }

        updatingEntity.setTitle(String.format(context.getResources().getString(R.string.bottom_mf)
                , mainJson.getUpdating().size(), setCount));
        updatingEntities.add(updatingEntity);

        for (MainJson.QuarsEntity quarsEntity : mainJson.getQuars()){
            updatingEntity = new MainJson.UpdatingEntity();
            updatingEntity.setType(TYPE_HEAD);
            updatingEntity.setTitle(MainJsonUtils.num2Season(quarsEntity.getQuarter()));

            updatingEntities.add(updatingEntity);
            updatingEntities.addAll(quarsEntity.getBangumis());

            updatingEntity = new MainJson.UpdatingEntity();
            updatingEntity.setType(TYPE_BOTTOM);
            setCount = 0;
            for(MainJson.UpdatingEntity temp : quarsEntity.getBangumis()){
                setCount = setCount + temp.getSets().size();
            }

            updatingEntity.setTitle(String.format(context.getResources().getString(R.string.bottom_mf)
                    , quarsEntity.getBangumis().size(), setCount));
            updatingEntities.add(updatingEntity);
        }
        return updatingEntities;
    }



    private static String num2Season(String numStr){
        StringBuilder time = new StringBuilder();
        int num = Integer.valueOf(numStr);

        int season = num % 100;

        num = (num - season) / 100;
        time.append(num2Str((num - num % 10) / 10));
        time.append(num2Str(num % 10));
        time.append("年");

        switch (season){
            case 1:
                time.append(" 冬 ");
                break;
            case 4:
                time.append(" 春 ");
                break;
            case 7:
                time.append(" 夏 ");
                break;
            case 10:
                time.append(" 秋 ");
                break;
            default:
                break;
        }
        return time.toString();
    }

    private static String num2Str(int num){
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
}
