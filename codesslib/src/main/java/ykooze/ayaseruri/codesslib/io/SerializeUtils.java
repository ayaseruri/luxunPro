package ykooze.ayaseruri.codesslib.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

/**
 * @author zhangxl
 * @className SerializeUtils
 * @create 2014年4月16日 上午11:31:07
 * @description 序列化工具类，可用于序列化对象到文件或从文件反序列化对象
 */
public class SerializeUtils {
    /**
     * 从文件反序列化对象
     *
     * @param tag
     * @return
     * @throws RuntimeException if an error occurs
     */

    public static Object deserializationSync(final Context context, final String tag, final boolean delete) {
        try(FileInputStream fileInputStream = context.openFileInput(tag);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){
            Object object = objectInputStream.readObject();
            if (delete) {
                context.deleteFile(tag);
            }
            return object;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 序列化对象到文件，默认异步
     *
     * @param tag
     * @param obj
     * @return
     * @throws RuntimeException if an error occurs
     */
    public static void serialization(final Context context, final String tag, final Object obj) {
        io.reactivex.Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(true);
                e.onComplete();
            }
        }).subscribeOn(RxUtils.getSchedulers()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean b) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    //同步序列化
    public static void serializationSync(Context context, final String tag, final Object obj) {
        if(null == obj){
            return;
        }

        try(FileOutputStream fileOutputStream = context.openFileOutput(tag, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
