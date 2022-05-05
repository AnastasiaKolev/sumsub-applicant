package com.sumsub.test.service.jersey;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumsub.test.client.JerseyClientService;
import com.sumsub.test.constant.Constant;
import com.sumsub.test.facade.DocumentFacade;
import com.sumsub.test.model.DocType;
import com.sumsub.test.model.Metadata;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import static com.sumsub.test.constant.Constant.CONTENT;
import static com.sumsub.test.constant.Constant.HEADER_IMAGE_ID;
import static com.sumsub.test.constant.Constant.METADATA;

public class DocumentService implements DocumentFacade<JerseyClientService> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final JerseyClientService jerseyClientService = new JerseyClientService();

    @Override
    public String addDocument(String applicantId, File file) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ParseException {
        String url = Constant.URL_APPLICANT + "/" + applicantId + Constant.URL_UPLOAD_DOCUMENT;
        Metadata metadata = Metadata.builder()
                .idDocType(DocType.PASSPORT)
                .country("RUS")
                .build();

        MultiPart part = new MultiPart(MediaType.MULTIPART_FORM_DATA_TYPE);
        FormDataBodyPart body = new FormDataBodyPart(METADATA, objectMapper.writeValueAsString(metadata), MediaType.MULTIPART_FORM_DATA_TYPE);
        part.bodyPart(body);

        FileDataBodyPart filePart = new FileDataBodyPart(CONTENT, file);
        filePart.contentDisposition(FormDataContentDisposition.name(CONTENT).fileName(file.getName()).build());
        part.bodyPart(filePart);

        Response response = jerseyClientService.sendMultiPartPost(url, part);

        boolean isSuccessful = response.getStatus() == 200 || response.getStatus() == 201;
        if (!isSuccessful) throw new RuntimeException("Jersey Error: Response was not successful for 'addDocument'.");
        return response.getHeaderString(HEADER_IMAGE_ID);
    }
}
