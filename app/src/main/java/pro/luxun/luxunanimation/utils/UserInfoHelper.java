package pro.luxun.luxunanimation.utils;

import android.text.TextUtils;

import pro.luxun.luxunanimation.bean.Auth;

/**
 * Created by wufeiyang on 16/5/18.
 */
public class UserInfoHelper {
    private static Auth.UserEntity mUserEntity;

    public static void save(Auth.UserEntity userEntity){
        mUserEntity = userEntity;
        mUserEntity.setAvatar(mUserEntity.getAvatar().replace("50","180"));
        SerializeUtils.serialization(SerializeUtils.TAG_USER_INFO, mUserEntity);
    }

    public static Auth.UserEntity getUserInfo(){
        if(null == mUserEntity){
            mUserEntity = (Auth.UserEntity) SerializeUtils.deserialization(SerializeUtils.TAG_USER_INFO, false);
            if(mUserEntity == null){
                mUserEntity = new Auth.UserEntity();
            }
        }
        return mUserEntity;
    }

    public static boolean isLogin(){
        return !TextUtils.isEmpty(getUserInfo().getUid());
    }
}
