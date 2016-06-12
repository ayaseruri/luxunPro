package pro.luxun.luxunanimation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.Comment;
import pro.luxun.luxunanimation.global.MApplication;
import pro.luxun.luxunanimation.global.MApplication_;
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
public class LikeCommentFragment extends BaseFragment {

    private TextView mEmptyText;
    private RecyclerView mRecyclerView;
    private View mRootView;

    private ApiService mApiService;
    private BaseRecyclerAdapter mAdapter;


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
                            MApplication_.getInstance().showToast("加载失败…", MApplication.TOAST_ALERT);
                            if(null != refreshComplete){
                                refreshComplete.onComplete();
                            }
                        }

                        @Override
                        public void onNext(List<Comment> comments) {
                            mAdapter.refresh(comments);
                        }
                    });
        }else {
            if(null != refreshComplete){
                refreshComplete.onComplete();
            }

            if(null == mEmptyText){
                return;
            }
            mEmptyText.setVisibility(View.VISIBLE);
            mEmptyText.setText("尚未登录…");
        }
    }

    public interface IOnRefreshComplete{
        void onComplete();
    }
}
