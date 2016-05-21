package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.Comment;
import pro.luxun.luxunanimation.utils.Utils;

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
    }

    public void bind(Comment comment){
        Glide.with(getContext()).load(comment.getUser().getAvatar()).into(mAvatar);
        mRatingBar.setRating(Float.valueOf(comment.getRate()));
        mContent.setText(comment.getText());
        mTime.setText(Utils.commentTimeFormat(Long.valueOf(comment.getCreated())));
        mLikeCount.setText(comment.getLiked());
    }
}
