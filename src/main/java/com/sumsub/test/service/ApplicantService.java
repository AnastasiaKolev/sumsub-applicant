package com.sumsub.test.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sumsub.test.client.FeignClientService;
import com.sumsub.test.client.JerseyClientService;
import com.sumsub.test.model.Applicant;
import com.sumsub.test.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.sumsub.test.constant.Constant.*;

@AllArgsConstructor
@NoArgsConstructor
public class ApplicantService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JerseyClientService jerseyClientService = new JerseyClientService();
    private FeignClientService feignClientService = new FeignClientService();

    public Applicant createJerseyApplicant(Applicant applicant) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        TreeMap<String, String> queryMap = new TreeMap<>();
        queryMap.put(LEVEL_NAME_KEY, LEVEL_NAME_VALUE);

        String requestBodyToJson = objectMapper.writeValueAsString(applicant);

        return jerseyClientService.sendPost(URL_APPLICANT, queryMap, requestBodyToJson).readEntity(Applicant.class);
    }

    public Applicant createFeignApplicant(Applicant applicant) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        TreeMap<String, String> queryMap = new TreeMap<>();
        queryMap.put(LEVEL_NAME_KEY, LEVEL_NAME_VALUE);
        String path = StringUtil.createPath(URL_APPLICANT, queryMap);

        String applicantToJson = objectMapper.writeValueAsString(applicant);
        RequestBody requestBody = RequestBody.create(applicantToJson, MediaType.parse("application/json; charset=utf-8"));

        okhttp3.ResponseBody responseBody = feignClientService.sendPost(path, requestBody).body();
        return responseBody != null ? objectMapper.readValue(responseBody.string(), Applicant.class) : null;
    }

    public String getJerseyApplicantStatus(String applicantId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = jerseyClientService.sendGet(URL_APPLICANT + "/" + applicantId + "/requiredIdDocsStatus");

        return response.getStatus() == 200 ? objectMapper.writeValueAsString(response.readEntity(Object.class)) : null;
    }

    public String getFeignApplicantStatus(String applicantId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        okhttp3.Response response = feignClientService.sendGet(URL_APPLICANT + "/" + applicantId + "/requiredIdDocsStatus");

        okhttp3.ResponseBody responseBody = response.body();
        return responseBody != null ? responseBody.string() : null;
    }

    public String getJerseyAccessToken(String externalUserId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        TreeMap<String, String> queryMap = new TreeMap<>();
        queryMap.put(USER_ID_PARAM, externalUserId);
        queryMap.put(LEVEL_NAME_KEY, LEVEL_NAME_VALUE);
        String path = StringUtil.createPath(URL_TOKEN, queryMap);

        Response response = jerseyClientService.sendTokenPost(URL_TOKEN, path, externalUserId);

        return response.getStatus() == 200 ? objectMapper.writeValueAsString(response.readEntity(Object.class)) : null;
    }

    public String getFeignAccessToken(String externalUserId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        TreeMap<String, String> queryMap = new TreeMap<>();
        queryMap.put("userId", externalUserId);
        queryMap.put(LEVEL_NAME_KEY, LEVEL_NAME_VALUE);
        String path = StringUtil.createPath(URL_TOKEN, queryMap);

        okhttp3.Response response = feignClientService.sendPost(path, RequestBody.create(new byte[0], null));

        okhttp3.ResponseBody responseBody = response.body();
        return responseBody != null ? responseBody.string() : null;
    }

    public String setJerseyApplicantStatusPending(String applicantId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = jerseyClientService.sendPost(URL_APPLICANT + "/" + applicantId + "/status/pending", null, "{}");

        return response.getStatus() == 200 ? objectMapper.writeValueAsString(response.readEntity(Object.class)) : null;
    }

    public String setFeignApplicantStatusPending(String applicantId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        okhttp3.Response response = feignClientService.sendPost(URL_APPLICANT + "/" + applicantId + "/status/pending", null);

        okhttp3.ResponseBody responseBody = response.body();
        return responseBody != null ? responseBody.string() : null;
    }
}
