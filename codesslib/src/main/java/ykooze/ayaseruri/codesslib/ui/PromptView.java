package ykooze.ayaseruri.codesslib.ui;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import ykooze.ayaseruri.codesslib.R;

/**
 * Created by wufeiyang on 16/7/20.
 */
public class PromptView extends FrameLayout {

    public static final int TYPE_UNINIT = -1;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_EMPTY = 1;
    public static final int TYPE_ERROR = 2;
    public static final int TYPE_LOADING = 3;

    private View mLoadingFrame;
    private View mErrorFrame;
    private View mEmptyFrame;
    private int mType = TYPE_UNINIT;

    private int mErrorLayoutId = -1, mEmptyLayoutId = -1, mLoadingLayoutId = -1;

    private LayoutInflater mInflater;

    public PromptView(Context context) {
        super(context);
        init();
    }

    public PromptView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.PromptView);
        mErrorLayoutId = array.getResourceId(R.styleable.PromptView_prompt_error_view, -1);
        mEmptyLayoutId = array.getResourceId(R.styleable.PromptView_prompt_empty_view, -1);
        mLoadingLayoutId = array.getResourceId(R.styleable.PromptView_prompt_loading_view, -1);
        array.recycle();
    }

    private void init(){
        mInflater = LayoutInflater.from(getContext());
    }

    @UiThread
    public void setType(@StatusType int type){
        mType = type;

        if(-1 != mErrorLayoutId){
            if(null == mErrorFrame){
                synchronized (PromptView.class){
                    if(null == mErrorFrame){
                        mErrorFrame = ((FrameLayout) mInflater.inflate(mEmptyLayoutId, this, true)).getChildAt(0);
                    }
                }
            }
        }

        if(-1 != mEmptyLayoutId){
            if(null == mEmptyFrame){
                synchronized (PromptView.class){
                    if(null == mEmptyFrame){
                        mEmptyFrame = ((FrameLayout) mInflater.inflate(mEmptyLayoutId, this, true)).getChildAt(1);
                    }
                }
            }
        }

        if(-1 != mLoadingLayoutId){
            if(null == mLoadingFrame){
                synchronized (PromptView.class){
                    if(null == mLoadingFrame){
                        mLoadingFrame = ((FrameLayout) mInflater.inflate(mLoadingLayoutId, this, true)).getChildAt(2);
                    }
                }
            }
        }

        if(null != mEmptyFrame && VISIBLE == mEmptyFrame.getVisibility()){
            mEmptyFrame.setVisibility(INVISIBLE);
        }
        if(null != mErrorFrame && VISIBLE == mErrorFrame.getVisibility()){
            mErrorFrame.setVisibility(INVISIBLE);
        }
        if(null != mLoadingFrame && VISIBLE == mLoadingFrame.getVisibility()){
            mLoadingFrame.setVisibility(INVISIBLE);
        }

        switch (mType){
            case TYPE_NORMAL:
                break;
            case TYPE_EMPTY:
                if(null != mEmptyFrame){
                    mEmptyFrame.setVisibility(VISIBLE);
                }
                break;
            case TYPE_ERROR:
                if(null != mErrorFrame){
                    mErrorFrame.setVisibility(VISIBLE);
                }
                break;
            case TYPE_LOADING:
                if(null != mLoadingFrame){
                    mLoadingFrame.setVisibility(VISIBLE);
                }
            default:
                break;
        }
    }

    public View getLoadingFrame() {
        return mLoadingFrame;
    }

    public void setLoadingFrame(FrameLayout loadingFrame) {
        mLoadingFrame = loadingFrame;
    }

    public View getErrorFrame() {
        return mErrorFrame;
    }

    public void setErrorFrame(FrameLayout errorFrame) {
        mErrorFrame = errorFrame;
    }

    public View getEmptyFrame() {
        return mEmptyFrame;
    }

    public void setEmptyFrame(FrameLayout emptyFrame) {
        mEmptyFrame = emptyFrame;
    }

    public int getErrorLayoutId() {
        return mErrorLayoutId;
    }

    public void setErrorLayoutId(int errorLayoutId) {
        mErrorLayoutId = errorLayoutId;
    }

    public int getEmptyLayoutId() {
        return mEmptyLayoutId;
    }

    public void setEmptyLayoutId(int emptyLayoutId) {
        mEmptyLayoutId = emptyLayoutId;
    }

    public int getLoadingLayoutId() {
        return mLoadingLayoutId;
    }

    public void setLoadingLayoutId(int loadingLayoutId) {
        mLoadingLayoutId = loadingLayoutId;
    }

    public int getType() {
        return mType;
    }

    @IntDef({TYPE_LOADING, TYPE_NORMAL, TYPE_EMPTY, TYPE_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StatusType{

    }
}
