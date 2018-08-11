package com.tencent.teresa.worker;

import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.processor.Processor;
import io.netty.channel.Channel;

public interface WorkerService {
	void dispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor);
}
