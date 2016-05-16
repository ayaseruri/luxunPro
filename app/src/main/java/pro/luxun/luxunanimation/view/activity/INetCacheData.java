package pro.luxun.luxunanimation.view.activity;

/**
 * Created by wufeiyang on 16/5/7.
 */
public interface INetCacheData<T> {
    void onStartGetJsonNet();
    void onGetJsonSuccessNet(T t);
    void onGetJsonErrorNet();
    void onGetJsonCacheSuccess(T t);
    void onGetJsonCacheFailed();
}
