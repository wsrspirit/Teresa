package com.spirit.teresa.timeout;

import java.util.concurrent.Future;

public interface TimeoutManager {
    Future<?> watch(Runnable task, long timeout);
}
