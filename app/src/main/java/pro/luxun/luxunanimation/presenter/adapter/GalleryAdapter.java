package pro.luxun.luxunanimation.presenter.adapter;

import android.support.annotation.UiThread;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wufeiyang on 16/5/16.
 */
public abstract class GalleryAdapter<T, V extends View> extends PagerAdapter {

    private List<T> datas = new ArrayList<>();

    @Override
    public V instantiateItem(ViewGroup container, int position) {
        return bindView(container, datas.get(position));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeViewAt(position);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @UiThread
    public void refresh(List<T> list){
        datas.clear();
        datas.addAll(list);
        notifyDataSetChanged();
    }

    public abstract V bindView(ViewGroup container, T t);
}
