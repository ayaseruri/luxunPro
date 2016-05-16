package pro.luxun.luxunanimation.model;

import java.util.ArrayList;
import java.util.List;

import pro.luxun.luxunanimation.bean.TopicJson;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.SerializeUtils;
import rx.Observable;

/**
 * Created by wufeiyang on 16/5/16.
 */
public class TopicFragmentModel implements INetCacheModel<List<TopicJson>> {

    @Override
    public Observable<List<TopicJson>> getJsonNet() {
        return RetrofitClient.getApiService().getTopicJson(RetrofitClient.URL_TOPIC_JSON);
    }

    @Override
    public List<TopicJson> getJsonCache() {
        return (ArrayList<TopicJson>) SerializeUtils.deserialization(SerializeUtils.TAG_TOPIC_JSON, true);
    }
}
