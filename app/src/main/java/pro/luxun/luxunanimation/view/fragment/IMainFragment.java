package pro.luxun.luxunanimation.view.fragment;

import pro.luxun.luxunanimation.bean.MainJson;

/**
 * Created by wufeiyang on 16/5/7.
 */
public interface IMainFragment {
    void onRefreshList(MainJson mainJson);
    void onFilterList(String keyword);
}
