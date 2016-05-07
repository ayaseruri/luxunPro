package pro.luxun.luxunanimation.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;

/**
 * Created by wufeiyang on 16/5/7.
 */

@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment implements IMainFragment {

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @AfterViews
    void init(){
        mLinearLayoutManager = new GridLayoutManager(mActivity, 3);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    public void onRefreshList(MainJson mainJson) {


        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onFilterList(String keyword) {

    }
}
