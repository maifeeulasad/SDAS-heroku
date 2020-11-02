package com.mua.mas.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "custom")
@NoArgsConstructor
public class Properties {

    public static String headerString = "Authorization";
    public static String tokenPrefix = "Bearer ";
    public static String secret = "8f821a74-367b-4741-95b6-fdfad9b44705";
    public static Long expirationTime = 2L*24L*60L*60L*1000L;

    /*
    //@Value("${HEADER_STRING}")
    public static String HEADER_STRING;
    //public static final String HEADER_STRING = "Authorization";
    //@Value("${TOKEN_PREFIX}")
    public static String TOKEN_PREFIX;
    //public static final String TOKEN_PREFIX = "Bearer ";
    //@Value("${SECRET}")
    public static String SECRET;
    //public static final String SECRET = "8f821a74-367b-4741-95b6-fdfad9b44705"; //can be generated from uuid
    //@Value("${EXPIRATION_TIME}")
    public static long EXPIRATION_TIME;
    //public static final long EXPIRATION_TIME = 60L*2L*1000L;
    */
}