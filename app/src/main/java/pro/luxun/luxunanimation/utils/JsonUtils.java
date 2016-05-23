package pro.luxun.luxunanimation.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.Danmaku;
import pro.luxun.luxunanimation.bean.MainJson;

/**
 * Created by wufeiyang on 16/5/8.
 */
public class JsonUtils {

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
            updatingEntity.setTitle(JsonUtils.num2Season(quarsEntity.getQuarter()));

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

    public static ArrayList<MainJson.UpdatingEntity> animationNames2Infos(List<String> names){
        ArrayList<MainJson.UpdatingEntity> entities = new ArrayList<>();
        MainJson mainJson = MainJasonHelper.getMainJsonCache();

        for(String name : names){
            for(MainJson.UpdatingEntity updatingEntity : mainJson.getUpdating()){
                if(updatingEntity.getTitle().equals(name)){
                    entities.add(updatingEntity);
                    break;
                }
            }

            for(MainJson.QuarsEntity quarsEntity : mainJson.getQuars()){
                for(MainJson.UpdatingEntity updatingEntity : quarsEntity.getBangumis()){
                    if(updatingEntity.getTitle().equals(name)){
                        entities.add(updatingEntity);
                        break;
                    }
                }
            }
        }

        return entities;
    }

    public static List<Danmaku> parserDanmaku(List<List> danmuList){

        List<Danmaku> danmakus = new ArrayList<>();

        for (List list : danmuList){
            Danmaku danmaku = new Danmaku();
            danmaku.setStart(Double.valueOf(list.get(0).toString()));
            danmaku.setText(list.get(2).toString());
            danmaku.setColor(list.get(3).toString());
            danmaku.setType(list.get(5).toString());
            danmaku.setY(Integer.valueOf(list.get(4).toString()));
            danmakus.add(danmaku);
        }
        return danmakus;
    }

    private static String num2Season(String numStr){
        StringBuilder time = new StringBuilder();
        int num = Integer.valueOf(numStr);

        int season = num % 100;

        num = (num - season) / 100;
        time.append(Utils.num2Str((num - num % 10) / 10));
        time.append(Utils.num2Str(num % 10));
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


}
