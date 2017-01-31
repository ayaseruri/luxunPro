package pro.luxun.luxunanimation.presenter.presenter;

import java.util.ArrayList;

import android.content.Context;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pro.luxun.luxunanimation.bean.TopicJson;
import pro.luxun.luxunanimation.model.INetCacheModel;
import pro.luxun.luxunanimation.model.TopicFragmentModel;
import pro.luxun.luxunanimation.view.activity.INetCacheData;
import ykooze.ayaseruri.codesslib.io.SerializeUtils;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

/**
 * Created by wufeiyang on 16/5/16.
 */
public class TopicFragmentPresenter {

    private static final String TAG_TOPIC_JSON = "topic_json";

    private INetCacheData<ArrayList<TopicJson>> mTopicFragment;
    private INetCacheModel<ArrayList<TopicJson>> mTopicModel;
    private Context mContext;

    public TopicFragmentPresenter(Context context, INetCacheData<ArrayList<TopicJson>> topicFragment) {
        mTopicFragment = topicFragment;
        mTopicModel = new TopicFragmentModel();
        mContext = context;
    }

    public void getTopicJsonNetSilent(){
        mTopicModel.getJsonNet()
                .compose(RxUtils.<ArrayList<TopicJson>>applySchedulers())
                .compose(RxUtils.<ArrayList<TopicJson>>applyCache(mContext, TAG_TOPIC_JSON))
                .subscribe();

    }

    public void getTopicJsonNet(){
        mTopicModel.getJsonNet()
                .compose(RxUtils.<ArrayList<TopicJson>>applySchedulers())
                .subscribe(new Observer<ArrayList<TopicJson>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mTopicFragment.onStartGetJsonNet();
                    }

                    @Override
                    public void onNext(ArrayList<TopicJson> topicJsons) {
                        mTopicFragment.onGetJsonSuccessNet(topicJsons);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTopicFragment.onGetJsonErrorNet();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getTopicJsonCache(){
        ArrayList<TopicJson> topicJson =
                (ArrayList<TopicJson>) SerializeUtils
                        .deserializationSync(mContext , TAG_TOPIC_JSON, true);
        if(null == topicJson){
            getTopicJsonNet();
        }else {
            mTopicFragment.onGetJsonCacheSuccess(topicJson);
        }
    }
}
