package com.tencent.teresa.server.server;

import com.tencent.teresa.serializer.Serializer;
import com.tencent.teresa.worker.WorkerService;

public interface RpcServerService {

    void start() throws Exception;

    void stop();

    void setServerAddress(String serverAddress);

    void setWorkerService(WorkerService workerService);

    void setSerializer(Serializer serializer);
}
