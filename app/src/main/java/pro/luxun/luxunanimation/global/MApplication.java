package pro.luxun.luxunanimation.global;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.github.johnpersano.supertoasts.SuperToast;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.UiThread;

import java.io.InputStream;

import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.LocalDisplay;
import pro.luxun.luxunanimation.utils.SerializeUtils;

/**
 * Created by wufeiyang on 16/5/7.
 */
@EApplication
public class MApplication extends Application {

    public static final int TOAST_ALERT = 0;
    public static final int TOAST_INFO = 1;
    public static final int TOAST_CONFIRM = 2;

    private SuperToast mSuperToast;

    @Override
    public void onCreate() {
        super.onCreate();

        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(RetrofitClient.getOkHttpClient()));

        SerializeUtils.init(this);
        LocalDisplay.init(this);
    }

    @UiThread
    public void showToast(String string, int kind) {
        int bgColor = SuperToast.Background.GREEN;
        switch (kind) {
            case TOAST_ALERT:
                bgColor = SuperToast.Background.RED;
                break;
            case TOAST_CONFIRM:
                bgColor = SuperToast.Background.GREEN;
                break;
            case TOAST_INFO:
                bgColor = SuperToast.Background.GRAY;
                break;
            default:
                break;
        }
        if (mSuperToast == null) {
            mSuperToast = new SuperToast(this);
        }
        mSuperToast.setText(string);
        mSuperToast.setBackground(bgColor);
        if (!mSuperToast.isShowing()) {
            mSuperToast.show();
        }
    }
}
