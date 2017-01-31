package pro.luxun.luxunanimation.global;

import java.io.InputStream;

import org.androidannotations.annotations.EApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;

import android.app.Application;
import pro.luxun.luxunanimation.net.RetrofitClient;
import ykooze.ayaseruri.codesslib.ui.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/7.
 */
@EApplication
public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Glide.get(this).register(GlideUrl.class
                , InputStream.class
                , new OkHttpUrlLoader.Factory(RetrofitClient.getOkHttpClient(this)));

        LocalDisplay.init(this);
    }
}
