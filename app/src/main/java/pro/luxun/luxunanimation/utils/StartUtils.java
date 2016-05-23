package pro.luxun.luxunanimation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.global.IntentConstant;
import pro.luxun.luxunanimation.view.activity.AnimationDetailActivity_;
import pro.luxun.luxunanimation.view.activity.VideoActivity_;

/**
 * Created by wufeiyang on 16/5/9.
 */
public class StartUtils {

    public static final int REQUEST_LOCAL_BORWSER = 0;

    public static void startAnimationDetailActivity(Context context, MainJson.UpdatingEntity updatingEntity){
        Intent intent = new Intent(context, AnimationDetailActivity_.class);
        intent.putExtra(IntentConstant.INTENT_UPDATING_ENTITY, (Parcelable) updatingEntity);
        context.startActivity(intent);
    }

    public static void startVedioActivty(Context context, MainJson.UpdatingEntity.SetsEntity setsEntity){
        Intent intent = new Intent(context, VideoActivity_.class);
        intent.putExtra(IntentConstant.INTENT_VIDEO_SET_ENTITY, (Parcelable) setsEntity);
        context.startActivity(intent);
    }

    public static void startLocalBorwser(Activity context, String url){
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivityForResult(it, REQUEST_LOCAL_BORWSER);
    }
}
