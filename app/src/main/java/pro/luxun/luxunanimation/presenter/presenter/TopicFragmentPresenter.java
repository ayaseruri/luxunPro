package pro.luxun.luxunanimation.presenter.presenter;

import java.util.ArrayList;
import java.util.List;

import pro.luxun.luxunanimation.bean.TopicJson;
import pro.luxun.luxunanimation.model.INetCacheModel;
import pro.luxun.luxunanimation.model.TopicFragmentModel;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.SerializeUtils;
import pro.luxun.luxunanimation.view.activity.INetCacheData;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wufeiyang on 16/5/16.
 */
public class TopicFragmentPresenter {

    private INetCacheData<List<TopicJson>> mTopicFragment;
    private INetCacheModel<List<TopicJson>> mTopicModel;

    public TopicFragmentPresenter(INetCacheData<List<TopicJson>> topicFragment) {
        mTopicFragment = topicFragment;
        mTopicModel = new TopicFragmentModel();
    }

    public void getTopicJsonNetSilent(){
        mTopicModel.getJsonNet().subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<TopicJson>>() {
                    @Override
                    public void call(List<TopicJson> topicJsons) {
                        SerializeUtils.serialization(SerializeUtils.TAG_TOPIC_JSON, topicJsons);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public void getTopicJsonNet(){
        mTopicModel.getJsonNet().compose(RxUtils.<List<TopicJson>>applySchedulers())
                .subscribe(new Subscriber<List<TopicJson>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mTopicFragment.onGetJsonErrorNet();
                    }

                    @Override
                    public void onNext(List<TopicJson> topicJsons) {
                        mTopicFragment.onGetJsonSuccessNet(topicJsons);
                    }

                    @Override
                    public void onStart() {
                        mTopicFragment.onStartGetJsonNet();
                    }
                });
    }

    public void getTopicJsonCache(){
        ArrayList<TopicJson> topicJson = (ArrayList<TopicJson>) SerializeUtils.deserialization(SerializeUtils.TAG_TOPIC_JSON, true);
        if(null == topicJson){
            mTopicModel.getJsonNet();
        }else {
            mTopicFragment.onGetJsonCacheSuccess(topicJson);
        }
    }
}
