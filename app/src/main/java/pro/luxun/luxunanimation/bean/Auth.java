package pro.luxun.luxunanimation.bean;

import java.io.Serializable;

/**
 * Created by wufeiyang on 16/5/17.
 */
public class Auth {

    /**
     * token : 53b8812a20fc8ae0f1de504a0f73c3e1
     * text : "Hello,this is Android!"
     * type : 1
     * created : 1463478840
     * user : {"uid":"2716","wbid":"2811954412","name":"Ayaseruri","avatar":"http://tva2.sinaimg.cn/crop.0.0.108.108.50/a79b04ecgw1ecnjcjyx41j2033031jr9.jpg","des":"","url":"http://weibo.com/2811954412","lv":"1","up":"0","sss":"e64dfb8b66e5dc47195f3e2885498caf"}
     */

    private String token;
    private String text;
    private String type;
    private String created;
    /**
     * uid : 2716
     * wbid : 2811954412
     * name : Ayaseruri
     * avatar : http://tva2.sinaimg.cn/crop.0.0.108.108.50/a79b04ecgw1ecnjcjyx41j2033031jr9.jpg
     * des :
     * url : http://weibo.com/2811954412
     * lv : 1
     * up : 0
     * sss : e64dfb8b66e5dc47195f3e2885498caf
     */

    private UserEntity user;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public static class UserEntity implements Serializable{
        private String uid;
        private String wbid;
        private String name;
        private String avatar;
        private String des;
        private String url;
        private String lv;
        private String up;
        private String sss;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getWbid() {
            return wbid;
        }

        public void setWbid(String wbid) {
            this.wbid = wbid;
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

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLv() {
            return lv;
        }

        public void setLv(String lv) {
            this.lv = lv;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public String getSss() {
            return sss;
        }

        public void setSss(String sss) {
            this.sss = sss;
        }
    }
}
