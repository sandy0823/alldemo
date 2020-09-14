package com.wf.consul.configuration;

import java.lang.reflect.Field;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.cloud.consul.ConsulAutoConfiguration;
import org.springframework.cloud.consul.ConsulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.ecwid.consul.transport.HttpTransport;
import com.ecwid.consul.transport.TLSConfig;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.ConsulRawClient;
import com.wf.consul.transport.ConsulHttpTransport;
import com.wf.consul.transport.ConsulHttpsTransport;

/***自动装配在开源spring cloud consul 自动装配配置之前 ****/
@Configuration
@EnableConfigurationProperties
@ConditionalOnConsulEnabled
@AutoConfigureBefore(ConsulAutoConfiguration.class)
public class ConsulOverrideConfiguration {
	static final int DEFAULT_MAX_CONNECTIONS = 1000;
	static final int DEFAULT_MAX_PER_ROUTE_CONNECTIONS = 500;
	static final int DEFAULT_CONNECTION_TIMEOUT = 10000; // 10 sec

	// 10 minutes for read timeout due to blocking queries timeout
	// https://www.consul.io/api/index.html#blocking-queries
	static final int DEFAULT_READ_TIMEOUT = 60000 * 10; // 10 min
	
	@Bean
	public ConsulHttpProperties consulHttpProperties() {
		return new ConsulHttpProperties();
	}
	
	@Bean
	public ConsulProperties consulProperties() {
		return new ConsulProperties();
	}

	@Bean
	public ConsulClient consulClient(ConsulProperties consulProperties,ConsulHttpProperties consulHttpProperties) {
		final int agentPort = consulProperties.getPort();
		final String agentHost = !StringUtils.isEmpty(consulProperties.getScheme())
				? consulProperties.getScheme() + "://" + consulProperties.getHost()
				: consulProperties.getHost();

	    ConsulRawClient.Builder builder =  ConsulRawClient.Builder.builder()
				.setHost(agentHost)
				.setPort(agentPort);
		HttpTransport transport = null;
		if (consulProperties.getTls() != null) {
			ConsulProperties.TLSConfig tls = consulProperties.getTls();
			TLSConfig tlsConfig = new TLSConfig(tls.getKeyStoreInstanceType(),
					tls.getCertificatePath(), tls.getCertificatePassword(),
					tls.getKeyStorePath(), tls.getKeyStorePassword());
			transport = new ConsulHttpsTransport(consulHttpProperties,tlsConfig);
		}else{
			transport = new ConsulHttpTransport(consulHttpProperties);
		}
		Field field = ReflectionUtils.findField(ConsulRawClient.Builder.class, "httpTransport");
		field.setAccessible(true);
		ReflectionUtils.setField(field, builder, transport);
		return new ConsulClient(builder.build());
	}
}
