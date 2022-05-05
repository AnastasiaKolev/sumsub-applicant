package com.sumsub.test.service.jersey;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumsub.test.client.JerseyClientService;
import com.sumsub.test.facade.TokenFacade;
import com.sumsub.test.util.StringUtil;

import static com.sumsub.test.constant.Constant.URL_TOKEN;

public class TokenService implements TokenFacade<JerseyClientService> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final JerseyClientService jerseyClientService = new JerseyClientService();

    @Override
    public String getAccessToken(String externalUserId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = jerseyClientService.sendTokenPost(URL_TOKEN, StringUtil.createApplicantStatusPath(externalUserId), externalUserId);

        boolean isSuccessful = response.getStatus() == 200 || response.getStatus() == 201;
        if (!isSuccessful) throw new RuntimeException("Jersey Error: Response was not successful for 'getAccessToken'.");
        return objectMapper.writeValueAsString(response.readEntity(Object.class));
    }
}
