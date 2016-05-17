package pro.luxun.luxunanimation.db;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by wufeiyang on 16/5/17.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface CookiePrefer {
    @DefaultString("")
    String getPhpsessid();
}
