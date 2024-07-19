```
大家好，本项目是作者用于一台云服务器的情况下，又不想购买多台云服务，但又有不少本地服务器的背景下编写的代码
代码中使用netty 与服务端简历长连接，并实时发送消息获取需要的服务，由客户端进行计算在返回给服务端中专给目标客户端
这样就可以实现一台服务器，多台本地电脑提供计算或耗时服务，从而减少成本

如何配置：  
mx-rpc:<br>
    #客户端配置消息<br>
    client: <br>
    #客户端ID标识<br>
    id: 1011  <br>
    #提供的服务名称<br>
    serviceName: ALL<br>
    #访问权重 访问策略为权重方式下生效<br>
    weight: 1<br>
    link:<br>
        #服务端IP<br>
        host: 127.0.0.1  <br>
        #服务端端口<br>
        port: 8081  <br>
        #服务端密码<br>
        password: 123456789<br>
    #服务端配置消息    <br>
    server:<br>
        #监听端口号<br>
        port: 8081<br>
        #服务端密码<br>
        password: 123456789<br>

启动类上增加注解：

@EnableMxRpcServer(visitStrategy = ProviderVisitStrategyEnums.NEXT)

会启动一个RPC服务端 访问策略目前有  轮询 HASH 注解
用户验证客户端管理客户端 并转发消息

@EnableMxRpcClient

会启动一个RPC客户端 用于访问和提供服务

@MxRpcResource(name="服务名称")

属性调用这个标签会生成一个代理对象，进行远程访问调用

Maven 地址：

<dependency>
    <groupId>io.gitee.mxspace</groupId>
    <artifactId>MxRpc</artifactId>
    <version>1.0-RELEASE</version>
</dependency>
```