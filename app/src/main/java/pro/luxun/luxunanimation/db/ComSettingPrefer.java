package pro.luxun.luxunanimation.db;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by wufeiyang on 16/5/29.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface ComSettingPrefer {
    @DefaultInt(-1)
    int getIngnoreVer();
}
