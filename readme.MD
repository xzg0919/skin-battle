一、开发前必读：
适应微服务环境，整个工程的结构定义如下：

1、collect-module-common   
所有工具类

2、collect-module-api            
所有实体类（PO，VO）以及定义的接口，包含所有子模块的（权益、分类查询，哒哒哒的）

3、collect-module-core
收呗实现Service和Mapper的代码，将来在拆分为几个模块： member ，order 等。 

4、collect-boot
收呗 boot 启动应用




二、注意包名规范
1、collect-module-common 里面的包名规范
com.tzj.collect.common.具体的工具类型


2、