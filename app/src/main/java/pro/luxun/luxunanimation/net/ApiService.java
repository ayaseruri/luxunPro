package pro.luxun.luxunanimation.net;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pro.luxun.luxunanimation.bean.Auth;
import pro.luxun.luxunanimation.bean.Comment;
import pro.luxun.luxunanimation.bean.Danmaku;
import pro.luxun.luxunanimation.bean.GetToken;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.bean.SubComment;
import pro.luxun.luxunanimation.bean.TopicJson;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by wufeiyang on 16/5/7.
 */
public interface ApiService {
    @GET
    Observable<MainJson> getMainJson(@Url String url);

    @GET
    Observable<List<TopicJson>> getTopicJson(@Url String url);

    @Multipart
    @POST
    Observable<GetToken> getToken(@Url String url, @Part("text") String text);

    @GET
    Observable<Auth> auth(@Url String url);

    @GET
    Observable<List<Comment>> getCommentList(@Url String url);

    @Multipart
    @POST
    Observable<SubComment> submitComment(@Url String url, @Part("rate") int rate
            , @Part("cur") int cur
            , @Part("time") RequestBody time
            , @Part("text") RequestBody comment);

    @GET
    Observable<List<String>> getBangumis(@Url String url);

    @Multipart
    @POST
    Observable<Object> subscribe(@Url String url, @Part("type") RequestBody type);

    @GET
    Observable<Object> getlikes(@Url String url);

    @GET
    Observable<List<List>> getDm(@Url String url);

    @Multipart
    @POST
    Observable<Danmaku> submitDm(@Url String url, @Part("start") RequestBody time
            , @Part("text") RequestBody text, @Part("color") RequestBody color, @Part("y") int y
            , @Part("type") RequestBody type);

    @GET
    Observable<ResponseBody> checkUpdate(@Url String url);
}
