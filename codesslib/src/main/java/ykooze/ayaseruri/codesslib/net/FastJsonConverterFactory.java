package ykooze.ayaseruri.codesslib.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * FastJson 解析器
 * Created by ayaseruri on 15/12/30.
 * waring 解析器在初始化的时候会自动初始化String变量为“”，防止null，但是会覆盖初始化的String类型为空字符串“”
 */
public class FastJsonConverterFactory extends Converter.Factory {

    private FastJsonConverterFactory() {

    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static FastJsonConverterFactory create() {
        return new FastJsonConverterFactory();
    }

    @Override
    public Converter<okhttp3.ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastJsonResponseBodyConverter<>(type);
    }

    @Override
    public Converter<?, okhttp3.RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FastJsonRequestBodyConverter<>();
    }

    private static class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

        @Override
        public RequestBody convert(T value) throws IOException {
            String json = JSON.toJSONString(value);
            return RequestBody.create(MEDIA_TYPE, json);
        }
    }

    private static class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

        private Type type;

        public FastJsonResponseBodyConverter(Type type) {
            this.type = type;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            return JSON.parseObject(value.string(), type, Feature.InitStringFieldAsEmpty);
        }
    }
}
