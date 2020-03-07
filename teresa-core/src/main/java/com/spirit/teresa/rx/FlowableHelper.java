package com.spirit.teresa.rx;

import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.ReplayProcessor;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlowableHelper {
    private static final Logger logger = LoggerFactory.getLogger("FlowableHelper");
    private Scheduler scheduler;
    private Executor executor;

    public FlowableHelper(int executorSize) {
        executor = Executors.newFixedThreadPool(executorSize);
        scheduler = Schedulers.computation();
    }

    public <T> Flowable<T> flowLift(Callable<T> callable) {
        return Flowable.defer(() -> {

            FlowableProcessor<T> flowableProcessor = ReplayProcessor.create(1);

            executor.execute(() -> {
                try {
                    T result = callable.call();
                    flowableProcessor.onNext(result);
                    flowableProcessor.onComplete();
                } catch (Exception e) {
                    flowableProcessor.onError(e);
                }
            });

            return flowableProcessor.observeOn(scheduler);
        });
    }

    public <T> Flowable<T> flowLiftReturnEmptyIfNull(Callable<T> callable) {
        return Flowable.defer(() -> {
            FlowableProcessor<Optional<T>> flowableProcessor = ReplayProcessor.create(1);

            executor.execute(() -> {
                try {
                    T result = callable.call();
                    flowableProcessor.onNext(Optional.ofNullable(result));
                    flowableProcessor.onComplete();
                } catch (Exception e) {
                    flowableProcessor.onError(e);
                }
            });

            return flowableProcessor.observeOn(scheduler).filter(Optional::isPresent).map(Optional::get);
        });
    }
}
