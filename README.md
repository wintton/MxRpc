大家好，本项目是作者用于一台云服务器的情况下，又不想购买多台云服务，但又有不少本地服务器的背景下编写的代码
代码中使用netty 与服务端简历长连接，并实时发送消息获取需要的服务，由客户端进行计算在返回给服务端中专给目标客户端
这样就可以实现一台服务器，多台本地电脑提供计算或耗时服务，从而减少成本

如何配置：  
mx-rpc:
    #客户端配置消息
    client:
    #客户端ID标识
    id: 1011  
    #提供的服务名称
    serviceName: ALL
    #访问权重 访问策略为权重方式下生效
    weight: 1
    link:
        #服务端IP
        host: 127.0.0.1  
        #服务端端口
        port: 8081  
        #服务端密码
        password: 123456789
    #服务端配置消息    
    server:
        #监听端口号
        port: 8081
        #服务端密码
        password: 123456789

启动类上增加注解：

@EnableMxRpcServer(visitStrategy = ProviderVisitStrategyEnums.NEXT)

会启动一个RPC服务端 访问策略目前有  轮询 HASH 注解
用户验证客户端管理客户端 并转发消息

@EnableMxRpcClient

会启动一个RPC客户端 用于访问和提供服务
