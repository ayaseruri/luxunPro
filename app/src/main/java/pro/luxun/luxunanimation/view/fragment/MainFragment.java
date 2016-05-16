package pro.luxun.luxunanimation.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.presenter.adapter.MainFragmentAdapter;
import pro.luxun.luxunanimation.utils.MainJasonHelper;

/**
 * Created by wufeiyang on 16/5/7.
 */

@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment{

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;

    private MainFragmentAdapter mAdapter;
    private GridLayoutManager mLinearLayoutManager;
    private MainJson mMainJson;

    @AfterViews
    void init(){
        mMainJson = MainJasonHelper.getMainJsonCache();
        mAdapter = new MainFragmentAdapter(mActivity, mMainJson);
        mLinearLayoutManager = new GridLayoutManager(mActivity, 3);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mLinearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanCount = mLinearLayoutManager.getSpanCount();
                if(0 == position){
                    return spanCount;
                }else {
                    return mAdapter.isNormalItem(position) ? 1 : spanCount;
                }
            }
        });
    }
}
