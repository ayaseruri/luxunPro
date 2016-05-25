package pro.luxun.luxunanimation.db;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by wufeiyang on 16/5/25.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface DanmakuSettingPrefer {
    @DefaultBoolean(true)
    boolean isDanmakuShow();
}
