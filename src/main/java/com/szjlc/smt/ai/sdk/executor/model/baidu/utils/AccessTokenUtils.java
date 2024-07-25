package com.szjlc.smt.ai.sdk.executor.model.baidu.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AccessTokenUtils {

    static final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();

    // 过期时间；默认60分钟
    private static final int expireMillis = 60 * 60 * 1000;

    // 缓存服务
    public static Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expireMillis - (60 * 1000), TimeUnit.MINUTES)
            .build();

    public static String getAccessToken(OkHttpClient okHttpClient, String apiKey, String apiSecret, String authHost) throws IOException {
        // 缓存Token
        String token = cache.getIfPresent("accessToken");
        if (null != token)
            return token;

        Request request = new Request.Builder()
                .url(authHost)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                        "grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + apiSecret))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = okHttpClient.newCall(request).execute();
        Map<String, String> map = JSON.parseObject(response.body().string(), Map.class);
        token = map.get("access_token");
        cache.put("accessToken", token);
        return token;
    }
}