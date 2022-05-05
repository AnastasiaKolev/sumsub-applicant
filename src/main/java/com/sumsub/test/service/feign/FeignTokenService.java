package com.sumsub.test.service.feign;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.sumsub.test.client.FeignClientService;
import com.sumsub.test.facade.TokenFacade;
import com.sumsub.test.util.StringUtil;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FeignTokenService implements TokenFacade<FeignClientService> {
    private static final FeignClientService feignClientService = new FeignClientService();

    @Override
    public String getAccessToken(String externalUserId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = feignClientService.sendPost(StringUtil.createApplicantStatusPath(externalUserId), RequestBody.create(new byte[0], null));

        ResponseBody responseBody = response.body();

        if (!response.isSuccessful()) throw new RuntimeException("Feign Error: Response was not successful for 'getAccessToken'.");
        return responseBody != null ? responseBody.string() : null;
    }
}
