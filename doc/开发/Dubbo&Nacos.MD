1、POM增加3个依赖

<!-- Dubbo Nacos registry dependency -->
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


3、Application增加
@EnableDubbo(scanBasePackages = "com.alibaba.dubbo.demo.service")
@PropertySource(value = "classpath:/provider-config.properties")


4、消费引用
@Reference(version = "${demo.service.version}")
private DemoService demoService;