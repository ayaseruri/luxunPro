package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.net.ApiService;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.Utils;
import rx.Subscriber;

/**
 * Created by wufeiyang on 16/5/12.
 */
@EViewGroup(R.layout.view_video_comment)
public class VideoComment extends LinearLayout{

    @ViewById(R.id.rating_bar)
    RatingBar mStarBar;
    @ViewById(R.id.comment_et)
    EditText mCommentET;
    @ViewById(R.id.rating_bar)
    RatingBar mRatingBar;
    @ViewById(R.id.progress)
    ProgressWheel mProgressWheel;

    private ApiService mApiService;
    private String mCommentUrl;
    private int mCur = 0;

    public VideoComment(Context context) {
        super(context);
    }

    public VideoComment(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setOrientation(VERTICAL);
        mProgressWheel.setVisibility(GONE);

        mApiService = RetrofitClient.getApiService();
    }

    @Click(R.id.submit_btn)
    void onSubmit(){
        if(TextUtils.isEmpty(mCommentUrl) || -1 ==mCur){
            throw new IllegalArgumentException("需要调用initComment来初始化评论url参数");
        }

        String comment = mCommentET.getText().toString();
        if(TextUtils.isEmpty(comment)){
            Snackbar.make(this, "番评不能为空", Snackbar.LENGTH_LONG).show();
            return;
        }

        mApiService.submitComment(RetrofitClient.URL_COMMENT, (int) mRatingBar.getRating()
                , mCur, 0.00, comment).compose(RxUtils.applySchedulers())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        mProgressWheel.setVisibility(GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(VideoComment.this, "评论出错", Snackbar.LENGTH_LONG).show();
                        mProgressWheel.setVisibility(GONE);
                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onStart() {
                        mProgressWheel.setVisibility(VISIBLE);
                    }
                });
    }


    public void initComment(String name){
        mCommentUrl = RetrofitClient.URL_COMMENT + Utils.encodeURIComponent("lx:" + name + mCur);
    }
}
