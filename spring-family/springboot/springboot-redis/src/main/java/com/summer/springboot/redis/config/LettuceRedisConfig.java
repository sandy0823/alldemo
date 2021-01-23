package com.summer.springboot.redis.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;

import io.lettuce.core.RedisClient;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;

/**
 * 开启 lettuce 刷新redis 集群拓扑结构图的能力
 * @author sandy
 *
 */
@Configuration
@ConditionalOnClass(RedisClient.class)
public class LettuceRedisConfig{
     
@Bean
@ConditionalOnProperty(name = "spring.redis.cluster.nodes")
@ConditionalOnMissingBean(RedisConnectionFactory.class)
@ConditionalOnMissingClass("org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce.Cluster")
    public LettuceClientConfigurationBuilderCustomizer builderCustomizers(){
        // 配置用于开启自适应刷新和定时刷新。如自适应刷新不开启，Redis集群变更时将会导致连接异常
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofSeconds(30))// 开启周期刷新(默认60秒)
                .enableAllAdaptiveRefreshTriggers() //开启自适应刷新
                .enablePeriodicRefresh(true)
                // 自适应刷新超时时间(默认30秒)
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(10)) //默认关闭开启后时间为30秒
                .refreshTriggersReconnectAttempts(3)
                .build();
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                                 .topologyRefreshOptions(clusterTopologyRefreshOptions)//拓扑刷新
                              //   .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                                 .autoReconnect(true)
                                 .socketOptions(SocketOptions.builder().keepAlive(true).build())
                                 .validateClusterNodeMembership(false)// 取消校验集群节点的成员关系
                                 .build();
         
        LettuceClientConfigurationBuilderCustomizer customizer = new LettuceClientConfigurationBuilderCustomizer() {
            @Override
            public void customize(LettuceClientConfigurationBuilder clientConfigurationBuilder) {
                clientConfigurationBuilder.clientOptions(clusterClientOptions);
                //clientConfigurationBuilder.readFrom(ReadFrom.SLAVE_PREFERRED);
            }
        };
         
        return customizer;
    }
}
