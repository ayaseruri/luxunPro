package pro.luxun.luxunanimation.bean;

/**
 * Created by wufeiyang on 16/5/19.
 */
public class Comment {

    /**
     * cid : 90
     * rate : 0
     * cur : null
     * text : 为甚全网只有这儿有
     * data :
     * liked : 0
     * agent : Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36
     * created : 1462671764
     * user : {"uid":"2533","name":"針慎","avatar":"","up":"0"}
     */

    private String cid;
    private String rate;
    private String cur;
    private String text;
    private String data;
    private String liked;
    private String agent;
    private String created;
    /**
     * uid : 2533
     * name : 針慎
     * avatar :
     * up : 0
     */

    private UserEntity user;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public static class UserEntity {
        private String uid;
        private String name;
        private String avatar;
        private String up;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }
    }
}
