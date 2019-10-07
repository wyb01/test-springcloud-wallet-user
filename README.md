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
            
六、Feign负载均衡

    Feign是一个声明式 WebService客户端。使 Feign能让编写Web Service客户端更加简单，它的使用方法是定义一个接口，然后
    在上面添加注解，同时也支持JAX-RS标准的注解。Feign也支持可拔插式的编码器和解码器。Spring Cloud对Feign进行了封装，
    使其支持了Spring MVC标准注解和 HttpMessageConverters。 Feign可以与Eureka和Ribbon组合使用以支持负载均衡。
    
    Feign是一个声明式的Web服务客户端，使得编写Web服务客户端变得非常容易。
    只需要创建一个接口，然后在上面添加注解即可。
    
    Feign是怎么出来的？
     1、大部分大家都可以接受直接调用我们的微服务来进行访问
         private static final String REST_URL_PREFIX = "http://localhost:8001"; 
             修改为：
         private static final String REST_URL_PREFIX = "http://WALLET-PROVIDER"; 
        
     2、大家目前都习惯面向接口面向接口编程，比如WebService接口,比如我们的DAO接口，这个已经是大家的规范
        2.1 微服务名字获得调用地址
        2.2 就是通过接口+注解，获得我们的调用服务
        
        适应社区其他程序员提出的，还是统一的面向接口编程的套路----Feign
        
        只需要创建一个接口，然后在上面添加注解即可。
        
           @Mapper
           public interface DeptDao{
           
           }
           
    Feign能干什么
    
       Feign旨在使编写Java Http客户端变得更容易。
       前面在使用 Ribbon+RestTemplate时，利用RestTemplate对http请求的封装处理，形成了一套模版化的调用方法。但是在实际
       开发中，由于对服务依赖的调用可能不止一处，往往一个接口会被多处调用，所以通常都会针对每个微服务自行封装些客户端
       类来包装这些依赖服务的调用。所以，Feign在此基础上做了进一步封装，由他来帮助我们定义和实现依赖服务接口的定义。在
       Feign的实现下，我们只需创建一个接口并使用注解的方式来配置它(以前是Dao接口上面标注Mapper注解,现在是个微服务接口
       上面标注一个Feign注解即可)，即可完成对服务提供方的接口绑定，简化了使用 Spring cloud Ribbon时，自动封装服务调用客户
       端的开发量。
       
       Feign集成了Ribbon
          利用Ribbon维护了wallet-provider的服务列表信息，并且通过轮询实现了客户端的负载均衡。而与Ribbon不同的是，通过Feign只需要定义服务
          绑定接口且以声明式（@FeignClient）的方法，优雅而简单的实现了服务调用
       
    Feign工程创建步骤：
    
        1、pom.xml添加依赖
        
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-feign</artifactId>
            </dependency>
            
        2、主启动类添加注解：
            @EnableFeignClients(basePackages= {"com.wallet"})
            @ComponentScan("com.wallet")
         
        3、wallet-api pom.xml 添加feign依赖
           
        4、wallet-api添加service公用接口
        
              添加注解：@FeignClient(value="WALLET-PROVIDER")  -- 对哪一个微服务进行接口编程
           
        
         Ribbon: Controller + RestTemplate   
         Feign:  接口
         
         Feign通过接口的方法调用Rest服务（之前是Ribbon+RestTempalte）,
         该请求发送给Eureka服务器（http:WALLET-PROVIDER/user/list）,
         通过Feign直接找到服务接口，由于在进行服务调用的时候融合了Ribbon技术，所以也支持负载均衡作用。
         
         
七、Hystrix断路器

        Hystrix是一个用于处理分布式系统的延迟和容错的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，
    Hystrix能够保证在一个依赖出问题的情况下，不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性。
        "断路器"本身是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控（类似熔断保险丝），
    向调用方返回一个符合预期的、可处理的备选响应（Fallback），而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了
    服务调用方的线程不会被长时间、不必要的占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩。
    
        注：是在服务端
    
    （一）服务熔断
        
          熔断机制是应对雪崩效应的一种微服务链路保护机制。
          当扇出链路的某个微服务不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，快速返回'错误"的响应
          信息。当检测到该节点微服务调用响应正常后恢复调用链路。在 SpringCloud框架里熔断机制通过 Hystrix实现。 Hystⅸx会监控微
          服务间调用的状况，当失败的调用到一定阈值，缺省是5秒内20次调用失败就会启动熔断机制。熔断机制的注解是@HystrixCommand  
          
          <1> pom.xml:
               <!-- hystrix -->
               <dependency>
                  <groupId>org.springframework.cloud</groupId>
                  <artifactId>spring-cloud-starter-hystrix</artifactId>
               </dependency>   
               
          <2>、application.yml:
              修改一句话：
                 instance-id: wallet-user8001-hystrix   #自定义hystrix相关的服务名称信息
                 
          <3>、controller的方法上面添加注解：
               @HystrixCommand(fallbackMethod = "processHystrix_Get")  : processHystrix_Get()自己定义的方法 
             
               例：
               if (null == user) {
                  throw new RuntimeException("该ID：" + id + "没有对应的信息");  //抛出了错误信息，自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法
               } 
               
          <4>、主启动类添加注解：
                @EnableCircuitBreaker  //对Hystrix熔断机制的支持
          
        
     （二）服务降级
     
           整体资源快不够了，忍痛将某些服务线关掉，待渡过难关，再开启回来。
           
           服务降级处理是在客户端实现完成的，与服务端没有关系
           
           搭建：
               修改wallet-api工程，根据已有的UserClientService接口新建一个实现了FallBackFactory接口的类UserClientServiceFallbackFactory，对接口UserClientService的方法统一处理熔断，在上面添加注解：@Component
               修改wallet-api工程，UserClientService接口在注解@FeignClient中添加fallbackFactory属性值：
                   
                    @FeignClient(value="WALLET-PROVIDER",fallbackFactory = UserClientServiceFallbackFactory.class)   //1、对哪一个微服务进行面向"接口"的编程 2、服务降级
                    public interface UserClientService {
                    
                        @RequestMapping(value = "/user/get/{id}", method = RequestMethod.GET)
                        public User get(@PathVariable("id") long id);
                    
                        @RequestMapping(value = "/user/list", method = RequestMethod.GET)
                        public List<User> list();
                    
                        @RequestMapping(value = "/user/add", method = RequestMethod.POST)
                        public boolean add(User user);
                    
                    }
                    
               wallet-consumer-feign - application.yml添加：
               
                        feign:
                          hystrix:
                            enabled: true
                        
           
           测试：
                3个 eureka先启动
                微服务 microservicecloud-provider-dept8001启动
                microservicecloud-consumer-dept-feign启动
                正常访问测试  http://localhost/consumer/dept/get/1
                故意关闭微服务 microservicecloud-provider-dept-8001
                客户端自己调用提示   --  此时服务端provider已经down了，但是我们做了服务降级处理，
                    让客户端在服务端不可用时也会获得提示信息而不会挂起耗死服务器
                
