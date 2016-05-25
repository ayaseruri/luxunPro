package pro.luxun.luxunanimation.utils;

import pro.luxun.luxunanimation.db.DanmakuSettingPrefer_;
import pro.luxun.luxunanimation.global.MApplication_;

/**
 * Created by wufeiyang on 16/5/25.
 */
public class DanmakuSettingHelper {
    private static DanmakuSettingPrefer_ sDanmakuSettingPrefer = new DanmakuSettingPrefer_(MApplication_.getInstance());

    public static void switchOff(){
        sDanmakuSettingPrefer.isDanmakuShow().put(false);
    }

    public static void switchOn(){
        sDanmakuSettingPrefer.isDanmakuShow().put(true);
    }

    public static boolean getSwitchStatus(){
        return sDanmakuSettingPrefer.isDanmakuShow().get();
    }
}
