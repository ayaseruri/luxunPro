package pro.luxun.luxunanimation.utils;

import android.content.Context;
import android.text.TextUtils;
import pro.luxun.luxunanimation.bean.Auth;
import ykooze.ayaseruri.codesslib.io.SerializeUtils;

/**
 * Created by wufeiyang on 16/5/18.
 */
public class UserInfoHelper {

    private static final String TAG_USER_INFO = "user_info";

    private static volatile Auth.UserEntity mUserEntity;

    public static void save(Context context, Auth.UserEntity userEntity){
        mUserEntity = userEntity;
        mUserEntity.setAvatar(mUserEntity.getAvatar().replace("50","180"));
        SerializeUtils.serialization(context, TAG_USER_INFO, mUserEntity);
    }

    public static Auth.UserEntity getUserInfo(Context context){
        if(null == mUserEntity){
            synchronized(UserInfoHelper.class){
                if(null == mUserEntity){
                    mUserEntity = (Auth.UserEntity) SerializeUtils.deserializationSync(context, TAG_USER_INFO, false);
                    if(mUserEntity == null){
                        mUserEntity = new Auth.UserEntity();
                    }
                }
            }
        }
        return mUserEntity;
    }

    public static boolean isLogin(Context context){
        return !TextUtils.isEmpty(getUserInfo(context).getUid());
    }
}
