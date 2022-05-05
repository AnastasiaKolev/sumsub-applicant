package com.sumsub.test.client;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import com.sumsub.test.constant.Constant;
import com.sumsub.test.util.SignatureUtil;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static com.sumsub.test.constant.Constant.GET;
import static com.sumsub.test.constant.Constant.POST;
import static com.sumsub.test.constant.Constant.REST_URI;

@AllArgsConstructor
public class FeignClientService {

    public Response sendGet(String path) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        long ts = Instant.now().getEpochSecond();

        Request request = new Request.Builder()
                .url(REST_URI + path)
                .header(Constant.X_APP_TOKEN_KEY, Constant.X_APP_TOKEN_VALUE)
                .header(Constant.X_APP_ACCESS_SIG, SignatureUtil.createSignature(ts, GET, path, null))
                .header(Constant.X_APP_ACCESS_TS, String.valueOf(ts))
                .get()
                .build();

        return new OkHttpClient().newCall(request).execute();
    }

    public Response sendPost(String path, RequestBody requestBody) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        long ts = Instant.now().getEpochSecond();

        Request request = new Request.Builder()
                .url(REST_URI + path)
                .header(Constant.X_APP_TOKEN_KEY, Constant.X_APP_TOKEN_VALUE)
                .header(Constant.X_APP_ACCESS_SIG, SignatureUtil.createSignature(ts, POST, path, requestBodyToBytes(requestBody)))
                .header(Constant.X_APP_ACCESS_TS, String.valueOf(ts))
                .post(requestBody)
                .build();

        return new OkHttpClient().newCall(request).execute();
    }

    public static byte[] requestBodyToBytes(RequestBody requestBody) throws IOException {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        return buffer.readByteArray();
    }

}
