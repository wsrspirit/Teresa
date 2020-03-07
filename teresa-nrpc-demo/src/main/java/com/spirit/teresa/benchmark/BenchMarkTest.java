package com.spirit.teresa.benchmark;

import com.spirit.teresa.client.DemoNrpcService;
import com.spirit.teresa.client.client.RpcClientService;
import com.spirit.teresa.client.TeresaClientConfig;
import com.spirit.teresa.packet.NrpcPacket;
import com.spirit.teresa.pb.AddExperienceReq;
import com.spirit.teresa.pb.AddExperienceRsp;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class BenchMarkTest {
    private static final Logger logger = LoggerFactory.getLogger(BenchMarkTest.class);

    private DemoNrpcService demoService;
    private ApplicationContext applicationContext;

    @Setup
    public void setup() {
        applicationContext = new AnnotationConfigApplicationContext(TeresaClientConfig.class);
        //使用注解形式实现
        demoService = (DemoNrpcService) applicationContext.getBean("demoNrpcService");
    }

    @TearDown
    public void tearDown() {
        applicationContext.getBean(RpcClientService.class).shutdown();
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testRpcBenchMark(BenchMarkTest benchMarkTest) {
        NrpcPacket request = new NrpcPacket();
        AddExperienceRsp addExperienceRsp = benchMarkTest.getDemoService().addExp(new AddExperienceReq(),request);
        logger.debug("testRpcBenchMark rsp threadName {} level {} result {}",Thread.currentThread().getName()
                ,addExperienceRsp.getLevel(),addExperienceRsp.getResult());
    }

    public DemoNrpcService getDemoService() {
        return demoService;
    }

    public void setDemoService(DemoNrpcService demoService) {
        this.demoService = demoService;
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(BenchMarkTest.class.getSimpleName())
//                .jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+LogCompilation", "-XX:+TraceClassLoading", "-XX:+PrintAssembly")
//                .measurementIterations(5)
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(1)
                .build();

        new Runner(opt).run();
    }
}
