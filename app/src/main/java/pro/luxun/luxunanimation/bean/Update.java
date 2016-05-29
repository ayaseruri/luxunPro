package pro.luxun.luxunanimation.bean;

/**
 * Created by wufeiyang on 16/5/29.
 */
public class Update {
    private String detail;
    private int ver_code;
    private boolean is_fouce_update;
    private String download_url;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getVer_code() {
        return ver_code;
    }

    public void setVer_code(int ver_code) {
        this.ver_code = ver_code;
    }

    public boolean is_fouce_update() {
        return is_fouce_update;
    }

    public void setIs_fouce_update(boolean is_fouce_update) {
        this.is_fouce_update = is_fouce_update;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
}
