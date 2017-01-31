package ykooze.ayaseruri.codesslib.cache;

import java.io.Serializable;
import java.util.HashMap;

import android.content.Context;
import ykooze.ayaseruri.codesslib.io.SerializeUtils;

/**
 * Created by wufeiyang on 16/7/22.
 * 一个Key Value 缓存框架
 * 可以充当容器的角色,可以进行内存缓存,磁盘缓存,或者内存和磁盘均缓存;
 */
public class CacheUtils {

    private static volatile HashMap<String, Object> sMemoryCache;

    public static void putMemory(String key, Object value){
        initMemoryCache();
        sMemoryCache.put(key, value);
    }

    public static <T extends Object & Serializable> void putDisk(Context context, String key, T t){
        initMemoryCache();
        sMemoryCache.put(key, t);
        SerializeUtils.serializationSync(context, key, t);
    }

    public static Object get(Context context, String key, boolean delete){
        initMemoryCache();
        if(sMemoryCache.containsKey(key)){
            return sMemoryCache.get(key);
        }else {
            Object object = null;
            try {
                 object = SerializeUtils.deserializationSync(context, key, delete);
                if(null != object && !delete){
                    sMemoryCache.put(key, object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }
    }

    public static void delete(Context context, String key){
        initMemoryCache();
        if(sMemoryCache.containsKey(key)){
            sMemoryCache.remove(key);
        }

        try {
            SerializeUtils.deserializationSync(context, key, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAll(){
        sMemoryCache = null;
        initMemoryCache();
    }

    private static void initMemoryCache(){
        if(null == sMemoryCache){
            synchronized (CacheUtils.class){
                if(null == sMemoryCache){
                    sMemoryCache = new HashMap<>();
                }
            }
        }
    }
}
