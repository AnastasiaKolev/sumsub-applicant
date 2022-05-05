package com.sumsub.test.facade;

import java.io.File;

public interface DocumentFacade<T> {
    String addDocument(String applicantId, File file) throws Exception;
}
