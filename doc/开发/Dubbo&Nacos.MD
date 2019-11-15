一、分步实施
定义：Service服务分为 Api Service 服务和 业务逻辑 Service 服务
（1）、将来 API Service 与 业务逻辑 Service 分别注入到 Nacos, 最终目标就是 Api 的服务与业务逻辑服务也分开启动
（2）、API Service 手动注入，在API框架里面再次开发
（3）、业务逻辑 Service 使用 Dubbo RPC，直接整合
（4）、业务逻辑 Service 作为 Dubbo 服务的提供者，可以单独启动应用，那么 Api Service 调用 Dubbo Service 需要修改注解（reference），
如果前期Api Service 与 业务逻辑 Service 整合在一起，那么 业务逻辑Service 需要增加2个注解，也就是Spring的注解和Dubbo的注解


二、实施过程
1、POM增加
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>dubbo-registry-nacos</artifactId>
        <version>0.0.1</version>
    </dependency>   
    
    <!-- Dubbo dependency -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>dubbo</artifactId>
        <version>2.6.5</version>
    </dependency>
    
    <!-- Alibaba Spring Context extension -->
    <dependency>
        <groupId>com.alibaba.spring</groupId>
        <artifactId>spring-context-support</artifactId>
        <version>1.0.2</version>
    </dependency>


2、增加配置 dubbo-config.properties
dubbo.application.name = your-dubbo-application
dubbo.registry.address = nacos://10.20.153.10:8848

dubbo.protocol.name = dubbo
dubbo.protocol.port = -1

3、Application增加
@EnableDubbo(scanBasePackages = "com.alibaba.dubbo.demo.service")
@PropertySource(value = "classpath:/provider-config.properties")

4、业务逻辑Service把 Spring的Service注解改为 @Component，增加 com.alibaba.dubbo.config.annotation.Service 注解

5、消费引用（远程Service）
@Reference(version = "${demo.service.version}")
private DemoService demoService;