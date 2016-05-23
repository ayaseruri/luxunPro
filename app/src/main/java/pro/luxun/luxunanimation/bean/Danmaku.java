package pro.luxun.luxunanimation.bean;

/**
 * Created by wufeiyang on 16/5/23.
 */
public class Danmaku {

    /**
     * start : 82.67
     * text : 正文
     * color : 颜色
     * y : 19
     * type : none
     */

    private double start;
    private String text;
    private String color;
    private int y;
    private String type;

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
