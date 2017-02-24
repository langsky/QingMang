package com.github.langsky.qingmang.event;


import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * RxBus
 * Created by swd1 on 17-1-16.
 */

public class RxBus {

    private final Subject<Object, Object> bus;

    //RxBus should be an instance
    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus instance() {
        return RxHolder.instance;
    }

    private static class RxHolder {
        private static final RxBus instance = new RxBus();
    }

    public void post(Object object) {
        bus.onNext(object);
    }

    public <T> Observable<T> toObservable(final Class<T> eventType) {
        return bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return eventType.isInstance(o);
            }
        }).cast(eventType);
    }


}
