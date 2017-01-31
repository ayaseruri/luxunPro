package ykooze.ayaseruri.codesslib.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;


/**
 * Created by jk.yeo on 16/3/4 15:28.
 * Mail to ykooze@gmail.com
 */
public class BasicParamsInterceptor implements Interceptor {

    private volatile Map<String, String> queryParamsMap;
    private volatile Map<String, String> paramsMap;
    private volatile Map<String, String> headerParamsMap;
    private volatile List<String> headerLinesList;

    private BasicParamsInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        // process header params inject
        Headers.Builder headerBuilder = request.headers().newBuilder();
        if (null != headerParamsMap && headerParamsMap.size() > 0) {
            Iterator iterator = headerParamsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                headerBuilder.add((String) entry.getKey(), (String) entry.getValue());
            }
        }

        if (null != headerLinesList && headerLinesList.size() > 0) {
            for (String line: headerLinesList) {
                headerBuilder.add(line);
            }
        }

        requestBuilder.headers(headerBuilder.build());
        // process header params end


        // process queryParams inject whatever it's GET or POST
        if (null != queryParamsMap && queryParamsMap.size() > 0) {
            injectParamsIntoUrl(request, requestBuilder, queryParamsMap);
        }
        // process header params end


        // process post body inject
        if (request.method().equals("POST") && null != request.body().contentType() && request.body().contentType().subtype().equals("x-www-form-urlencoded")) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if (null != paramsMap && paramsMap.size() > 0) {
                Iterator iterator = paramsMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    formBodyBuilder.add((String) entry.getKey(), (String) entry.getValue());
                }
            }
            RequestBody formBody = formBodyBuilder.build();
            String postBodyString = bodyToString(request.body());
            postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
            requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8")
                    , postBodyString));
        } else {
            // can't inject into body, then inject into url
            if(null != paramsMap){
                injectParamsIntoUrl(request, requestBuilder, paramsMap);
            }
        }

        request = requestBuilder.build();
        return chain.proceed(request);
    }

    // func to inject params into url
    private void injectParamsIntoUrl(Request request, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
        }

        requestBuilder.url(httpUrlBuilder.build());
    }

    private static String bodyToString(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if(copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }

    public Map<String, String> getQueryParamsMap() {
        if(null == queryParamsMap){
            synchronized (BasicParamsInterceptor.class){
                if(null == queryParamsMap){
                    queryParamsMap = new HashMap<>();
                }
            }
        }
        return queryParamsMap;
    }

    public Map<String, String> getParamsMap() {
        if(null == paramsMap){
            synchronized (BasicParamsInterceptor.class){
                if(null == paramsMap){
                    paramsMap = new HashMap<>();
                }
            }
        }
        return paramsMap;
    }

    public Map<String, String> getHeaderParamsMap() {
        if(null == headerParamsMap){
            synchronized (BasicParamsInterceptor.class){
                if(null == headerParamsMap){
                    headerParamsMap = new HashMap<>();
                }
            }
        }
        return headerParamsMap;
    }

    public List<String> getHeaderLinesList() {
        if(null == headerLinesList){
            synchronized (BasicParamsInterceptor.class){
                if(null == headerLinesList){
                    headerLinesList = new ArrayList<>();
                }
            }
        }
        return headerLinesList;
    }

    public interface IOnResponse{
        void onResponse(Response response);
    }

    public static class Builder {

        BasicParamsInterceptor interceptor;

        public Builder() {
            interceptor = new BasicParamsInterceptor();
        }

        public Builder addParam(String key, String value) {
            interceptor.getParamsMap().put(key, value);
            return this;
        }

        public Builder addParamsMap(Map<String, String> paramsMap) {
            interceptor.paramsMap.putAll(paramsMap);
            return this;
        }

        public Builder addHeaderParam(String key, String value) {
            interceptor.getHeaderParamsMap().put(key, value);
            return this;
        }

        public Builder addHeaderParamsMap(Map<String, String> headerParamsMap) {
            interceptor.getHeaderParamsMap().putAll(headerParamsMap);
            return this;
        }

        public Builder addHeaderLine(String headerLine) {
            int index = headerLine.indexOf(":");
            if (index == -1) {
                throw new IllegalArgumentException("Unexpected header: " + headerLine);
            }
            interceptor.getHeaderLinesList().add(headerLine);
            return this;
        }

        public Builder addHeaderLinesList(List<String> headerLinesList) {
            for (String headerLine: headerLinesList) {
                int index = headerLine.indexOf(":");
                if (index == -1) {
                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
                }
                interceptor.getHeaderLinesList().add(headerLine);
            }
            return this;
        }

        public Builder addQueryParam(String key, String value) {
            interceptor.getQueryParamsMap().put(key, value);
            return this;
        }

        public Builder addQueryParamsMap(Map<String, String> queryParamsMap) {
            interceptor.getQueryParamsMap().putAll(queryParamsMap);
            return this;
        }

        public BasicParamsInterceptor build() {
            return interceptor;
        }

    }
}
