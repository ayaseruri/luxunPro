package pro.luxun.luxunanimation.view.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.utils.TranslucentStatusHelper;

/**
 * Created by wufeiyang on 16/5/7.
 */
@EViewGroup
public class TranslucentStatusBarLinearLayout extends LinearLayout{

    private int mStatusColor;

    public TranslucentStatusBarLinearLayout(Context context) {
        super(context);
    }

    public TranslucentStatusBarLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TranslucentStatusBarLinearLayout);
        mStatusColor = array.getColor(R.styleable.TranslucentStatusBarLinearLayout_status_bar_color, ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        array.recycle();
    }

    @AfterViews
    void init(){
        setOrientation(VERTICAL);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Context context = getContext();
            if(context instanceof Activity){
                setFitsSystemWindows(false);
                TranslucentStatusHelper.translucentStatus((Activity) context);
                ImageView statusBar = new ImageView(context);
                statusBar.setBackgroundColor(mStatusColor);
                addView(statusBar, 0, new LayoutParams(LayoutParams.MATCH_PARENT, TranslucentStatusHelper.getStatusBarHeight(context)));
            }
        }
    }
}
