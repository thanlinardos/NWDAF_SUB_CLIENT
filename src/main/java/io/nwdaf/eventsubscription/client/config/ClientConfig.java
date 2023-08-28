package io.nwdaf.eventsubscription.client.config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.nwdaf.eventsubscription.client.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.client.requestbuilders.CreateSubscriptionRequestBuilder;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
//@EnableWebMvc
@RegisterReflectionForBinding({RequestSubscriptionModel.class,RequestNotificationModel.class,RequestEventModel.class,CreateSubscriptionRequestBuilder.class,NotificationMethodEnum.class})
public class ClientConfig{
//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/client").setViewName("client");
//    }
    // @Value("${trust.store}")
    // private Resource trustStore;
    // @Value("${trust.store.password}")
    // private String trustStorePassword;

    // @Bean
    // public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
    //   CertificateException, MalformedURLException, IOException {
      
    //     // SSLContext sslContext = new SSLContextBuilder()
    //     //   .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();
    //     // SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
    //     Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
    //     // .register("https", sslConFactory)
    //     .register("http", new PlainConnectionSocketFactory())
    //     .build();
    //     BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
    //     CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
    //     ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
    //     return new RestTemplate(requestFactory);
    // }
}
