package pro.luxun.luxunanimation.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.global.IntentConstant;
import pro.luxun.luxunanimation.view.activity.AnimationDetailActivity_;

/**
 * Created by wufeiyang on 16/5/9.
 */
public class StartUtils {

    public static void startAnimationDetailActivity(Context context, MainJson.UpdatingEntity updatingEntity){
        Intent intent = new Intent(context, AnimationDetailActivity_.class);
        intent.putExtra(IntentConstant.INTENT_UPDATING_ENTITY, (Parcelable) updatingEntity);
        context.startActivity(intent);
    }
}
