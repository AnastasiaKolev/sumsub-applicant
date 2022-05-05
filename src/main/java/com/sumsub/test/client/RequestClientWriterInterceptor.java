package com.sumsub.test.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import com.sumsub.test.constant.Constant;
import com.sumsub.test.util.SignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class RequestClientWriterInterceptor implements WriterInterceptor {

    public static final Logger log = LoggerFactory.getLogger(JerseyClientService.class);

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        OutputStream old = context.getOutputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            context.setOutputStream(buffer);
            context.proceed();

            byte[] entity = buffer.toByteArray();

            long ts = Instant.now().getEpochSecond();
            context.getHeaders().putSingle(Constant.X_APP_ACCESS_TS, ts);

            String signature = SignatureUtil.createSignature(ts, HttpMethod.POST, context.getHeaders().get("path").get(0).toString(), entity);
            context.getHeaders().putSingle(Constant.X_APP_ACCESS_SIG, signature);
            context.getHeaders().remove("path");

            old.write(entity);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error: " + e.getMessage());
        } finally {
            context.setOutputStream(old);
        }
    }
}