package io.nwdaf.eventsubscription.client.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import io.nwdaf.eventsubscription.client.NwdafSubClientApplication;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestTemplateFactoryConfig {

    private static Resource trustStore;
    private static String trustStorePassword;

    public static ClientHttpRequestFactory createRestTemplateFactory(Boolean secure) {
        SSLContext sslContext;
        SSLContext insecureSslContext;
        SSLConnectionSocketFactory sslConFactory;
        if (trustStore == null || trustStorePassword == null) {
            return new HttpComponentsClientHttpRequestFactory();
        }
        try {
            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();

            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            insecureSslContext = new SSLContextBuilder()
                    .loadTrustMaterial(acceptingTrustStrategy).build();
            if (secure) {
                sslConFactory = new SSLConnectionSocketFactory(sslContext);
            } else {
                sslConFactory = new SSLConnectionSocketFactory(insecureSslContext, (s, sslSession) -> true);
            }
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslConFactory)
                    .register("http", new PlainConnectionSocketFactory())
                    .build();
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(2000);
            connectionManager.setDefaultMaxPerRoute(2000);
            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
            return new HttpComponentsClientHttpRequestFactory(httpClient);
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException
                 | IOException e) {
            NwdafSubClientApplication.getLogger().error("Error creating RestTemplateFactory: " + e.getMessage());
        }
        return null;
    }

    public static void setTrustStore(Resource trustStore) {
        RestTemplateFactoryConfig.trustStore = trustStore;
    }

    public static void setTrustStorePassword(String trustStorePassword) {
        RestTemplateFactoryConfig.trustStorePassword = trustStorePassword;
    }
}