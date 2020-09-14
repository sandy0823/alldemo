package com.example.demo.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import com.ecwid.consul.transport.AbstractHttpTransport;
import com.ecwid.consul.transport.TLSConfig;
import com.ecwid.consul.transport.TransportException;
import com.ecwid.consul.transport.TLSConfig.KeyStoreInstanceType;

public class ConsulHttpsTransport extends AbstractHttpTransport {
    private final HttpClient httpClient;
    
    public ConsulHttpsTransport(ConsulHttpProperties consulHttpProperties,TLSConfig tlsConfig){
    	try {
			KeyStore clientStore = KeyStore.getInstance(tlsConfig.getKeyStoreInstanceType().name());
			clientStore.load(new FileInputStream(tlsConfig.getCertificatePath()), tlsConfig.getCertificatePassword().toCharArray());

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(clientStore, tlsConfig.getCertificatePassword().toCharArray());
			KeyManager[] kms = kmf.getKeyManagers();

			KeyStore trustStore = KeyStore.getInstance(KeyStoreInstanceType.JKS.name());
			trustStore.load(new FileInputStream(tlsConfig.getKeyStorePath()), tlsConfig.getKeyStorePassword().toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(trustStore);
			TrustManager[] tms = tmf.getTrustManagers();

			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
			sslContext.init(kms, tms, new SecureRandom());
			SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext);

			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", factory).build();

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
			connectionManager.setMaxTotal(consulHttpProperties.getMaxConnections());
			connectionManager.setDefaultMaxPerRoute(consulHttpProperties.getMaxPerRouteConnections());

			RequestConfig requestConfig = RequestConfig.custom().
					setConnectTimeout(consulHttpProperties.getConnectionTimeout()).
					setConnectionRequestTimeout(consulHttpProperties.getConnectionTimeout()).
					setSocketTimeout(consulHttpProperties.getReadTimeout()).
					build();

			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().
					setConnectionManager(connectionManager).
					setDefaultRequestConfig(requestConfig);

			this.httpClient = httpClientBuilder.build();
		} catch (GeneralSecurityException e) {
			throw new TransportException(e);
		} catch (IOException e) {
			throw new TransportException(e);
		}
    }
    
    public ConsulHttpsTransport(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@Override
	protected HttpClient getHttpClient() {
		return this.httpClient;
	}

}
