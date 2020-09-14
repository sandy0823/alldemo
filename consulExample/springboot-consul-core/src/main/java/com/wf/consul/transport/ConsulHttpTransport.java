package com.wf.consul.transport;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.ecwid.consul.transport.AbstractHttpTransport;
import com.wf.consul.configuration.ConsulHttpProperties;

public class ConsulHttpTransport extends AbstractHttpTransport {
	private final HttpClient httpClient;
	
	public ConsulHttpTransport(ConsulHttpProperties consulHttpProperties){
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(consulHttpProperties.getMaxConnections());
		connectionManager.setDefaultMaxPerRoute(consulHttpProperties.getMaxPerRouteConnections());

		RequestConfig requestConfig = RequestConfig.custom().
				setConnectTimeout(consulHttpProperties.getConnectionTimeout()).
				setConnectionRequestTimeout(consulHttpProperties.getConnectionTimeout()).
				setSocketTimeout(consulHttpProperties.getReadTimeout()).
				build();

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().
				setConnectionManager(connectionManager).
				setDefaultRequestConfig(requestConfig).
				useSystemProperties();

		this.httpClient = httpClientBuilder.build();
	}
	
	public ConsulHttpTransport(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	@Override
	protected HttpClient getHttpClient() {
		return this.httpClient;
	}
}
