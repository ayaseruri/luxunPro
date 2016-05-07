package pro.luxun.luxunanimation.net;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by zhangxl on 15/12/30.
 */
final class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Override
    public RequestBody convert(T value) throws IOException {
        String json = JSON.toJSONString(value);
        return RequestBody.create(MEDIA_TYPE, json);
    }
}
