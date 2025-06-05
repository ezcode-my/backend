package org.ezcode.codetest.infrastructure.elasticsearch.config;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.springframework.data.elasticsearch.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

	@Value("${spring.datasource.elasticsearch.address}")
	private String elasticsearchAddress;

	@Value("${spring.datasource.elasticsearch.port}")
	private String elasticsearchPort;

	@Value("${spring.datasource.elasticsearch.username}")
	private String elasticsearchUser;

	@Value("${spring.datasource.elasticsearch.password}")
	private String elasticsearchPassword;

	@Override
	public ClientConfiguration clientConfiguration() {

		SSLContext sslContext = trustAllSslContext();

		return ClientConfiguration.builder()
			.connectedTo(elasticsearchAddress + ":" + elasticsearchPort)
			.usingSsl(sslContext)
			.withBasicAuth(elasticsearchUser, elasticsearchPassword)
			.build();
	}

	private SSLContext trustAllSslContext() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
					public void checkClientTrusted(X509Certificate[] certs, String authType) { }
					public void checkServerTrusted(X509Certificate[] certs, String authType) { }
				}
			};
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			return sc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
