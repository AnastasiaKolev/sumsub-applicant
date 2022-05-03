package com.sumsub.test.util;

import java.util.TreeMap;

public class StringUtil {

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
