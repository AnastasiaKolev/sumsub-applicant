package com.sumsub.test.service.feign;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumsub.test.client.FeignClientService;
import com.sumsub.test.constant.Constant;
import com.sumsub.test.facade.DocumentFacade;
import com.sumsub.test.model.DocType;
import com.sumsub.test.model.Metadata;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sumsub.test.constant.Constant.CONTENT;
import static com.sumsub.test.constant.Constant.HEADER_IMAGE_ID;
import static com.sumsub.test.constant.Constant.METADATA;

public class FeignDocumentService implements DocumentFacade<FeignClientService> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final FeignClientService feignClientService = new FeignClientService();

    public String addDocument(String applicantId, File file) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String path = Constant.URL_APPLICANT + "/" + applicantId + Constant.URL_UPLOAD_DOCUMENT;

        Metadata metadata = Metadata.builder()
                .idDocType(DocType.PASSPORT)
                .country("RUS")
                .build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(METADATA, objectMapper.writeValueAsString(metadata))
                .addFormDataPart(CONTENT, file.getName(), RequestBody.create(file, MediaType.parse("jpg/*")))
                .build();

        Response response = feignClientService.sendPost(path, requestBody);

        if (!response.isSuccessful()) throw new RuntimeException("Feign Error: Response was not successful for 'addDocument'.");

        return response.headers().get(HEADER_IMAGE_ID);
    }

}
