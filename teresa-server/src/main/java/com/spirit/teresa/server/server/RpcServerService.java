package com.spirit.teresa.server.server;

import com.spirit.teresa.serializer.Serializer;
import com.spirit.teresa.worker.WorkerService;

public interface RpcServerService {

    void start() throws Exception;

    void stop();

    void setServerAddress(String serverAddress);

    void setWorkerService(WorkerService workerService);

    void setSerializer(Serializer serializer);
}
