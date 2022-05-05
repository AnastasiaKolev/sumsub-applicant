package com.sumsub.test.service.feign;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumsub.test.client.FeignClientService;
import com.sumsub.test.facade.ApplicantFacade;
import com.sumsub.test.model.Applicant;
import com.sumsub.test.util.StringUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.sumsub.test.constant.Constant.URL_APPLICANT;
import static com.sumsub.test.constant.Constant.URL_APPLICANT_INFO;
import static com.sumsub.test.constant.Constant.URL_DOCS_STATUS;
import static com.sumsub.test.constant.Constant.URL_STATUS_PENDING;

public class FeignApplicantService implements ApplicantFacade<FeignClientService> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final FeignClientService feignClientService = new FeignClientService();

    public Applicant createApplicant(Applicant applicant) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String applicantToJson = objectMapper.writeValueAsString(applicant);
        RequestBody requestBody = RequestBody.create(applicantToJson, MediaType.parse("application/json; charset=utf-8"));

        Response response = feignClientService.sendPost(StringUtil.createApplicantPath(), requestBody);
        ResponseBody responseBody = response.body();

        if (!response.isSuccessful()) throw new RuntimeException("Feign Error: Response was not successful 'createApplicant'.");
        return responseBody != null ? objectMapper.readValue(responseBody.string(), Applicant.class) : null;
    }

    public String getApplicantStatus(String applicantId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = feignClientService.sendGet(URL_APPLICANT + "/" + applicantId + URL_DOCS_STATUS);
        ResponseBody responseBody = response.body();

        if (!response.isSuccessful()) throw new RuntimeException("Feign Error: Response was not successful 'getApplicantStatus'.");
        return responseBody != null ? responseBody.string() : null;
    }

    public String setApplicantStatusPending(String applicantId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = feignClientService.sendPost(URL_APPLICANT + "/" + applicantId + URL_STATUS_PENDING, RequestBody.create(new byte[0], null));
        ResponseBody responseBody = response.body();

        if (!response.isSuccessful()) throw new RuntimeException("Feign Error: Response was not successful 'setApplicantStatusPending'.");
        return responseBody != null ? responseBody.string() : null;
    }

    public Object getApplicantInfo(String applicantId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = feignClientService.sendGet(URL_APPLICANT + "/" + applicantId + URL_APPLICANT_INFO);
        ResponseBody responseBody = response.body();

        if (!response.isSuccessful()) throw new RuntimeException("Feign Error: Response was not successful 'getApplicantInfo'.");
        return responseBody != null ? objectMapper.readValue(responseBody.string(), Object.class) : null;
    }
}
