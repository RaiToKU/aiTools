package com.szjlc.smt.ai.sdk.executor.model.xunfei.utils;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 签名工具包
 *
 * @author 小傅哥，微信：fustack
 */
@Slf4j
public class URLAuthUtils {

    // 过期时间；默认30分钟
    private static final int expireMillis = 30 * 60 * 1000;

    // 缓存服务
    public static Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expireMillis - (60 * 1000), TimeUnit.MILLISECONDS)
            .build();

    public static String getAuthURl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        return getAuthURl(hostUrl, apiKey, apiSecret, "GET", Boolean.TRUE);
    }

    public static String getAuthURl(String hostUrl, String apiKey, String apiSecret, String httpMethod, Boolean webSocket) throws Exception {
        // 缓存Token
        String token = cache.getIfPresent(apiKey);
        if (null != token) return token;

        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());

        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                httpMethod + " " + url.getPath() + " HTTP/1.1";

        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl authURL = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).
                addQueryParameter("date", date).
                addQueryParameter("host", url.getHost()).
                build();
        String httpURL = authURL.toString();
        if (webSocket) {
            httpURL = authURL.toString().replace("http://", "ws://").replace("https://", "wss://");
        }
        cache.put(apiKey, httpURL);
        return httpURL;
    }

}
