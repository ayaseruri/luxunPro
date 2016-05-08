package pro.luxun.luxunanimation.view.activity;

import pro.luxun.luxunanimation.bean.MainJson;

/**
 * Created by wufeiyang on 16/5/7.
 */
public interface IMainActivity {
    void onStartGetMainJsonNet();
    void onGetMainJsonSuccessNet(MainJson mainJson);
    void onGetMainJsonErrorNet();
    void onGetMainJsonCacheSuccess(MainJson mainJson);
    void onGetMainJsonCacheFailed();
}
