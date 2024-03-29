package com.sumsub.test.constant;

import com.sumsub.test.util.PropertyUtil;

public class Constant {

    // X-App-Token - an App Token that you generate in our dashboard
    public static final String X_APP_TOKEN_KEY = "X-App-Token";
    public static final String X_APP_TOKEN_VALUE = PropertyUtil.getProperty("app_token");
    // X-App-Access-Sig - signature of the request in the hex format and lowercase (see below)
    public static final String X_APP_ACCESS_SIG = "X-App-Access-Sig";
    // X-App-Access-Ts - number of seconds since Unix Epoch in UTC
    public static final String X_APP_ACCESS_TS = "X-App-Access-Ts";

    public static final String SECRET = PropertyUtil.getProperty("secret");

    public static final String CYPHER_ALGORITHM = "HmacSHA256";

    public static final String REST_URI = "https://test-api.sumsub.com";
    public static final String GET = "GET";
    public static final String POST = "POST";

    public static final String URL_APPLICANT = "/resources/applicants";
    public static final String URL_TOKEN = "/resources/accessTokens";
    public static final String URL_UPLOAD_DOCUMENT = "/info/idDoc";
    public static final String URL_STATUS_PENDING = "/status/pending";
    public static final String URL_APPLICANT_INFO = "/one";
    public static final String URL_DOCS_STATUS = "/requiredIdDocsStatus";

    public static final String LEVEL_NAME_KEY = "levelName";
    public static final String LEVEL_NAME_VALUE = "basic-kyc-level";
    public static final String USER_ID_PARAM = "userId";


    public static final String CONTENT = "content";
    public static final String METADATA = "metadata";


    public static final String HEADER_IMAGE_ID = "X-Image-Id";
}
