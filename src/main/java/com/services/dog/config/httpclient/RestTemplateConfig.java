package com.services.dog.config.httpclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${breeds.list.all.timeout}")
    private int allBreedsTimeout;

    @Value("${breeds.list.sub-breed.timeout}")
    private int subBreedsTimeout;

    @Bean
    public RestTemplate allBreedsRestTemplate() {
        return createRestTemplate(allBreedsTimeout);
    }

    @Bean
    public RestTemplate subBreedsRestTemplate() {
        return createRestTemplate(subBreedsTimeout);
    }

    private RestTemplate createRestTemplate(int timeout) {
        return new RestTemplate(clientHttpRequestFactory(timeout));
    }

    private ClientHttpRequestFactory clientHttpRequestFactory(int timeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        return factory;
    }
}
