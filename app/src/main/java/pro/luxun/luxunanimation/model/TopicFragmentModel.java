package pro.luxun.luxunanimation.model;

import java.util.ArrayList;

import android.content.Context;
import io.reactivex.Observable;
import pro.luxun.luxunanimation.bean.TopicJson;
import pro.luxun.luxunanimation.net.RetrofitClient;
import ykooze.ayaseruri.codesslib.io.SerializeUtils;

/**
 * Created by wufeiyang on 16/5/16.
 */
public class TopicFragmentModel implements INetCacheModel<ArrayList<TopicJson>> {

    private static final String TAG_TOPIC_JSON = "topic_json";

    @Override
    public Observable<ArrayList<TopicJson>> getJsonNet() {
        return RetrofitClient.getApiService().getTopicJson(RetrofitClient.URL_TOPIC_JSON);
    }

    @Override
    public ArrayList<TopicJson> getJsonCache(Context context) {
        return (ArrayList<TopicJson>) SerializeUtils.deserializationSync(context, TAG_TOPIC_JSON, true);
    }
}
