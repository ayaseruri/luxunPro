package pro.luxun.luxunanimation.global;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;

import org.androidannotations.annotations.EApplication;

import java.io.InputStream;

import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.LocalDisplay;
import pro.luxun.luxunanimation.utils.SerializeUtils;

/**
 * Created by wufeiyang on 16/5/7.
 */
@EApplication
public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(RetrofitClient.getOkHttpClient()));

        SerializeUtils.init(this);
        LocalDisplay.init(this);
    }
}
