package com.sumsub.test.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumsub.test.constant.Constant;
import com.sumsub.test.util.SignatureUtil;
import com.sumsub.test.util.StringUtil;
import lombok.AllArgsConstructor;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

import static com.sumsub.test.constant.Constant.*;

@AllArgsConstructor
public class JerseyClientService {

    public static final Logger log = LoggerFactory.getLogger(JerseyClientService.class);
    private static final Client client = ClientBuilder.newClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Response sendGet(String url) throws NoSuchAlgorithmException, InvalidKeyException {
        long ts = Instant.now().getEpochSecond();
        return client
                .target(REST_URI)
                .path(url)
                .request(MediaType.APPLICATION_JSON)
                .header(Constant.X_APP_TOKEN_KEY, Constant.X_APP_TOKEN_VALUE)
                .header(Constant.X_APP_ACCESS_SIG, SignatureUtil.createSignature(ts, HttpMethod.GET, url, null))
                .header(Constant.X_APP_ACCESS_TS, String.valueOf(ts))
                .get();
    }

    public Response sendTokenPost(String url, String path, String applicantId) throws NoSuchAlgorithmException, InvalidKeyException {
        long ts = Instant.now().getEpochSecond();
        return client
                .register(RequestClientWriterInterceptor.class)
                .target(REST_URI)
                .path(url)
                .queryParam(USER_ID_PARAM, applicantId)
                .queryParam(LEVEL_NAME_KEY, LEVEL_NAME_VALUE)
                .request()
                .header(Constant.X_APP_TOKEN_KEY, Constant.X_APP_TOKEN_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, null)
                .header("path", path)
                .header(Constant.X_APP_ACCESS_SIG, SignatureUtil.createSignature(ts, HttpMethod.POST, path, null))
                .header(Constant.X_APP_ACCESS_TS, String.valueOf(ts))
                .post(Entity.json(objectMapper.nullNode()));
    }

    public Response sendPost(String url, TreeMap<String, String> queryMap, String requestBody) throws NoSuchAlgorithmException, InvalidKeyException {
        long ts = Instant.now().getEpochSecond();

        String path = StringUtil.createPath(URL_TOKEN, queryMap);

        if (queryMap == null) {
            return client
                    .target(REST_URI)
                    .path(url)
                    .request()
                    .header(Constant.X_APP_TOKEN_KEY, Constant.X_APP_TOKEN_VALUE)
                    .header(Constant.X_APP_ACCESS_SIG, SignatureUtil.createSignature(ts, HttpMethod.POST, path, requestBody.getBytes(StandardCharsets.UTF_8)))
                    .header(Constant.X_APP_ACCESS_TS, ts)
                    .post(Entity.json(requestBody));
        } else {
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
