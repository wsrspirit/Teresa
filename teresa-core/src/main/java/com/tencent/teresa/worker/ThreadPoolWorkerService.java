package com.tencent.teresa.worker;


import co.paralleluniverse.fibers.SuspendExecution;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.handler.TaskHandler;
import com.tencent.teresa.limiter.DefaultPacketLimiter;
import com.tencent.teresa.limiter.IoPacketLimiter;
import com.tencent.teresa.processor.Processor;
import com.tencent.teresa.utils.U;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolWorkerService extends AbstractWorkerService {
	static final Logger logger = LoggerFactory.getLogger(ThreadPoolWorkerService.class);
	private ExecutorService tasks;
	private static final int DEFAULT_THREAD_COUNT = 16;

	public ThreadPoolWorkerService() {
		this(U.DEFAULT_THREADS);
	}

	public ThreadPoolWorkerService(int threadCount) {
		this(new DefaultPacketLimiter(),threadCount);

	}

	public ThreadPoolWorkerService(IoPacketLimiter limiter) {
		this(limiter, U.DEFAULT_THREADS);
	}

	public ThreadPoolWorkerService(IoPacketLimiter limiter, int threadCount) {
		super(limiter);
		this.tasks = Executors.newFixedThreadPool(threadCount,new ThreadFactory() {
			final AtomicInteger TID = new AtomicInteger(0);
			final String GROUP_NAME = "WORK_THREAD_";
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, GROUP_NAME + TID.getAndIncrement());
			}
		});
	}

	@Override
	public void doDispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor, IoPacketLimiter packetLimiter) {
		tasks.execute(() -> {
			try {
				taskHandler.handler(ch, msg, processor, packetLimiter);
			} catch (SuspendExecution suspendExecution) {
				logger.error("thread mode should not accure this err, make sure you use quasar agent with use CoroutineWorkerService");
			}
		});
	}
}


