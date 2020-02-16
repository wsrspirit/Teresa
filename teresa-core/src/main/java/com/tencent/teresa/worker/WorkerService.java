package com.tencent.teresa.worker;

import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.processor.Processor;
import io.netty.channel.Channel;

import java.util.concurrent.Executor;

public interface WorkerService {
	void dispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor);
	Executor getExecutor();
	void setWorkerMode(String workerMode);
}
