package org.example.bank_rest.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public class CookieUtil {

    private CookieUtil(){}

    public static Map<String, String> getCookieMap(HttpServletRequest request) {
        if (request.getCookies() == null) return Map.of();
        return Arrays.stream(request.getCookies())
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
    }
}
