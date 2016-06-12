package pro.luxun.luxunanimation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.global.MApplication;
import pro.luxun.luxunanimation.global.MApplication_;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;
import pro.luxun.luxunanimation.utils.GridSpacingItemDecoration;
import pro.luxun.luxunanimation.utils.JsonUtils;
import pro.luxun.luxunanimation.utils.LocalDisplay;
import pro.luxun.luxunanimation.utils.MainJasonHelper;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.UserInfoHelper;
import pro.luxun.luxunanimation.view.view.MFAnimationItem;
import pro.luxun.luxunanimation.view.view.MFAnimationItem_;
import rx.Subscriber;

/**
 * Created by wufeiyang on 16/5/21.
 */
public class BangumiFragment extends BaseFragment {

    private TextView mEmptyText;
    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<MainJson.UpdatingEntity, MFAnimationItem> mAdapter;

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == mRootView){
            mRootView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_list, container, false);

            mEmptyText = (TextView) mRootView.findViewById(R.id.empty_text);
            mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler);

            init();
        }

        return mRootView;
    }

    void init(){
        mAdapter = new BaseRecyclerAdapter<MainJson.UpdatingEntity, MFAnimationItem>() {

            @Override
            protected MFAnimationItem onCreateItemView(ViewGroup parent, int viewType) {
                return MFAnimationItem_.build(parent.getContext());
            }

            @Override
            protected void onBindView(MFAnimationItem mfAnimationItem, MainJson.UpdatingEntity s) {
                s.setSub(true);
                mfAnimationItem.bind(s, "");
            }
        };

        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, LocalDisplay.dp2px(4), true));

        if(UserInfoHelper.isLogin()){
            mEmptyText.setVisibility(View.GONE);
            ArrayList<String> bangumis = MainJasonHelper.getBangumisCache();
            if(null != bangumis){
                List<MainJson.UpdatingEntity> updatingEntities = JsonUtils.animationNames2Infos(bangumis);
                mAdapter.refresh(updatingEntities);
            }
        }else {
            mEmptyText.setVisibility(View.VISIBLE);
            mEmptyText.setText("没有数据…");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            refresh(null);
        }
    }

    public void refresh(final IOnRefreshComplete refreshComplete){
        if(UserInfoHelper.isLogin()){
            if(null == mEmptyText){
                return;
            }
            mEmptyText.setVisibility(View.GONE);

            if(null == mAdapter){
                return;
            }
            RetrofitClient.getApiService().getBangumis(RetrofitClient.URL_BANGUMI)
                    .compose(RxUtils.<ArrayList<String>>applySchedulers())
                    .subscribe(new Subscriber<ArrayList<String>>() {
                        @Override
                        public void onCompleted() {
                            if(null != refreshComplete){
                                refreshComplete.onComplete();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            MApplication_.getInstance().showToast("加载失败…", MApplication.TOAST_ALERT);
                            if(null != refreshComplete){
                                refreshComplete.onComplete();
                            }
                        }

                        @Override
                        public void onNext(ArrayList<String> list) {
                            MainJasonHelper.saveBangumis(list);
                            List<MainJson.UpdatingEntity> updatingEntities =
                                    JsonUtils.animationNames2Infos(list);
                            mAdapter.refresh(updatingEntities);
                        }
                    });
        }else {
            if(null == mEmptyText){
                return;
            }
            mEmptyText.setVisibility(View.VISIBLE);

            if(null != refreshComplete){
                refreshComplete.onComplete();
            }
        }
    }

    public interface IOnRefreshComplete{
        void onComplete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRootView = null;
    }
}
