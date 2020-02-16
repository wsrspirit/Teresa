package com.tencent.teresa.client;

import com.tencent.teresa.client.future.SendPacketFuture;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.processor.Processor;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientProcessor implements Processor<IoPacket,IoPacket> {
    private static final Logger logger = LoggerFactory.getLogger(ClientProcessor.class);
    @Override
    public IoPacket process(IoPacket ioPacket, Channel ioChannel) throws Exception {

//        SendPacketFuture<IoPacket> future = (SendPacketFuture<IoPacket>) channelManager.remove(ioChannel.remoteAddress(),ioPacket.getSeq());
//        if (future == null) {
//            logger.error("subCmd {} seq {} routerId {} response can not find it's future could not be found, mostly because remote server rpc timeout"
//                    ,ioPacket.getCmd(),ioPacket.getSeq(),ioPacket.getRouterId());
//            return;
//        }
//        future.setResult(ioPacket);
//        future.getTimeoutTask().cancel(false);
//        future.callback();
        return null;
    }
}
