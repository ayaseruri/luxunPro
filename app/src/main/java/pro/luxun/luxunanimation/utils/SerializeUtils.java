package pro.luxun.luxunanimation.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author ayaseruri
 * @className SerializeUtils
 * @create 2014年4月16日 上午11:31:07
 * @description 序列化工具类，可用于序列化对象到文件或从文件反序列化对象
 */
public class SerializeUtils {

    public static final String TAG_MAIN_JSON = "main_json";
    public static final String TAG_TOPIC_JSON = "topic_json";
    public static final String TAG_USER_INFO = "user_info";
    public static final String TAG_BANGUMIS = "bangumis";

    private static Context sContext;

    public static void init(Context context){
        sContext = context;
    }

    /**
     * 从文件反序列化对象
     *
     * @param tag
     * @return
     * @throws RuntimeException if an error occurs
     */
    public static Object deserialization(String tag, boolean delete) {
        if(null != sContext){
            try {
                FileInputStream fileInputStream = sContext.openFileInput(tag);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Object object = objectInputStream.readObject();
                objectInputStream.close();
                if (delete) {
                    sContext.deleteFile(tag);
                }
                return object;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 序列化对象到文件
     *
     * @param tag
     * @param obj
     * @return
     * @throws RuntimeException if an error occurs
     */
    public static void serialization(final String tag, final Object obj) {
        if(null == sContext){
            return;
        }

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    FileOutputStream fileOutputStream = sContext.openFileOutput(tag, Context.MODE_PRIVATE);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(obj);
                    objectOutputStream.close();
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean b) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }
}
