package pro.luxun.luxunanimation.utils;

/**
 * Created by wufeiyang on 16/5/19.
 */
public class AuthInfoHelper {

    private static String mAuthToken = "";
    private static boolean mIsRequestAuth;

    public static boolean setRequestAuth(boolean isRequestAuth){
        mIsRequestAuth = isRequestAuth;
        return mIsRequestAuth;
    }

    public static boolean isRequestAuth(){
        return mIsRequestAuth;
    }

    public static void saveAuthToken(String token){
        mAuthToken = token;
    }

    public static String getAuthToken(){
        return mAuthToken;
    }

}
