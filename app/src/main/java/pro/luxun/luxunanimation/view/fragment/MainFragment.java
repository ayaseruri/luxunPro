package pro.luxun.luxunanimation.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.global.IntentConstant;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;
import pro.luxun.luxunanimation.presenter.adapter.MainFragmentAdapter;
import pro.luxun.luxunanimation.utils.GridSpacingItemDecoration;
import pro.luxun.luxunanimation.utils.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/7.
 */

@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment{

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;

    private MainFragmentAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private MainJson mMainJson;

    @AfterViews
    void init(){
        mMainJson = getArguments().getParcelable(IntentConstant.INTENT_MAIN_JSON);
        mAdapter = new MainFragmentAdapter(mMainJson);
        mLinearLayoutManager = new GridLayoutManager(mActivity, 3);

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, LocalDisplay.dp2px(4), false));
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @AfterTextChange(R.id.search)
    void onSearch(TextView searchBox, Editable keywords){
        if(null != mAdapter){
            mAdapter.addFilter(keywords.toString());
        }
    }
}
