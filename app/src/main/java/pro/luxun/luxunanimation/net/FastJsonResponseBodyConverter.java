package pro.luxun.luxunanimation.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by zhangxl on 15/12/30.
 */
final class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Type type;

    public FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Reader reader = value.charStream();
        BufferedReader in = new BufferedReader(reader);
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        String json = buffer.toString();
        in.close();
        return JSON.parseObject(json, type, Feature.InitStringFieldAsEmpty);
    }
}
