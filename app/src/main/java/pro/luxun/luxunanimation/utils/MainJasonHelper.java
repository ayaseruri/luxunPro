package pro.luxun.luxunanimation.utils;

import java.util.ArrayList;

import android.content.Context;
import io.reactivex.Observable;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.net.RetrofitClient;
import ykooze.ayaseruri.codesslib.io.SerializeUtils;

/**
 * Created by wufeiyang on 16/5/16.
 */
public class MainJasonHelper {

    private static final String TAG_MAIN_JSON = "main_json";
    private static final String TAG_BANGUMIS = "bangumis";

    private static MainJson sMainJson;
    private static ArrayList<String> sBangumis;

    public static void saveMainJson(Context context, final MainJson mainJson){
        sMainJson = mainJson;
        SerializeUtils.serialization(context, TAG_MAIN_JSON, mainJson);
    }

    public static void saveBangumis(Context context, final ArrayList<String> bangumis){
        sBangumis = bangumis;
        SerializeUtils.serialization(context, TAG_BANGUMIS, bangumis);
    }

    public static MainJson getMainJsonCache(Context context) {
        if(null == sMainJson){
            synchronized(MainJasonHelper.class){
                if(null == sMainJson){
                    sMainJson = (MainJson) SerializeUtils.deserializationSync(context, TAG_MAIN_JSON, true);
                }
            }
        }
        return sMainJson;
    }

    public static ArrayList<String> getBangumisCache(Context context){
        if(null == sBangumis){
            synchronized(MainJasonHelper.class){
                if(null == sBangumis){
                    sBangumis = (ArrayList<String>) SerializeUtils.deserializationSync(context, TAG_BANGUMIS, true);
                }
            }
        }
        return sBangumis;
    }

    public static Observable<MainJson> getMainJsonNet(){
        return RetrofitClient.getApiService().getMainJson(RetrofitClient.URL_MAIN_JSON);
    }

    public static Observable<ArrayList<String>> getBangumisNet(){
        return RetrofitClient.getApiService().getBangumis(RetrofitClient.URL_BANGUMI);
    }
}
