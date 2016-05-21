package pro.luxun.luxunanimation.view.fragment;

import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;

/**
 * Created by wufeiyang on 16/5/21.
 */
@EFragment(R.layout.fragment_list)
public class LikeCommentFragment extends BaseFragment {

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;

    @AfterViews
    void init(){

    }
}
