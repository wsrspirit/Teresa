# Teresa
![Teresa](https://img.alicdn.com/tfs/TB1sgYxc9slXu8jSZFuXXXg7FXa-1078-602.jpg)

从0到1开发Java微服务框架，见微知著，麻雀虽小五脏俱全

- 基于Netty。TCP、UDP上定义网络层数据协议
- 基于Protobuf。包装RPC数据协议；使用ProtoStuff优化代码结构及编解码速度
- 跨语言。使用Protobuf作为RPC封装协议；基于大小命令字定义路由寻址
-  高性能。Reactor基础上增加多线程、异步化（RxJava）、协程（Quasar）处理器；客户端连接池，连接复用；减少包传递时的内存拷贝
- 好用。尽量符合Java语言的RPC惯性；支持SpringBoot
- 异步化。请求和响应天然支持异步化，改造轻松；RPC数据协议包结构拓展性强，异步化依赖包头拓展信息而不是线程上下文；
- 可靠。超时处理，过载保护，异常传递，地址可达检测，熔断保护
- 拓展。RPC请求和响应的多个阶段留有充分的SPI接口
- 运维。优雅上下线