package ykooze.ayaseruri.codesslib.others;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by wufeiyang on 16/6/18.
 */
public class InputMethodUtils {
    /**
     * 显示输入法
     * @param context
     * @param view view是要在哪个view的基础上显示输入面板，同时再使用该方法之前，view需要获得焦点，可以通过requestFocus()方法来设定。
     */
    public static void show(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 隐藏输入法
     * @param context
     * @param view 当前获得焦点的view
     */
    public static void hide(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
