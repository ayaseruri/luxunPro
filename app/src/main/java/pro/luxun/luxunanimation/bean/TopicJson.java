package pro.luxun.luxunanimation.bean;

import java.io.Serializable;

/**
 * Created by wufeiyang on 16/5/16.
 */
public class TopicJson implements Serializable{

    /**
     * created : 1600000010
     * title : 一六年春 原创动画特辑
     * bangumis : 超时空要塞Δ
     宇宙巡警露露子
     迷家
     黑色残骸
     羁绊者
     * text : 你问我原创好不好，
     那肯定是极好的呀！
     * type : list
     */

    private String created;
    private String title;
    private String bangumis;
    private String text;
    private String type;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBangumis() {
        return bangumis;
    }

    public void setBangumis(String bangumis) {
        this.bangumis = bangumis;
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
}
