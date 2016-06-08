package pro.luxun.luxunanimation.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.Comment;
import pro.luxun.luxunanimation.global.MApplication;
import pro.luxun.luxunanimation.net.ApiService;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.UserInfoHelper;
import pro.luxun.luxunanimation.view.view.CommentItem;
import pro.luxun.luxunanimation.view.view.CommentItem_;
import rx.Subscriber;

/**
 * Created by wufeiyang on 16/5/21.
 */
@EFragment(R.layout.fragment_list)
public class LikeCommentFragment extends BaseFragment {

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;
    @App
    MApplication mMApplication;

    @StringRes(R.string.net_error)
    String mNetErrorStr;

    private ApiService mApiService;
    private BaseRecyclerAdapter mAdapter;

    @AfterViews
    void init(){
        mApiService = RetrofitClient.getApiService();
        mAdapter = new BaseRecyclerAdapter<Comment, CommentItem>() {
            @Override
            protected CommentItem onCreateItemView(ViewGroup parent, int viewType) {
                return CommentItem_.build(parent.getContext());
            }

            @Override
            protected void onBindView(CommentItem commentItem, Comment comment) {
                commentItem.bind(comment, true);
            }
        };


        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh(null);
    }

    public void refresh(final IOnRefreshComplete refreshComplete){
        if(UserInfoHelper.isLogin()){
            mApiService.getlikeComment(RetrofitClient.URL_LIKE_COMMENT).compose(RxUtils.<List<Comment>>applySchedulers())
                    .subscribe(new Subscriber<List<Comment>>() {
                        @Override
                        public void onCompleted() {
                            if(null != refreshComplete){
                                refreshComplete.onComplete();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            mMApplication.showToast(mNetErrorStr, MApplication.TOAST_ALERT);
                        }

                        @Override
                        public void onNext(List<Comment> comments) {
                            mAdapter.refresh(comments);
                        }
                    });
        }
    }

    public interface IOnRefreshComplete{
        void onComplete();
    }
}
