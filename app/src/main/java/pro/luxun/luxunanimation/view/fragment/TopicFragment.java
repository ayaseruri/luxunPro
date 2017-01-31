package pro.luxun.luxunanimation.view.fragment;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.TopicJson;
import pro.luxun.luxunanimation.global.MApplication;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;
import pro.luxun.luxunanimation.presenter.presenter.TopicFragmentPresenter;
import pro.luxun.luxunanimation.utils.GridSpacingItemDecoration;
import pro.luxun.luxunanimation.view.activity.INetCacheData;
import pro.luxun.luxunanimation.view.view.TopicItem;
import pro.luxun.luxunanimation.view.view.TopicItem_;
import ykooze.ayaseruri.codesslib.others.ToastUtils;
import ykooze.ayaseruri.codesslib.ui.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/16.
 */
@EFragment(R.layout.fragment_topic)
public class TopicFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        INetCacheData<ArrayList<TopicJson>>{

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;
    @ViewById(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @App
    MApplication mMApplication;
    @StringRes(R.string.net_error)
    String mNetError;

    private BaseRecyclerAdapter mBaseRecyclerAdapter;
    private TopicFragmentPresenter mTopicFragmentPresenter;

    @AfterViews
    void init(){
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(this);

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
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, LocalDisplay.dp2px(4), true));

        mTopicFragmentPresenter = new TopicFragmentPresenter(mActivity, this);
        mTopicFragmentPresenter.getTopicJsonNetSilent();
        mTopicFragmentPresenter.getTopicJsonCache();
    }

    @Override
    public void onStartGetJsonNet() {

    }

    @Override
    public void onGetJsonSuccessNet(ArrayList<TopicJson> topicJsons) {
        mRefreshLayout.setRefreshing(false);
        mBaseRecyclerAdapter.refresh(topicJsons);
    }

    @Override
    public void onGetJsonErrorNet() {
        mRefreshLayout.setRefreshing(false);
        ToastUtils.showTost(mActivity, ToastUtils.TOAST_ALERT, mNetError);
    }

    @Override
    public void onGetJsonCacheSuccess(ArrayList<TopicJson> topicJsons) {
        mBaseRecyclerAdapter.refresh(topicJsons);
    }

    @Override
    public void onGetJsonCacheFailed() {

    }

    @Override
    public void onRefresh() {
        mTopicFragmentPresenter.getTopicJsonNet();
    }
}
