package com.sumsub.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumsub.test.client.FeignClientService;
import com.sumsub.test.client.JerseyClientService;
import com.sumsub.test.constant.Constant;
import com.sumsub.test.model.Applicant;
import com.sumsub.test.model.DocType;
import com.sumsub.test.model.Metadata;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class DocumentService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final JerseyClientService jerseyClientService = new JerseyClientService();
    private final FeignClientService feignClientService = new FeignClientService();

    public String addJerseyDocument(String applicantId, File file) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ParseException {
        String url = Constant.URL_APPLICANT + "/" + applicantId + Constant.URL_UPLOAD_DOCUMENT;
        Metadata metadata = Metadata.builder()
                .idDocType(DocType.PASSPORT)
                .country("DEU")
                .build();

        MultiPart part = new MultiPart(MediaType.MULTIPART_FORM_DATA_TYPE);
        FormDataBodyPart body = new FormDataBodyPart("metadata", objectMapper.writeValueAsString(metadata), MediaType.MULTIPART_FORM_DATA_TYPE);
        part.bodyPart(body);

        FileDataBodyPart filePart = new FileDataBodyPart("content", file, MediaType.valueOf("image/*"));
        filePart.contentDisposition(new FormDataContentDisposition("form-data; name=\"content\"; filename=\"" + file.getName() + "\""));
        part.bodyPart(filePart);

        Response response = jerseyClientService.sendMultiPartPost(url, part);
        return response.getHeaderString("X-Image-Id");
    }

    public String addFeignDocument(String applicantId, File file) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String path = Constant.URL_APPLICANT + "/" + applicantId + Constant.URL_UPLOAD_DOCUMENT;
        Metadata metadata = Metadata.builder()
                .idDocType(DocType.PASSPORT)
                .country("GBR")
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("metadata", objectMapper.writeValueAsString(metadata))
                .addFormDataPart("content", file.getName(), RequestBody.create(file, okhttp3.MediaType.parse("image/*")))
                .build();
        okhttp3.Response responseBody = feignClientService.sendPost(path, requestBody);
        return responseBody != null ? responseBody.headers().get("X-Image-Id") : null;
    }

}
