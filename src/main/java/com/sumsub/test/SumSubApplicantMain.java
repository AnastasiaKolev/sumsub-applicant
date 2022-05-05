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

    private static final ApplicantFacade<JerseyClientService> jerseyApplicantService = new ApplicantService();
    private static final ApplicantFacade<FeignClientService> feignApplicantService = new FeignApplicantService();

    private static final DocumentFacade<JerseyClientService> jerseyDocumentService = new DocumentService();
    private static final DocumentFacade<FeignClientService> feignDocumentService = new FeignDocumentService();

    private static final TokenFacade<JerseyClientService> jerseyTokenService = new TokenService();
    private static final TokenFacade<FeignClientService> feignTokenService = new FeignTokenService();

    public static void main(String[] args) throws Exception {
        Applicant jerseyApplicant = Applicant.builder().build();
        Applicant feignApplicant = Applicant.builder().build();
        File jerseyFile = new File(Objects.requireNonNull(SumSubApplicantMain.class.getResource("/docs/passport.jpg")).getFile());
        File feignFile = new File(Objects.requireNonNull(SumSubApplicantMain.class.getResource("/docs/passport2.jpg")).getFile());

        // Jersey
        Applicant createdJerseyApplicant = jerseyApplicantService.createApplicant(jerseyApplicant);
        log.info("[Jersey] Created an applicant: " + createdJerseyApplicant.id + " for externalUserId: " + jerseyApplicant.externalUserId);
        // Feign
        Applicant createdFeignApplicant = feignApplicantService.createApplicant(feignApplicant);
        log.info("[Feign] Created an applicant: " + createdFeignApplicant.id + " for externalUserId: " + feignApplicant.externalUserId);

        // Jersey
        String addedJerseyDocument = jerseyDocumentService.addDocument(createdJerseyApplicant.id, jerseyFile);
        log.info("[Jersey] Identifier of the added document: " + addedJerseyDocument);
        // Feign
        String addedFeignDocument = feignDocumentService.addDocument(createdFeignApplicant.id, feignFile);
        log.info("[Feign] Identifier of the added document: " + addedFeignDocument);

        // Jersey
        String jerseyApplicantStatus = jerseyApplicantService.getApplicantStatus(createdJerseyApplicant.id);
        log.info("[Jersey] Applicant status: " + jerseyApplicantStatus);
        // Feign
        String feignApplicantStatus = feignApplicantService.getApplicantStatus(createdFeignApplicant.id);
        log.info("[Feign] Applicant status (json string): " + feignApplicantStatus);

        // Jersey
        String jerseyAccessToken = jerseyTokenService.getAccessToken(createdJerseyApplicant.externalUserId);
        log.info("[Jersey] Access token (json string): " + jerseyAccessToken);
        // Feign
        String getFeignAccessToken = feignTokenService.getAccessToken(createdFeignApplicant.externalUserId);
        log.info("[Feign] Access token (json string): " + getFeignAccessToken);

        // Jersey
        String jerseyApplicantStatusPending = jerseyApplicantService.setApplicantStatusPending(createdJerseyApplicant.id);
        log.info("[Jersey] Applicant status to pending: " + jerseyApplicantStatusPending);
        // Feign
        String feignApplicantStatusPending = feignApplicantService.setApplicantStatusPending(createdFeignApplicant.id);
        log.info("[Feign] Applicant status to pending: " + feignApplicantStatusPending);

        Thread.sleep(Long.parseLong(PropertyUtil.getProperty("timeout")));
        // Jersey
        Object jerseyApplicantInfo = jerseyApplicantService.getApplicantInfo(createdJerseyApplicant.id);
        log.info("[Jersey] Get an applicant info: " + jerseyApplicantInfo + " for data: " + createdJerseyApplicant.externalUserId);
        Map<String, Object> jerseyInfo = objectMapper.convertValue(jerseyApplicantInfo, LinkedHashMap.class);
        Map<String, String> jerseyDocInfo = objectMapper.convertValue(jerseyInfo.get("info"), LinkedHashMap.class);
        log.info("[Jersey] An applicant info: name = " + jerseyDocInfo.get("firstNameEn")
                + ", last name = " + jerseyDocInfo.get("lastNameEn"));

        // Feign
        Object feignApplicantInfo = feignApplicantService.getApplicantInfo(createdFeignApplicant.id);
        log.info("[Feign] Get an applicant info: " + feignApplicantInfo + " for data: " + createdFeignApplicant.externalUserId);
        Map<String, Object> feignInfo = objectMapper.convertValue(feignApplicantInfo, LinkedHashMap.class);
        Map<String, String> feignDocInfo = objectMapper.convertValue(feignInfo.get("info"), LinkedHashMap.class);
        log.info("[Feign] An applicant info: name = " + feignDocInfo.get("firstNameEn")
                + ", last name = " + feignDocInfo.get("lastNameEn"));
    }
}
