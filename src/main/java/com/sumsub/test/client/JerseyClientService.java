package com.sumsub.test.client;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sumsub.test.constant.Constant;
import com.sumsub.test.util.SignatureUtil;
import lombok.AllArgsConstructor;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import static com.sumsub.test.constant.Constant.LEVEL_NAME_KEY;
import static com.sumsub.test.constant.Constant.LEVEL_NAME_VALUE;
import static com.sumsub.test.constant.Constant.REST_URI;
import static com.sumsub.test.constant.Constant.USER_ID_PARAM;

@AllArgsConstructor
public class JerseyClientService {

    private static final Client client = ClientBuilder.newClient();

    public Response sendGet(String url) throws NoSuchAlgorithmException, InvalidKeyException {
        long ts = Instant.now().getEpochSecond();

        return client
                .target(REST_URI)
                .path(url)
                .request()
                .header(Constant.X_APP_TOKEN_KEY, Constant.X_APP_TOKEN_VALUE)
                .header(Constant.X_APP_ACCESS_SIG, SignatureUtil.createSignature(ts, HttpMethod.GET, url, null))
                .header(Constant.X_APP_ACCESS_TS, String.valueOf(ts))
                .get();
    }

    public Response sendTokenPost(String url, String path, String externalUserId) throws NoSuchAlgorithmException, InvalidKeyException {
        long ts = Instant.now().getEpochSecond();

        return client
                .target(REST_URI)
                .path(url)
                .queryParam(LEVEL_NAME_KEY, LEVEL_NAME_VALUE)
                .queryParam(USER_ID_PARAM, externalUserId)
                .request()
                .header(Constant.X_APP_TOKEN_KEY, Constant.X_APP_TOKEN_VALUE)
                .header(Constant.X_APP_ACCESS_SIG, SignatureUtil.createSignature(ts, HttpMethod.POST, path, null))
                .header(Constant.X_APP_ACCESS_TS, String.valueOf(ts))
                .post(Entity.json(null));
    }

    public Response sendApplicantPost(String url, String path, String requestBody) throws NoSuchAlgorithmException, InvalidKeyException {
        long ts = Instant.now().getEpochSecond();

        return client
                .target(REST_URI)
                .path(url)
                .queryParam(LEVEL_NAME_KEY, LEVEL_NAME_VALUE)
                .request()
                .header(Constant.X_APP_TOKEN_KEY, Constant.X_APP_TOKEN_VALUE)
                .header(Constant.X_APP_ACCESS_SIG, SignatureUtil.createSignature(ts, HttpMethod.POST, path, requestBody.getBytes(StandardCharsets.UTF_8)))
                .header(Constant.X_APP_ACCESS_TS, ts)
                .post(Entity.json(requestBody));
    }

    public Response sendPost(String url, String requestBody) throws NoSuchAlgorithmException, InvalidKeyException {
        long ts = Instant.now().getEpochSecond();

        return client
                .target(REST_URI)
                .path(url)
                .request()
                .header(Constant.X_APP_TOKEN_KEY, Constant.X_APP_TOKEN_VALUE)
                .header(Constant.X_APP_ACCESS_SIG, SignatureUtil.createSignature(ts, HttpMethod.POST, url, null))
                .header(Constant.X_APP_ACCESS_TS, ts)
                .post(Entity.json(requestBody));
    }

    public Response sendMultiPartPost(String url, MultiPart part) {
        return client
                .register(MultiPartFeature.class)
                .register(RequestClientWriterInterceptor.class)
                .target(REST_URI)
                .path(url)
                .request()
                .header(Constant.X_APP_TOKEN_KEY, Constant.X_APP_TOKEN_VALUE)
                .header("path", url)
                .post(Entity.entity(part, MediaType.MULTIPART_FORM_DATA_TYPE));
    }
}