八、豪猪hystrixDashboard  
    
        除了隔离依赖服务的调用以外， Hystriⅸ还提供了准实时的调用监控( Hystrix Dashboard)， Hystrix会持绩地记录所有通过
        Hystrix发起的请求的执行信息，并以统计报表和图形的形式展示给用户，包括每秒执行多少请求多少成功，多少失败等。
        Netflix通过 hystrix-metrics-event-stream项目实现了对以上指标的监控。 Spring Cloud提供了 Hystrix Dashboard的整合，
        对监控内容转化成可视化界面。
        
        pom.xml:
                    <!-- hystrix和 hystrix-dashboard相关 -->
                    <dependency>
                        <groupId>org.springframework.cloud</groupId>
                        <artifactId>spring-cloud-starter-hystrix</artifactId>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.cloud</groupId>
                        <artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
                    </dependency>   
                    
         启动类：
            @EnableHystrixDashboard   //开启仪表盘监控注解
            
        
         所有Provider微服务提供类（8001/8002/8003）都需要监控依赖配置：
            <!-- actuator监控信息完善 -->
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-actuator</artifactId>
                    </dependency>
           
九、Zuul是什么
    
        zuu包含了对请求的路由和过滤两个最主要的功能：
            其中路由功能负责将外部请求转发到貝体的微服务实例上，是实现外部访问统一入口的基础，而过滤器功能则负责对请求的处理过
            程迸行干预，是实现请求校验、服务聚合等功能的基础。Zuul和 Eureka进行整合，将Zuul自身注册为Eureka服务治理下的应用，
            同时从Eureka中获得其他微服务的消息，也即以后的访问微服务都是通过Zuul跳转后获得。
            
        注意：zuu服务最终还是会注册进Eureka
        
          提供 = 代理+路由+过滤三大功能 
          
        创建步骤：
            1、新建工程：wallet-zuul-gateway-9527
            2、pom.xml:
                        <!-- zuul路由网关 -->
                        <dependency>
                            <groupId>org.springframework.cloud</groupId>
                            <artifactId>spring-cloud-starter-zuul</artifactId>
                        </dependency>  
              Zuul注入进Eureka
            3、 主启动类：
                @EnableZuulProxy
                
        Zuul路由访问映射规则：
            before:
                http:/myzuul.com9527/wallet-provider/user/get/2
                zuul:
                  routes:
                    mydept.serviceId: wallet-provider
                    mydept.path: /mydept/**
            after:
                http://myzuul.com:9527/mydept/user/get/1
                
                
           zuul:
             #ignored-services: wallet-provider  #单个 （原真实服务名忽略）
             prefix: /wallet                     #设置统一公共前缀
             ignored-services: "*"               #多个
           
             routes:                               #域名映射
               mydept.serviceId: wallet-provider   #微服务真实地址
               mydept.path: /mydept/**             #配置后对外提供的虚拟地址
               
            访问地址： http://myzuul.com:9527/wallet/mydept/user/get/4
            
十、Config分布式配置中心
    
           微服务意味着要将单体应用中的业务拆分成一个个子服务，每个服务的力度相对较小，因此系统中会出现大量的服务。
        由于每个服务都需要必要的配置信息才能运行，所以一套集中的、动态的配置管理设施是必不可少的。
        SpringCloud提供了ConfigServer来解决这个问题，我们每一个微服务自己带着一个application.yml，上百个配置文件的管理。。。
        
        一、是什么
                SpringCloud Config为微服务架构中的微服务提供集中式的外部配置支持，配置服务器为
                各个不同微服务应用的所有环境提供了一个中心化的外部配置。
        二、怎么玩
            SpringCloud Config 分为客户端和服务器两部分
            服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置服务器并为客户端提供获取配置信息，加密/解密信息等访问接口
            客户端则是通过指定的配置中心来管理应用资源，以及与业务相关的配置内容，并在启动的时候从配置中心获取和加载配置信息，
            配置服务器默认采用git来存储配置信息，这样就有助于对环境配置进行板本管理，并且可以通过git客户端工具来方便的管理和
            访问配置内容
        三、创建步骤：
            1、pom.xml:
                        <!-- springCloud Config -->
                        <dependency>
                            <groupId>org.springframework.cloud</groupId>
                            <artifactId>spring-cloud-config-server</artifactId>
                        </dependency>
             2、启动类：
                     @EnableConfigServer
        
            
             
            
          
             
             
         
       
       
       
    
        
        
    
    
       
    