package pro.luxun.luxunanimation.view.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.utils.UIUtils;

/**
 * Created by wufeiyang on 16/5/7.
 */
@EViewGroup
public class TranslucentStatusBarLinearLayout extends LinearLayout{
    public TranslucentStatusBarLinearLayout(Context context) {
        super(context);
    }

    public TranslucentStatusBarLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setOrientation(VERTICAL);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setFitsSystemWindows(false);

            Context context = getContext();
            if(context instanceof Activity){
                ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                ImageView statusBar = new ImageView(context);
                statusBar.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                addView(statusBar, 0, new LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.getStatusBarHeight(context)));
            }
        }
    }
}
