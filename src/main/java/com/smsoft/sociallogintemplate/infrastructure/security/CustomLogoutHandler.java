package com.smsoft.sociallogintemplate.infrastructure.security;

import com.smsoft.sociallogintemplate.common.util.CookieUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        CookieUtils.deleteCookie(request, response, "access_token");
    }
}