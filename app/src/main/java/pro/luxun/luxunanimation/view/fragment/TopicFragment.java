package pro.luxun.luxunanimation.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.TopicJson;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;
import pro.luxun.luxunanimation.presenter.presenter.TopicFragmentPresenter;
import pro.luxun.luxunanimation.view.activity.INetCacheData;
import pro.luxun.luxunanimation.view.view.TopicItem;
import pro.luxun.luxunanimation.view.view.TopicItem_;

/**
 * Created by wufeiyang on 16/5/16.
 */
@EFragment(R.layout.fragment_topic)
public class TopicFragment extends BaseFragment implements INetCacheData<List<TopicJson>>{

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;

    private BaseRecyclerAdapter mBaseRecyclerAdapter;
    private TopicFragmentPresenter mTopicFragmentPresenter;

    @AfterViews
    void init(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mBaseRecyclerAdapter = new BaseRecyclerAdapter<TopicJson, TopicItem>() {
            @Override
            protected TopicItem onCreateItemView(ViewGroup parent, int viewType) {
                return TopicItem_.build(parent.getContext());
            }

            @Override
            protected void onBindView(TopicItem topicItem, TopicJson topicJson) {
                topicItem.bind(topicJson);
            }
        };
        mRecyclerView.setAdapter(mBaseRecyclerAdapter);

        mTopicFragmentPresenter = new TopicFragmentPresenter(this);
        mTopicFragmentPresenter.getTopicJsonNetSilent();
        mTopicFragmentPresenter.getTopicJsonCache();
    }

    @Override
    public void onStartGetJsonNet() {

    }

    @Override
    public void onGetJsonSuccessNet(List<TopicJson> topicJsons) {
        mBaseRecyclerAdapter.refresh(topicJsons);
    }

    @Override
    public void onGetJsonErrorNet() {

    }

    @Override
    public void onGetJsonCacheSuccess(List<TopicJson> topicJsons) {
        mBaseRecyclerAdapter.refresh(topicJsons);
    }

    @Override
    public void onGetJsonCacheFailed() {

    }
}
