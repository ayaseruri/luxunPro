package pro.luxun.luxunanimation.net;

import java.util.List;

import pro.luxun.luxunanimation.bean.Auth;
import pro.luxun.luxunanimation.bean.Comment;
import pro.luxun.luxunanimation.bean.GetToken;
import pro.luxun.luxunanimation.bean.MainJson;
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
    Observable<Object> submitComment(@Url String url, @Part("rate") int rate
            , @Part("cur") int cur
            , @Part("time") String time
            , @Part("text") String comment);
}
