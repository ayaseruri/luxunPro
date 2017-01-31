package ykooze.ayaseruri.codesslib.ui;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.Callable;

import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ykooze.ayaseruri.codesslib.adapter.RecyclerAdapter;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

/**
 * Created by wufeiyang on 16/6/17.
 */
public class CommonRecyclerView extends RecyclerView {

    public static final int TYPE_REFRESH = 0;
    public static final int TYPE_LOADMORE = 1;
    public static final int TYPE_FIRSTIN = 2;
    public static final int TYPE_END = 3;

    private int mPreLoadPadding = 10;
    private boolean isLoading, isRefreshing;
    private Runnable mRefreshTask;
    private boolean isEnd;


    public CommonRecyclerView(Context context) {
        super(context);
    }

    public CommonRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public <T> void init(final ICommonRecyclerView<T> iCommonRecyclerView
                    , final RecyclerAdapter<T> recyclerAdapter){
        setAdapter(recyclerAdapter);

        Observable.defer(new Callable<ObservableSource<List<T>>>() {
            @Override
            public ObservableSource<List<T>> call() throws Exception {
                final List<T> ts = iCommonRecyclerView.getFirstInData();
                return null == ts ? Observable.<List<T>>empty() : Observable.create(new ObservableOnSubscribe<List<T>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                        e.onNext(ts);
                        e.onComplete();
                    }
                });
            }
        }).compose(RxUtils.<List<T>>applySchedulers())
                .compose(RxLifecycleAndroid.<List<T>>bindView(this))
                .subscribe(new Observer<List<T>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                iCommonRecyclerView.uiLoadingStart(TYPE_FIRSTIN);
                            }
                        });
                    }

                    @Override
                    public void onNext(List<T> ts) {
                        isEnd = iCommonRecyclerView.isEnd(ts);
                        iCommonRecyclerView.onHandleData(ts);
                        recyclerAdapter.refresh(ts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iCommonRecyclerView.uiLoadingError(TYPE_FIRSTIN, e);
                    }

                    @Override
                    public void onComplete() {
                        iCommonRecyclerView.uiLoadingComplete(TYPE_FIRSTIN);
                    }
                });

        mRefreshTask = new Runnable() {
            @Override
            public void run() {
                Observable.defer(new Callable<ObservableSource<List<T>>>() {
                    @Override
                    public ObservableSource<List<T>> call() throws Exception {
                        final List<T> ts = iCommonRecyclerView.getRefreshData();
                        return null == ts ? Observable.<List<T>>empty() : Observable.create(new ObservableOnSubscribe<List<T>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                                e.onNext(ts);
                                e.onComplete();
                            }
                        });
                    }
                }).compose(RxUtils.<List<T>>applySchedulers())
                        .compose(RxLifecycleAndroid.<List<T>>bindView(CommonRecyclerView.this))
                        .subscribe(new Observer<List<T>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                iCommonRecyclerView.uiLoadingStart(TYPE_REFRESH);
                            }

                            @Override
                            public void onNext(List<T> ts) {
                                isEnd = iCommonRecyclerView.isEnd(ts);
                                iCommonRecyclerView.onHandleData(ts);
                                recyclerAdapter.refresh(ts);
                            }

                            @Override
                            public void onError(Throwable e) {
                                iCommonRecyclerView.uiLoadingError(TYPE_REFRESH, e);
                                isRefreshing = false;
                            }

                            @Override
                            public void onComplete() {
                                iCommonRecyclerView.uiLoadingComplete(TYPE_REFRESH);
                                isRefreshing = false;
                            }
                        });
            }
        };

        clearOnScrollListeners();
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int totalItemCount = layoutManager.getItemCount();
                    if(totalItemCount > 0 && isEnd
                            && getLastVisibleItemPosition(recyclerView) == totalItemCount - 1 ){
                        iCommonRecyclerView.uiLoadingComplete(TYPE_END);
                    }else if(!isEnd && !isLoading){
                        if(getLastVisibleItemPosition(recyclerView) >= totalItemCount - 1 - mPreLoadPadding){
                            isLoading = true;
                            Observable.defer(new Callable<ObservableSource<List<T>>>() {
                                @Override
                                public ObservableSource<List<T>> call() throws Exception {
                                    final List<T> ts = iCommonRecyclerView.getLoadMoreData();
                                    return null == ts ? Observable.<List<T>>empty() : Observable.create(new ObservableOnSubscribe<List<T>>() {
                                        @Override
                                        public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                                            e.onNext(ts);
                                            e.onComplete();
                                        }
                                    });
                                }
                            })
                                    .compose(RxUtils.<List<T>>applySchedulers())
                                    .compose(RxLifecycleAndroid.<List<T>>bindView(CommonRecyclerView.this))
                                    .subscribe(new Observer<List<T>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            iCommonRecyclerView.uiLoadingStart(TYPE_LOADMORE);
                                        }

                                        @Override
                                        public void onNext(List<T> ts) {
                                            if(iCommonRecyclerView.isEnd(ts)){
                                                isEnd = true;
                                            }
                                            iCommonRecyclerView.onHandleData(ts);
                                            recyclerAdapter.add(ts);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            iCommonRecyclerView.uiLoadingError(TYPE_LOADMORE, e);
                                            isLoading = false;
                                        }

                                        @Override
                                        public void onComplete() {
                                            iCommonRecyclerView.uiLoadingComplete(TYPE_LOADMORE);
                                            isLoading = false;
                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public int getLastVisibleItemPosition(RecyclerView recyclerView){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager)
                    .findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager
                    = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
            return findMax(lastPositions);
        }
        return -1;
    }

    public void refreshData(){
        if(!isRefreshing){
            synchronized (CommonRecyclerView.class){
                if(!isRefreshing){
                    isRefreshing = true;
                    post(mRefreshTask);
                }
            }
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public int getPreLoadPadding() {
        return mPreLoadPadding;
    }

    public void setPreLoadPadding(int preLoadPadding) {
        mPreLoadPadding = preLoadPadding;
    }

    public interface ICommonRecyclerView<T>{
        List<T> getFirstInData() throws Exception;
        List<T> getRefreshData() throws Exception;
        List<T> getLoadMoreData() throws Exception;
        boolean isEnd(List<T> ts);
        void onHandleData(List<T> ts);
        void uiLoadingStart(@LoadingType int loadingType);
        void uiLoadingError(@LoadingType int loadingType, Throwable e);
        void uiLoadingComplete(@LoadingType int loadingType);
    }

    @IntDef({TYPE_FIRSTIN, TYPE_LOADMORE, TYPE_REFRESH, TYPE_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadingType{

    }
}
