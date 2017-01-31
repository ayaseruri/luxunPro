package pro.luxun.luxunanimation.view.view.Danmaku;

import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.view.view.ColorPalette;
import ykooze.ayaseruri.codesslib.ui.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/24.
 */
@EBean
public class DanmakuSetting implements View.OnClickListener{

    private PopupWindow mPopupWindow;
    private Context mContext;
    private int mPopHeight, mPopWidth, mColor;

    private Button mRLButton, mLRButton, mTopButton;
    private ColorPalette mColorPalette;

    private IOnColorPaletteClick mIOnColorPaletteClick;

    private String mDanmakuType;
    private View mContentView;

    public DanmakuSetting(Context context) {
        mContext = context;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.danmaku_setting, null);
        mContentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        mPopWidth = mContentView.getMeasuredWidth();
        mPopHeight = mContentView.getMeasuredHeight();

        mColorPalette = (ColorPalette) mContentView.findViewById(R.id.color_palette);

        mRLButton = (Button) mContentView.findViewById(R.id.RL_btn);
        mLRButton = (Button) mContentView.findViewById(R.id.LR_btn);
        mTopButton = (Button) mContentView.findViewById(R.id.top_btn);

        mRLButton.setOnClickListener(this);
        mRLButton.setSelected(true);
        mDanmakuType = DanmakuConstant.TYPE_RL;

        mLRButton.setOnClickListener(this);
        mTopButton.setOnClickListener(this);

        mColor = mColorPalette.getColor();
        mColorPalette.setIOnColorClick(new ColorPalette.IOnColorClick() {
            @Override
            public void onColorClick(int color) {
                if(null != mIOnColorPaletteClick){
                    mIOnColorPaletteClick.onColorClick(mDanmakuType, color);
                }
                mPopupWindow.dismiss();
            }
        });
    }

    public void show(View athor){
        mPopupWindow = new PopupWindow(mContentView, mPopWidth, mPopHeight, true);
        mPopupWindow.showAsDropDown(athor, - mPopWidth/2, - mPopHeight - LocalDisplay.dp2px(22));
    }

    public void setIOnColorPaletteClick(IOnColorPaletteClick IOnColorPaletteClick) {
        mIOnColorPaletteClick = IOnColorPaletteClick;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.RL_btn:
                mDanmakuType = DanmakuConstant.TYPE_RL;
                break;
            case R.id.LR_btn:
                mDanmakuType = DanmakuConstant.TYPE_LR;
                break;
            case R.id.top_btn:
                mDanmakuType = DanmakuConstant.TYPE_TOP;
                break;
            default:
                break;
        }

        mRLButton.setSelected(false);
        mLRButton.setSelected(false);
        mTopButton.setSelected(false);

        if(v instanceof Button){
            Button button = (Button) v;
            button.setSelected(true);
        }
    }

    public int getDefaultColor(){
        return mColor;
    }

    public String getDefaultType(){
        return mDanmakuType;
    }

    public interface IOnColorPaletteClick{
        void onColorClick(String type, int color);
    }
}
