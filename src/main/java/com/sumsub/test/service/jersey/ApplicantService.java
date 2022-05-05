package com.sumsub.test.service.jersey;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumsub.test.client.JerseyClientService;
import com.sumsub.test.facade.ApplicantFacade;
import com.sumsub.test.model.Applicant;
import com.sumsub.test.util.StringUtil;

import static com.sumsub.test.constant.Constant.URL_APPLICANT;
import static com.sumsub.test.constant.Constant.URL_APPLICANT_INFO;
import static com.sumsub.test.constant.Constant.URL_DOCS_STATUS;
import static com.sumsub.test.constant.Constant.URL_STATUS_PENDING;

public class ApplicantService implements ApplicantFacade<JerseyClientService> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final JerseyClientService jerseyClientService = new JerseyClientService();

    @Override
    public Applicant createApplicant(Applicant applicant) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        String requestBodyToJson = objectMapper.writeValueAsString(applicant);

        Response response = jerseyClientService.sendApplicantPost(URL_APPLICANT, StringUtil.createApplicantPath(), requestBodyToJson);

        boolean isSuccessful = response.getStatus() == 200 || response.getStatus() == 201;
        if (!isSuccessful) throw new RuntimeException("Jersey Error: Response was not successful for 'createApplicant'.");
        return response.readEntity(Applicant.class);
    }

    @Override
    public String getApplicantStatus(String applicantId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = jerseyClientService.sendGet(URL_APPLICANT + "/" + applicantId + URL_DOCS_STATUS);

        boolean isSuccessful = response.getStatus() == 200 || response.getStatus() == 201;
        if (!isSuccessful) throw new RuntimeException("Jersey Error: Response was not successful for 'getApplicantStatus'.");
        return objectMapper.writeValueAsString(response.readEntity(Object.class));
    }

    @Override
    public String setApplicantStatusPending(String applicantId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = jerseyClientService.sendPost(URL_APPLICANT + "/" + applicantId + URL_STATUS_PENDING, null);

        boolean isSuccessful = response.getStatus() == 200 || response.getStatus() == 201;
        if (!isSuccessful) throw new RuntimeException("Jersey Error: Response was not successful for 'setApplicantStatusPending'.");
        return objectMapper.writeValueAsString(response.readEntity(Object.class));
    }

    @Override
    public Object getApplicantInfo(String applicantId) throws NoSuchAlgorithmException, InvalidKeyException {
        Response response = jerseyClientService.sendGet(URL_APPLICANT + "/" + applicantId + URL_APPLICANT_INFO);

        boolean isSuccessful = response.getStatus() == 200 || response.getStatus() == 201;
        if (!isSuccessful) throw new RuntimeException("Jersey Error: Response was not successful for 'getApplicantInfo'.");
        return response.readEntity(Object.class);
    }
}
