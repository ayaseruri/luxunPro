package pro.luxun.luxunanimation.view.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.Comment;
import pro.luxun.luxunanimation.bean.PostLikeComment;
import pro.luxun.luxunanimation.global.MApplication;
import pro.luxun.luxunanimation.net.ApiService;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.StartUtils;
import pro.luxun.luxunanimation.utils.UserInfoHelper;
import pro.luxun.luxunanimation.utils.Utils;
import ykooze.ayaseruri.codesslib.others.ToastUtils;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

/**
 * Created by wufeiyang on 16/5/19.
 */
@EViewGroup(R.layout.item_comment_list)
public class CommentItem extends RelativeLayout{

    @ViewById(R.id.avatar)
    RoundedImageView mAvatar;
    @ViewById(R.id.time)
    TextView mTime;
    @ViewById(R.id.content)
    TextView mContent;
    @ViewById(R.id.like_count)
    TextView mLikeCount;
    @ViewById(R.id.rating_bar)
    RatingBar mRatingBar;
    @ViewById(R.id.heart)
    ImageView mHeartView;
    @App
    MApplication mMApplication;

    private ApiService mApiService;

    public CommentItem(Context context) {
        super(context);
    }

    public CommentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);

        mApiService = RetrofitClient.getApiService();
    }

    public void bind(final Comment comment, boolean isLiked){
        Glide.with(getContext()).load(comment.getUser().getAvatar().replace("50","180")).into(mAvatar);
        mRatingBar.setRating(Float.valueOf(comment.getRate()));
        mContent.setText(comment.getText());
        mTime.setText(Utils.commentTimeFormat(Long.valueOf(comment.getCreated())));
        mLikeCount.setText(comment.getLiked());

        mHeartView.setSelected(isLiked);
        mHeartView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!UserInfoHelper.isLogin(getContext())){
                    Snackbar.make(getRootView(), "登录之后才能喜欢…", Snackbar.LENGTH_LONG).setAction("登录", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StartUtils.startMainActivity(getContext(), 0);
                        }
                    }).show();
                    return;
                }

                mApiService.likeComment(RetrofitClient.URL_LIKE, Utils.str2RequestBody("cid"), Utils.str2RequestBody(comment.getCid()))
                        .compose(RxUtils.<PostLikeComment>applySchedulers())
                        .subscribe(new Observer<PostLikeComment>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mHeartView.setSelected(true);
                            }

                            @Override
                            public void onNext(PostLikeComment postLikeComment) {
                                if(TextUtils.isEmpty(postLikeComment.getUid())){
                                    mHeartView.setSelected(false);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                mHeartView.setSelected(false);
                                ToastUtils.showTost(getContext(), ToastUtils.TOAST_ALERT, "喜欢操作失败");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }
}
