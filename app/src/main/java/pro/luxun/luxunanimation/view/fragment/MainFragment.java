package pro.luxun.luxunanimation.view.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.presenter.adapter.MainFragmentAdapter;
import pro.luxun.luxunanimation.utils.MainJasonHelper;
import pro.luxun.luxunanimation.utils.RxUtils;
import rx.Subscriber;

/**
 * Created by wufeiyang on 16/5/7.
 */

@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;
    @ViewById(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    private MainFragmentAdapter mAdapter;
    private GridLayoutManager mLinearLayoutManager;

    @AfterViews
    void init(){
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.colorPrimary));

        mAdapter = new MainFragmentAdapter(mActivity);
        mLinearLayoutManager = new GridLayoutManager(mActivity, 3);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mLinearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanCount = mLinearLayoutManager.getSpanCount();
                if(0 == position){
                    return spanCount;
                }else {
                    return mAdapter.isNormalItem(position) ? 1 : spanCount;
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        MainJasonHelper.getMainJsonNet()
                .compose(RxUtils.<MainJson>applySchedulers())
                .subscribe(new Subscriber<MainJson>() {
                    @Override
                    public void onCompleted() {
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(MainJson mainJson) {
                        MainJasonHelper.saveMainJson(mainJson);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(null != mAdapter){
            mAdapter.notifyDataSetChanged();
        }
    }
}
