# test-springcloud-wallet-user

一、项目结构

    wallet : 整体父工程

     wallet-api : 公共子模块 entitygit add 

     wallet-provider-8001 : 微服务提供者8001

     wallet-provider-8002 : 微服务提供者8002

     wallet-provider-8003 : 微服务提供者8003
    
二、自我保护机制：

        默认情况下，如果 EurekaServer在一定时间内没有接收到某个微服务实例的心跳， Eurekaservery将会注销该实例(默认90秒)。
     但是当网络分区故障发生时，微服务与 EurekaServer之间无法正常通信，以上行为可能变得非常危险了—因为微服务本身其实
     是健康的，此时本不应该注销这个微服务。 Eureka通过“自我保护模式”来解决这个问题——当 EurekaServer节点在短时间内丢
     失过多客户端时(可能发生了网络分区故障)，那么这个节点就会进入自我保护模式。一旦进入该模式， EurekaServer就会保护服
     务注册表中的信息，不再删除服务注册表中的数据(也就是不会注销任何微服务)。当网络故障恢复后，该 Eureka Server节点会
     自动退出自我保护模式。
        在自我保护模式中， Eureka server会保护服务注册表中的信息，不再注销任何服务实例。当它收到的心跳数重新恢复到阈值以上
     时，该 Eureka Server节点就会自动退出自我保护模式。它的设计哲学就是宁可保留错误的服务注册信息，也不盲目注销任何可能
     健康的服务实例。一句话讲解：好死不如赖活着
     综上，自我保护模式是种应对网络异常的安全保护措施。它的架构哲学是宁可同时保留所有微服务(健康的微服务和不健康的微
     服务都会保留)，也不盲目注销任何健康的微服务。使用自我保护模式，可以让 Eureka集群更加的健壮、稳定。
     在 Spring Cloud中，可以使用eureka.server.enable-self-preservation = false 禁用自我保护模式。

三、服务发现

    对于注册进eureka里面的微服务，可以通过服务发现来获得该服务的信息。
    
        @EnableDiscoveryClient  主启动类添加注解
    
        @Autowired
        private DiscoveryClient client;  //服务发现
        
        @RequestMapping(value = "/user/discovery", method = RequestMethod.GET)
            public Object discovery()
            {
                List<String> list = client.getServices();   //eureka中的微服务
                System.out.println("**********" + list);
        
                List<ServiceInstance> srvList = client.getInstances("WALLET-PROVIDER"); //获取名称为"WALLET-PROVIDER"的微服务
                for (ServiceInstance element : srvList) {
                    System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
                            + element.getUri());
                }
                return this.client;
            }
            
四、eureka集群配置

    1、做域名映射   hosts文件
    2、微服务注册进eureka集群
    
    **文件配置：**
    
        ~~7001(7002/7003 -- 注册进eureka集群) application.yml:~~   
        
        server:
          port: 7001
        
        eureka:
          instance:
            hostname: eureka7001.com   #eureka服务端的实例名称
          client:
            register-with-eureka: false     # false表示不向注册中心注册自己
            fetch-registry: false           #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
            service-url:
               #defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/       #设置与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址（单机）。
              defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/
              
    
五、Ribbon负载均衡

    Spring Cloud Ribbon是基于 Netflix Ribbon实现的一套 客户端 负载均衡的工具。
    简单的说， Ribbon是Netflix发布的开源项目，主要功能是提供客户端的软件负载均衡算法，将Netflix的中间层服务连接在一起。
    Ribbon客户端组件提供一系列完善的配置项如连接超时，重试等。简单的说，就是在配置文件中列出 Load Balancer(简称LB)后
    面所有的机器， Ribbon会自动的帮助你基于某种规则(如简单轮询，随机连接等)去连接这些机器。我们也很容易使用 Ribbon实
    现自定义的负载均衡算法。

    配置：
        80 - pom.xml：
         <!-- Ribbon相关 -->
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-eureka</artifactId>
                </dependency>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-ribbon</artifactId>
                </dependency>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-config</artifactId>
                </dependency>
                
        config配置类添加注解：
            @LoadBalanced  //Spring Cloud Ribbon是基于Netflix Ribbon实现的一套"客户端"负载均衡的工具
            
        添加wallet-provider-8002/8003两个工程：
            application.yml修改数据库连接
            
            
        Ribbon使80通过eureka找到微服务"WALLET-PROVIDER"（三个工程），默认使用轮询算法访问服务接口，即provider7001/provider7002/provider7003轮询访问
        
        Ribbon在工作时分成两步
            第一步先选择EurekaServer，它优先选择在同一个区域内负载较少的server。
            第二步再根据用户指定的策略，在从server取到的服务注册列表中选择一个地址。
            其中Ribbon提供了多种策略：比如轮询、随机和根据响应时间加权。
            
        Ribbon核心组件IRule:
            重写算法：
                配置类ConfigBean中添加：
                
                    @Bean
                    public IRule myRule() {
                        //return new RoundRobinRule();  //"轮询算法"
                        return new RandomRule();        //达到的目的，用我们重新选择的"随机算法"替代默认的轮询
                    }
            
        
    