package com.app.rsspark.network;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by konstie on 11.12.16.
 */

public class ApiInterceptor implements Interceptor {
    private static ApiInterceptor apiInterceptor;
    private String scheme;
    private String endpoint;

    private ApiInterceptor() {}

    public static ApiInterceptor get() {
        if (apiInterceptor == null) {
            apiInterceptor = new ApiInterceptor();
        }
        return apiInterceptor;
    }

    public void setApiInterceptor(String baseUrl) {
        HttpUrl httpUrl = HttpUrl.parse(baseUrl);
        scheme = httpUrl.scheme();
        endpoint = httpUrl.host();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (scheme != null && endpoint != null) {
            HttpUrl newUrl = request.url().newBuilder()
                    .scheme(scheme)
                    .host(endpoint)
                    .build();
            request = request.newBuilder()
                    .url(newUrl)
                    .build();
        }
        return chain.proceed(request);
    }
}
