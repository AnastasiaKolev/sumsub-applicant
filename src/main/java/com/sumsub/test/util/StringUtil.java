package com.sumsub.test.util;

import java.util.TreeMap;

import static com.sumsub.test.constant.Constant.LEVEL_NAME_KEY;
import static com.sumsub.test.constant.Constant.LEVEL_NAME_VALUE;
import static com.sumsub.test.constant.Constant.URL_APPLICANT;
import static com.sumsub.test.constant.Constant.URL_TOKEN;
import static com.sumsub.test.constant.Constant.USER_ID_PARAM;

public class StringUtil {

    public static String createApplicantPath() {
        TreeMap<String, String> queryMap = new TreeMap<>();
        queryMap.put(LEVEL_NAME_KEY, LEVEL_NAME_VALUE);
        return StringUtil.createPath(URL_APPLICANT, queryMap);
    }

    public static String createApplicantStatusPath(String externalUserId) {
        TreeMap<String, String> queryMap = new TreeMap<>();
        queryMap.put(USER_ID_PARAM, externalUserId);
        queryMap.put(LEVEL_NAME_KEY, LEVEL_NAME_VALUE);
        return StringUtil.createPath(URL_TOKEN, queryMap);
    }

    public static String createPath(String url, TreeMap<String, String> queryMap) {
        String path = url;
        if (queryMap != null) {
            StringBuilder query = new StringBuilder("?");
            for (String key : queryMap.keySet()) {
                query.append(key).append("=").append(queryMap.get(key));
                if (!queryMap.lastKey().equals(key)) query.append("&");
            }
            path = url + query;
        }
        return path;
    }
}
