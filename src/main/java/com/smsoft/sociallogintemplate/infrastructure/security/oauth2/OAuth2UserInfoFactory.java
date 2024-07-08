package com.smsoft.sociallogintemplate.infrastructure.security.oauth2;

import com.smsoft.sociallogintemplate.common.exception.OAuth2AuthenticationProcessingException;
import com.smsoft.sociallogintemplate.domain.user.AuthProvider;
import com.smsoft.sociallogintemplate.infrastructure.security.oauth2.user.*;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.NAVER.toString())) {
            return new NaverOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.KAKAO.toString())) {
            return new KakaoOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}