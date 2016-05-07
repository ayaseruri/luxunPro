package pro.luxun.luxunanimation.net;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * FastGson 解析器
 * Created by zhangxl on 15/12/30.
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
}
