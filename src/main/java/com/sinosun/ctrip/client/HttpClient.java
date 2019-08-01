package com.sinosun.ctrip.client;

import io.vertx.ext.web.client.WebClient;

/**
 * Created on 2019/8/1 13:56.
 *
 * @author caogu
 */
public class HttpClient {
    private static WebClient webClient;

    public static void setHttpClient(WebClient webClient) {
        HttpClient.webClient = webClient;
    }

    public static WebClient getHttpClient() {
        return HttpClient.webClient;
    }
}
