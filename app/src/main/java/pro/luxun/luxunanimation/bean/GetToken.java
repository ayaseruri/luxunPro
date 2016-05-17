package pro.luxun.luxunanimation.bean;

/**
 * Created by wufeiyang on 16/5/17.
 */
public class GetToken {

    /**
     * token : 8733badc0f6fbbf0bb6c5cff82213789
     * text : 来自安卓客户端的登录请求
     * type : 0
     * created : 1463392023
     */

    private String token;
    private String text;
    private int type;
    private int created;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }
}
