package com.sumsub.test.facade;

import com.sumsub.test.model.Applicant;

public interface ApplicantFacade<T> {
    Applicant createApplicant(Applicant applicant) throws Exception;

    String getApplicantStatus(String applicantId) throws Exception;

    String setApplicantStatusPending(String applicantId) throws Exception;

    Object getApplicantInfo(String applicantId) throws Exception;
}
