package pro.luxun.luxunanimation.view.view;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import pro.luxun.luxunanimation.BuildConfig;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.db.ComSettingPrefer;
import pro.luxun.luxunanimation.db.ComSettingPrefer_;
import pro.luxun.luxunanimation.global.MApplication;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

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
        RetrofitClient.getApiService().checkUpdate(RetrofitClient.URL_UPDATE).compose(RxUtils.<ResponseBody>applySchedulers())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(!isSilent){
                            mApplication.showToast("更新失败", MApplication.TOAST_ALERT);
                        }
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        try {
                            String infoStr = response.string();
                            final pro.luxun.luxunanimation.bean.Update updateInfo = JSON.parseObject(infoStr, pro.luxun.luxunanimation.bean.Update.class, Feature.InitStringFieldAsEmpty);

                            if(BuildConfig.VERSION_CODE != updateInfo.getVer_code() && mComSettingPrefer.getIngnoreVer().get() != updateInfo.getVer_code()){
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
                                        downloadUpdate(updateInfo.getDownload_url(), "update", mContext.getCacheDir().getPath());
                                        mDialog.dismiss();
                                    }
                                });

                                mDetailTV.setText(updateInfo.getDetail());

                                mDialog.show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            onError(e);
                        }
                    }

                    @Override
                    public void onStart() {
                        if(!isSilent){
                            mApplication.showToast("正在检查更新", MApplication.TOAST_INFO);
                        }
                    }
                });
    }

    private void downloadUpdate(final String url, final String saveName, final String savePath){
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Request request = new Request.Builder().url(url).build();
                BufferedSink output = null;
                BufferedSource input = null;
                try {
                    Response response = RetrofitClient.getOkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        File file = new File(savePath + saveName + "." + MimeTypeMap.getFileExtensionFromUrl(url));
                        if (!file.exists()) {
                            if (!file.createNewFile()) {
                                subscriber.onError(new IOException("无法创建歌曲文件"));
                                return;
                            }
                        }
                        output = Okio.buffer(Okio.sink(file));
                        input = Okio.buffer(Okio.source(response.body().byteStream()));
                        long totalByteLength = response.body().contentLength();
                        byte data[] = new byte[2048];

                        subscriber.onNext(0);
                        long total = 0;
                        int count = 0;
                        while ((count = input.read(data)) != -1) {
                            total += count;
                            output.write(data, 0, count);
                            subscriber.onNext((int) (total * 100 / totalByteLength));
                        }
                        output.flush();
                        output.close();
                        input.close();
                        Uri uri = Uri.fromFile(file); //这里是APK路径
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        mContext.startActivity(intent);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Exception("更新出现问题"));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(new IOException("数据读写错误"));
                }
            }
        }).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .compose(RxUtils.<Integer>applySchedulers())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        notificationBuilder.setProgress(100, 100, false);
                        notificationBuilder.setContentTitle("下载完毕");
                        mNotifyMgr.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        notificationBuilder.setContentTitle("更新失败，请稍后重试");
                        mNotifyMgr.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
                        mApplication.showToast("更新失败，请稍后重试", MApplication.TOAST_ALERT);
                    }

                    @Override
                    public void onNext(Integer precentage) {
                        notificationBuilder.setProgress(100, precentage, false);
                        mNotifyMgr.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
                    }

                    @Override
                    public void onStart() {
                        notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
                        notificationBuilder.setContentTitle("正在下载更新");
                        mNotifyMgr.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
                    }
                });
    }
}
