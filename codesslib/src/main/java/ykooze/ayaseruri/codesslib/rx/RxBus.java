package ykooze.ayaseruri.codesslib.rx;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.SerializedSubscriber;

/**
 * Created by jk.yeo on 16/3/1 17:47.
 * 基于 RxJava 简单的 EventBus
 */
public class RxBus {

    private static volatile RxBus mDefaultInstance;

    private RxBus() {
    }

    public static RxBus getDefault() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    private final FlowableProcessor<Object> _bus = PublishProcessor.create().toSerialized();

    public void send(String tag, Object object) {
        EventObject eventObject = new EventObject(tag, object);
        new SerializedSubscriber<>(_bus).onNext(eventObject);
    }

    public interface ReceiveOnUiThread {
        void OnReceive(String tag, Object object);
    }

    public interface ReceiveOnIOThread {
        void OnReceive(String tag, Object object);
    }

    public Disposable registerOnUiThread(final ReceiveOnUiThread receiveOnUiThread) {
        return _bus.ofType(EventObject.class).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EventObject>() {
                    @Override
                    public void accept(EventObject eventObject) throws Exception {
                        receiveOnUiThread.OnReceive(eventObject.getTag(), eventObject.getObject());
                    }
                });
    }

    public Disposable registerOnIOThread(final ReceiveOnIOThread receiveOnIOThread) {
        return _bus.ofType(EventObject.class).observeOn(RxUtils.getSchedulers())
                .subscribe(new Consumer<EventObject>() {
                    @Override
                    public void accept(EventObject eventObject) throws Exception {
                        receiveOnIOThread.OnReceive(eventObject.getTag(), eventObject.getObject());
                    }
                });
    }

    private static class EventObject {
        private String tag;
        private Object object;

        public EventObject(String tag, Object object) {
            this.tag = tag;
            this.object = object;
        }

        public String getTag() {
            return tag;
        }

        public Object getObject() {
            return object;
        }
    }
}
