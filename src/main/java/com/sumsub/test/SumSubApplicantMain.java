package com.sumsub.test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumsub.test.client.FeignClientService;
import com.sumsub.test.client.JerseyClientService;
import com.sumsub.test.facade.ApplicantFacade;
import com.sumsub.test.facade.DocumentFacade;
import com.sumsub.test.facade.TokenFacade;
import com.sumsub.test.model.Applicant;
import com.sumsub.test.service.feign.FeignApplicantService;
import com.sumsub.test.service.feign.FeignDocumentService;
import com.sumsub.test.service.feign.FeignTokenService;
import com.sumsub.test.service.jersey.ApplicantService;
import com.sumsub.test.service.jersey.DocumentService;
import com.sumsub.test.service.jersey.TokenService;
import com.sumsub.test.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SumSubApplicantMain {
    public static final Logger log = LoggerFactory.getLogger(SumSubApplicantMain.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final ApplicantFacade<JerseyClientService> jerseyApplicant = new ApplicantService();
    private static final ApplicantFacade<FeignClientService> feignApplicant = new FeignApplicantService();

    private static final DocumentFacade<JerseyClientService> jerseyDocument = new DocumentService();
    private static final DocumentFacade<FeignClientService> feignDocument = new FeignDocumentService();

    private static final TokenFacade<JerseyClientService> jerseyToken = new TokenService();
    private static final TokenFacade<FeignClientService> feignToken = new FeignTokenService();

    public static void main(String[] args) throws Exception {
        Applicant applicantJ = Applicant.builder().build();
        Applicant applicantF = Applicant.builder().build();
        File file = new File(Objects.requireNonNull(SumSubApplicantMain.class.getResource("/docs/passport.jpg")).getFile());

        // Jersey
        Applicant createdJerseyApplicant = jerseyApplicant.createApplicant(applicantJ);
        log.info("[Jersey] Created an applicant: " + createdJerseyApplicant.id + " for externalUserId: " + applicantJ.externalUserId);
        // Feign
        Applicant createdFeignApplicant = feignApplicant.createApplicant(applicantF);
        log.info("[Feign] Created an applicant: " + createdFeignApplicant.id + " for externalUserId: " + applicantF.externalUserId);

        // Jersey
        String addedJerseyDocument = jerseyDocument.addDocument(createdJerseyApplicant.id, file);
        log.info("[Jersey] Identifier of the added document: " + addedJerseyDocument);
        // Feign
        String addedFeignDocument = feignDocument.addDocument(createdFeignApplicant.id, file);
        log.info("[Feign] Identifier of the added document: " + addedFeignDocument);

        // Jersey
        String jerseyApplicantStatus = jerseyApplicant.getApplicantStatus(createdJerseyApplicant.id);
        log.info("[Jersey] Applicant status: " + jerseyApplicantStatus);
        // Feign
        String feignApplicantStatus = feignApplicant.getApplicantStatus(createdFeignApplicant.id);
        log.info("[Feign] Applicant status (json string): " + feignApplicantStatus);

        // Jersey
        String jerseyAccessToken = jerseyToken.getAccessToken(createdJerseyApplicant.externalUserId);
        log.info("[Jersey] Access token (json string): " + jerseyAccessToken);
        // Feign
        String getFeignAccessToken = feignToken.getAccessToken(createdFeignApplicant.externalUserId);
        log.info("[Feign] Access token (json string): " + getFeignAccessToken);

        // Jersey
        String jerseyApplicantStatusPending = jerseyApplicant.setApplicantStatusPending(createdJerseyApplicant.id);
        log.info("[Jersey] Applicant status to pending: " + jerseyApplicantStatusPending);
        // Feign
        String feignApplicantStatusPending = feignApplicant.setApplicantStatusPending(createdFeignApplicant.id);
        log.info("[Feign] Applicant status to pending: " + feignApplicantStatusPending);

        Thread.sleep(Long.parseLong(PropertyUtil.getProperty("thread_sleep")));
        // Jersey
        Object jerseyApplicantInfo = jerseyApplicant.getApplicantInfo(createdJerseyApplicant.id);
        log.info("[Jersey] Get an applicant info: " + jerseyApplicantInfo + " for data: " + createdJerseyApplicant.externalUserId);
        Map<String, Object> jerseyInfo = objectMapper.convertValue(jerseyApplicantInfo, LinkedHashMap.class);
        Map<String, String> jerseyDocInfo = objectMapper.convertValue(jerseyInfo.get("info"), LinkedHashMap.class);
        log.info("[Jersey] An applicant info: name = " + jerseyDocInfo.get("firstNameEn")
                + ", last name = " + jerseyDocInfo.get("lastNameEn"));

        // Feign
        Object feignApplicantInfo = feignApplicant.getApplicantInfo(createdFeignApplicant.id);
        log.info("[Feign] Get an applicant info: " + feignApplicantInfo + " for data: " + createdFeignApplicant.externalUserId);
        Map<String, Object> feignInfo = objectMapper.convertValue(feignApplicantInfo, LinkedHashMap.class);
        Map<String, String> feignDocInfo = objectMapper.convertValue(feignInfo.get("info"), LinkedHashMap.class);
        log.info("[Feign] An applicant info: name = " + feignDocInfo.get("firstNameEn")
                + ", last name = " + feignDocInfo.get("lastNameEn"));
    }
}
