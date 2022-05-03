package com.sumsub.test;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import com.sumsub.test.model.Applicant;
import com.sumsub.test.service.ApplicantService;
import com.sumsub.test.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SumSubApplicantMain {
    public static final Logger log = LoggerFactory.getLogger(SumSubApplicantMain.class);

    private static final ApplicantService applicantService = new ApplicantService();
    private static final DocumentService documentService = new DocumentService();

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ParseException {
        Applicant applicant = Applicant.builder().build();

        Applicant response = applicantService.createJerseyApplicant(applicant);
        log.info("Jersey: Created an applicant №1 " + response.id + " for data: " + applicant.externalUserId);

        File file = new File(SumSubApplicantMain.class.getResource("/images/index.jpg").getFile());
        String imageId = documentService.addJerseyDocument(response.id, file);
        log.info("Jersey: Identifier of the added document №1: " + imageId);

        String applicantStatus = applicantService.getJerseyApplicantStatus(response.id);
        log.info("Jersey: Applicant status: " + applicantStatus);

        String accessToken = applicantService.getJerseyAccessToken(response.externalUserId);
        log.info("Jersey: Access token (json string): " + accessToken);

        String applicantStatusPending = applicantService.setJerseyApplicantStatusPending(response.id);
        log.info("Jersey: Applicant status to pending: " + applicantStatusPending);


        Applicant applicant2 = Applicant.builder().build();
        Applicant response2 = applicantService.createFeignApplicant(applicant2);
        log.info("Feign: Created an applicant №2 " + response2.id + " for data: " + applicant2.externalUserId);

        String imageId2 = documentService.addFeignDocument(response2.id, file);
        log.info("Feign: Identifier of the added document №2: " + imageId2);

        String applicantStatus2 = applicantService.getFeignApplicantStatus(response2.id);
        log.info("Feign: Applicant status (json string): " + applicantStatus2);

        String accessToken2 = applicantService.getFeignAccessToken(response2.externalUserId);
        log.info("Feign: Access token (json string): " + accessToken2);

        String applicantStatusPending2 = applicantService.setFeignApplicantStatusPending(response2.id);
        log.info("Feign: Applicant status to pending: " + applicantStatusPending2);
    }
}
