package com.sumsub.test.util;

import com.sumsub.test.constant.Constant;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SignatureUtil {

    public static String createSignature(long ts, String method, String url, byte[] requestBodyByte) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha256 = Mac.getInstance(Constant.CYPHER_ALGORITHM);
        hmacSha256.init(new SecretKeySpec(Constant.SECRET.getBytes(StandardCharsets.UTF_8), Constant.CYPHER_ALGORITHM));
        hmacSha256.update((ts + method + url).getBytes(StandardCharsets.UTF_8));
        byte[] signatureByte = requestBodyByte != null ? hmacSha256.doFinal(requestBodyByte) : hmacSha256.doFinal();
        return Hex.encodeHexString(signatureByte);
    }
}
