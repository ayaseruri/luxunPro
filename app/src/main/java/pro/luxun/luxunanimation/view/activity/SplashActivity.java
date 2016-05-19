package pro.luxun.luxunanimation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.EActivity;

import java.util.concurrent.TimeUnit;

import pro.luxun.luxunanimation.R;
import rx.Observable;
import rx.functions.Action1;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {
    private static final int TIME_SPLASH = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.timer(TIME_SPLASH, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long l) {
                        startActivity(new Intent(SplashActivity.this, MainActivity_.class));
                        finish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
