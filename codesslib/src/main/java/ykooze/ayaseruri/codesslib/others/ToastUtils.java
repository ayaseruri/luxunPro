package ykooze.ayaseruri.codesslib.others;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.github.johnpersano.supertoasts.library.Style.DURATION_LONG;

/**
 * Created by wufeiyang on 16/7/20.
 */
public class ToastUtils {

    public static final int TOAST_ALERT = 0;
    public static final int TOAST_INFO = 1;
    public static final int TOAST_CONFIRM = 2;

    public static void showTost(@NonNull Context context
            , @ToastType int type
            , String content){

        Style style = Style.green();
        switch (type){
            case TOAST_ALERT:
                style = Style.red();
                break;
            case TOAST_CONFIRM:
                style = Style.green();
                break;
            case TOAST_INFO:
                style = Style.grey();
                break;
            default:
                break;
        }

        SuperToast.create(context.getApplicationContext()
                , content
                , DURATION_LONG
                , style)
                .setFrame(Style.FRAME_KITKAT)
                .setText(content)
                .setAnimations(Style.ANIMATIONS_FADE)
                .show();
    }

    public static void showTost(@NonNull Context context
            , @ToastType int type
            , int stringId){
        showTost(context, type, context.getString(stringId));
    }

    @IntDef({TOAST_INFO, TOAST_ALERT, TOAST_CONFIRM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ToastType{

    }
}
