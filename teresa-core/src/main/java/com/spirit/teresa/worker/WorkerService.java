package com.spirit.teresa.worker;

import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.processor.Processor;
import io.netty.channel.Channel;

import java.util.concurrent.Executor;

public interface WorkerService {
	void dispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor);
	Executor getExecutor();
}
