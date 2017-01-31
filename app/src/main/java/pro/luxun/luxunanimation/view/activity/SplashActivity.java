package pro.luxun.luxunanimation.view.activity;

import java.util.concurrent.TimeUnit;

import org.androidannotations.annotations.EActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.utils.StartUtils;
import ykooze.ayaseruri.codesslib.rx.RxActivity;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends RxActivity {

    private static final short TIME_SPLASH = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.timer(TIME_SPLASH, TimeUnit.SECONDS)
                .compose(this.<Long>bindToLifecycle())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        StartUtils.startMainActivity(SplashActivity.this, 1);
                        finish();
                    }
                });
    }
}
