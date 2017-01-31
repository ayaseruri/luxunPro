package ykooze.ayaseruri.codesslib.update;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import ykooze.ayaseruri.codesslib.R;
import ykooze.ayaseruri.codesslib.io.FileUtils;
import ykooze.ayaseruri.codesslib.net.download.DownloadListener;
import ykooze.ayaseruri.codesslib.net.download.DownloadManager;

/**
 * 需要网络访问权限
 * Created by wufeiyang on 2017/1/11.
 */

public class UpdateDialog extends Dialog implements View.OnClickListener{

    private static final short NOTIFY_id = 1086;

    private TextView mDetailTV;
    private Button mCancel, mOk;
    private UpdateInfo mUpdateInfo;
    private DownloadListener mDownloadListener;

    public UpdateDialog(Context context, UpdateInfo updateInfo, DownloadListener downloadListener) {
        super(context);
        mUpdateInfo = updateInfo;
        mDownloadListener = downloadListener;
        init();
    }

    public void setProgressListener(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    private void init(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.codess_dialog_update);

        mDetailTV = (TextView) findViewById(R.id.update_detail);
        mCancel = (Button) findViewById(R.id.cancel);
        mOk = (Button) findViewById(R.id.ok);

        if(null != mUpdateInfo){
            mDetailTV.setText(mUpdateInfo.getDetail());
        }

        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(null != mUpdateInfo){
            if(R.id.ok == v.getId()){
                new DownloadManager(mUpdateInfo.getDownload_url()
                        , FileUtils.getExternalCacheDir(getContext())
                        , mDownloadListener)
                        .download(0);
            }
        }

        dismiss();
    }
}
