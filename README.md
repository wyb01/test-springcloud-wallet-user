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