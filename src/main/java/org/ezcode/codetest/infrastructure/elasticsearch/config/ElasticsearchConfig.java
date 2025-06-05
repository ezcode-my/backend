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

	//엘라스틱서치 설정 컨피그입니다.
	@Override
	public ClientConfiguration clientConfiguration() {

		SSLContext sslContext = trustAllSslContext();

		return ClientConfiguration.builder()
			.connectedTo(elasticsearchAddress + ":" + elasticsearchPort)
			.usingSsl(sslContext)
			.withBasicAuth(elasticsearchUser, elasticsearchPassword)
			.build();
	}

	//엘라스틱서치에서 제공하는 인증서가 인증서가 사설 인증서(Self Signed)라서 스프링 앱에서 해당 인증서를 신뢰할 수 없어서 오류가뜨는데
	//해당 설정으로 어떤 인증서든 검증하지 않고 신뢰하게끔 처리하는 설정을 넣었습니다.
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
