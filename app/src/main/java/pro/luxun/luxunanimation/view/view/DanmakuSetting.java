package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import org.androidannotations.annotations.EBean;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.utils.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/24.
 */
@EBean
public class DanmakuSetting {

    private PopupWindow mPopupWindow;
    private Context mContext;
    private int mPopHeight, mPopWidth;

    public DanmakuSetting(Context context) {
        mContext = context;
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.danmaku_setting, null);
        contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        mPopWidth = contentView.getMeasuredWidth();
        mPopHeight = contentView.getMeasuredHeight();

        mPopupWindow = new PopupWindow(contentView, mPopWidth, mPopHeight, true);
    }

    public void show(View athor){
        mPopupWindow.showAsDropDown(athor, - mPopWidth/2, - mPopHeight - LocalDisplay.dp2px(22));
    }
}
