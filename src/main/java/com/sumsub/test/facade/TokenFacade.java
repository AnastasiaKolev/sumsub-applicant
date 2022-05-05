package com.sumsub.test.facade;

public interface TokenFacade<T> {
    String getAccessToken(String externalUserId) throws Exception;
}
