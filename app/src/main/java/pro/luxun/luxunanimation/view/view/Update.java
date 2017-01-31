package pro.luxun.luxunanimation.view.view;

import java.io.File;
import java.io.IOException;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import pro.luxun.luxunanimation.BuildConfig;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.db.ComSettingPrefer_;
import pro.luxun.luxunanimation.global.MApplication;
import pro.luxun.luxunanimation.net.RetrofitClient;
import ykooze.ayaseruri.codesslib.others.ToastUtils;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

/**
 * Created by wufeiyang on 16/5/29.
 */
@EBean
public class Update {

    private static final int DOWNLOAD_NOTIFICATION_ID = 10086;

    private Context mContext;
    private Dialog mDialog;
    private TextView mDetailTV;

    private Button mCancel, mOk, mIngnore;
    private NotificationManager mNotifyMgr;

    @App
    MApplication mApplication;
    @Pref
    ComSettingPrefer_ mComSettingPrefer;

    public Update(Context mContext) {
        this.mContext = mContext;
        this.mNotifyMgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void checkUpdate(final boolean isSilent){
        RetrofitClient.getApiService().checkUpdate(RetrofitClient.URL_UPDATE)
                .compose(RxUtils.<pro.luxun.luxunanimation.bean.Update>applySchedulers())
                .subscribe(new Observer<pro.luxun.luxunanimation.bean.Update>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final pro.luxun.luxunanimation.bean.Update updateInfo) {
                        if (BuildConfig.VERSION_CODE != updateInfo.getVer_code()
                                && mComSettingPrefer.getIngnoreVer().get() != updateInfo.getVer_code()) {
                            mDialog = new Dialog(mContext);
                            mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            mDialog.setCancelable(false);
                            mDialog.setContentView(R.layout.dialog_update);

                            mDetailTV = (TextView) mDialog.findViewById(R.id.update_detail);
                            mCancel = (Button) mDialog.findViewById(R.id.cancel);
                            mIngnore = (Button) mDialog.findViewById(R.id.ingnore);
                            mOk = (Button) mDialog.findViewById(R.id.ok);

                            mIngnore.setVisibility(updateInfo.is_fouce_update() ? View.GONE : View.VISIBLE);

                            mCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            });

                            mIngnore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mComSettingPrefer.getIngnoreVer().put(updateInfo.getVer_code());
                                    mDialog.dismiss();
                                }
                            });

                            mOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadUpdate(updateInfo.getDownload_url(), "update",
                                            mContext.getCacheDir().getPath());
                                    mDialog.dismiss();
                                }
                            });

                            mDetailTV.setText(updateInfo.getDetail());

                            mDialog.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(!isSilent){
                            ToastUtils.showTost(mContext, ToastUtils.TOAST_ALERT, "更新失败");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void downloadUpdate(final String url, final String saveName, final String savePath){
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                Request request = new Request.Builder().url(url).build();
                Response response = RetrofitClient.getOkHttpClient(mContext)
                        .newCall(request).execute();
                if (response.isSuccessful()) {
                    File file = new File(savePath + saveName + "." + MimeTypeMap.getFileExtensionFromUrl(url));
                    if (!file.exists()) {
                        if (!file.createNewFile()) {
                            e.onError(new IOException("无法创建歌曲文件"));
                            return;
                        }
                    }
                    BufferedSink output = Okio.buffer(Okio.sink(file));
                    BufferedSource input = Okio.buffer(Okio.source(response.body().byteStream()));
                    long totalByteLength = response.body().contentLength();
                    byte data[] = new byte[2048];

                    e.onNext(0);
                    long total = 0;
                    int count = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                        e.onNext((int) (total * 100 / totalByteLength));
                    }
                    output.flush();
                    output.close();
                    input.close();
                    Uri uri = Uri.fromFile(file); //这里是APK路径
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    mContext.startActivity(intent);
                    e.onComplete();
                } else {
                    e.onError(new Exception("更新出现问题"));
                }
            }
        }, BackpressureStrategy.DROP).compose(RxUtils.<Integer>applyFlowableSchedulers())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
                        notificationBuilder.setContentTitle("正在下载更新");
                        mNotifyMgr.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
                    }

                    @Override
                    public void onNext(Integer precentage) {
                        notificationBuilder.setProgress(100, precentage, false);
                        mNotifyMgr.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        notificationBuilder.setContentTitle("更新失败，请稍后重试");
                        mNotifyMgr.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
                        ToastUtils.showTost(mContext, ToastUtils.TOAST_ALERT, "更新失败，请稍后重试");
                    }

                    @Override
                    public void onComplete() {
                        notificationBuilder.setProgress(100, 100, false);
                        notificationBuilder.setContentTitle("下载完毕");
                        mNotifyMgr.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
                    }
                });
    }
}
