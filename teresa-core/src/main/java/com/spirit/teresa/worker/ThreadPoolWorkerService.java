package com.spirit.teresa.worker;


import co.paralleluniverse.fibers.SuspendExecution;
import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.limiter.IoPacketLimiter;
import com.spirit.teresa.utils.U;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolWorkerService extends AbstractWorkerService {
	static final Logger logger = LoggerFactory.getLogger(ThreadPoolWorkerService.class);
	protected Executor executor;

	public ThreadPoolWorkerService() {
		this(U.DEFAULT_THREADS);
	}

	public ThreadPoolWorkerService(int threadCount) {
		super();
		executor = Executors.newFixedThreadPool(threadCount,new ThreadFactory() {
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
		executor.execute(() -> {
			try {
				processor.process(msg, ch);
				packetLimiter.release(ch,msg,processor);
			} catch (Throwable throwable) {
				logger.error("ThreadPoolWorkerService dispatch ioPacket processor err",throwable);
//				logger.error("thread mode should not accure this err, make sure you use quasar agent with use CoroutineWorkerService");
			}
		});
	}

	@Override
	public Executor getExecutor() {
		return executor;
	}
}


