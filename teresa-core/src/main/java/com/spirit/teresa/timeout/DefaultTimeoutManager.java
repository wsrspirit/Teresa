package com.spirit.teresa.timeout;


import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultTimeoutManager implements TimeoutManager {
    protected ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);
    @Override
    public Future<?> watch(Runnable task, long timeout) {
        return scheduled.schedule(task, timeout, TimeUnit.MILLISECONDS);
    }
}
