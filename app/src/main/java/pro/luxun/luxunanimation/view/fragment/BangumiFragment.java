package pro.luxun.luxunanimation.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.view.view.MFAnimationItem_;
import rx.Subscriber;

/**
 * Created by wufeiyang on 16/5/21.
 */
@EFragment(R.layout.fragment_list)
public class BangumiFragment extends BaseFragment {

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;

    private BaseRecyclerAdapter mAdapter;

    @AfterViews
    void init(){
        mAdapter = new BaseRecyclerAdapter() {
            @Override
            protected View onCreateItemView(ViewGroup parent, int viewType) {
                return MFAnimationItem_.build(parent.getContext());
            }

            @Override
            protected void onBindView(View view, Object o) {

            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }
        };

        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
    }

    @Override
    public void onResume() {
        super.onResume();
        RetrofitClient.getApiService().getBangumis(RetrofitClient.URL_BANGUMI).compose(RxUtils.applySchedulers())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }
}
