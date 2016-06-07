package pro.luxun.luxunanimation.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;
import pro.luxun.luxunanimation.utils.GridSpacingItemDecoration;
import pro.luxun.luxunanimation.utils.JsonUtils;
import pro.luxun.luxunanimation.utils.LocalDisplay;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.view.view.MFAnimationItem;
import pro.luxun.luxunanimation.view.view.MFAnimationItem_;
import rx.Subscriber;

/**
 * Created by wufeiyang on 16/5/21.
 */
@EFragment(R.layout.fragment_list)
public class BangumiFragment extends BaseFragment {

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;

    private BaseRecyclerAdapter<MainJson.UpdatingEntity, MFAnimationItem> mAdapter;

    @AfterViews
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
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh(null);
    }

    public void refresh(final IOnRefreshComplete refreshComplete){
        RetrofitClient.getApiService().getBangumis(RetrofitClient.URL_BANGUMI).compose(RxUtils.<List<String>>applySchedulers())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        if(null != refreshComplete){
                            refreshComplete.onComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> list) {
                        List<MainJson.UpdatingEntity> updatingEntities =
                                JsonUtils.animationNames2Infos(list);
                        mAdapter.refresh(updatingEntities);
                    }
                });
    }

    public interface IOnRefreshComplete{
        void onComplete();
    }
}
