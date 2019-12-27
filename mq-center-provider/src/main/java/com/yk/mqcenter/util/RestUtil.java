package com.yk.mqcenter.util;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author yzw
 * @date 2019/11/19 15:58
 */
public class RestUtil {

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);

    private static RestTemplate restTemplate;

    static {
        restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : converters) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(StandardCharsets.UTF_8);
                break;
            }
        }
    }

    public static String doGet(String url) {
        log.info("http get request. url={}", url);
        checkNotNull(url, "url is null");

        String resp = restTemplate.getForObject(url, String.class);
        log.info("http post response. content={}", resp);

        return resp;
    }

    public static String doPost(String url) {
        log.info("http post request. url={}", url);
        checkNotNull(url, "url is null");

        String resp = restTemplate.postForObject(url, null, String.class);

        log.info("http post response. content={}", resp);

        return resp;
    }

    public static String doPostByJson(String url, String params) {
        log.info("http post request. url={}, params={}", url, params);
        checkNotNull(url, "url is null");
        checkNotNull(params, "params is null");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(params, httpHeaders);

        String resp = restTemplate.postForObject(url, entity, String.class);

        log.info("http post response. content={}", resp);

        return resp;
    }

    public static String doPostByForm(String url, Map<String, String> params) {
        log.info("http post request. url={}, params={}", url, params);
        checkNotNull(url, "url is null");
        checkNotNull(params, "params is null");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            map.add(param.getKey(), param.getValue());
        }

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);

        String resp = restTemplate.postForObject(url, entity, String.class);

        log.info("http post response. content={}", resp);

        return resp;
    }

    public static String doPostByForm(String url, Map<String, String> params, Map<String, String> headers) {
        log.info("http post request. url={}, params={}, headers={}", url, params, headers);
        checkNotNull(url, "url is null");
        checkNotNull(params, "params is null");
        checkNotNull(headers, "headers is null");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            httpHeaders.set(header.getKey(), header.getValue());
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            map.add(param.getKey(), param.getValue());
        }

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);

        String resp = restTemplate.postForObject(url, entity, String.class);

        log.info("http post response. content={}", resp);

        return resp;
    }
}
