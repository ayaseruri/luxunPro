package pro.luxun.luxunanimation.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;
import pro.luxun.luxunanimation.utils.GridSpacingItemDecoration;
import pro.luxun.luxunanimation.utils.JsonUtils;
import pro.luxun.luxunanimation.utils.MainJasonHelper;
import pro.luxun.luxunanimation.utils.UserInfoHelper;
import pro.luxun.luxunanimation.view.view.MFAnimationItem;
import pro.luxun.luxunanimation.view.view.MFAnimationItem_;
import ykooze.ayaseruri.codesslib.others.ToastUtils;
import ykooze.ayaseruri.codesslib.rx.RxUtils;
import ykooze.ayaseruri.codesslib.ui.LocalDisplay;

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

        if(UserInfoHelper.isLogin(mActivity)){
            mEmptyText.setVisibility(View.GONE);
            ArrayList<String> bangumis = MainJasonHelper.getBangumisCache(mActivity);
            if(null != bangumis){
                List<MainJson.UpdatingEntity> updatingEntities = JsonUtils.animationNames2Infos(mActivity, bangumis);
                mAdapter.refresh(updatingEntities);
            }
        }else {
            mEmptyText.setVisibility(View.VISIBLE);
            mEmptyText.setText("尚未登录…");
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
        if(UserInfoHelper.isLogin(mActivity)){
            if(null == mEmptyText){
                return;
            }
            mEmptyText.setVisibility(View.GONE);

            if(null == mAdapter){
                return;
            }
            RetrofitClient.getApiService().getBangumis(RetrofitClient.URL_BANGUMI)
                    .compose(RxUtils.<ArrayList<String>>applySchedulers())
                    .subscribe(new Observer<ArrayList<String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ArrayList<String> list) {
                            MainJasonHelper.saveBangumis(mActivity, list);
                            mAdapter.refresh(JsonUtils.animationNames2Infos(mActivity, list));
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            ToastUtils.showTost(mActivity, ToastUtils.TOAST_ALERT, "加载失败…");
                            if(null != refreshComplete){
                                refreshComplete.onComplete();
                            }
                        }

                        @Override
                        public void onComplete() {
                            if(null != refreshComplete){
                                refreshComplete.onComplete();
                            }
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
