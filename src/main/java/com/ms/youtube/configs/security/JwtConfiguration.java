package com.ms.youtube.configs.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt.config")
public class JwtConfiguration {
    private String loginUrl = "/login/**";
    private String registerUrl = "/register/**";
    @NestedConfigurationProperty
    private Header header = new Header();
    private int expiration = 3600;
    private String privateKey = "wstUTt4k7jc01awEpHBt6A0AnUlmsppu";
    private String type = "encrypted";

    public static class Header{
        private String name = "Authorization";
        private String prefix = "Bearer ";

        public String getName() {
            return name;
        }
        public String getPrefix() {
            return prefix;
        }
    }

    public String getType() {
        return type;
    }
    public String getRegisterUrl() {
        return registerUrl;
    }
    public String getLoginUrl() {
        return loginUrl;
    }
    public int getExpiration() {
        return expiration;
    }
    public String getPrivateKey() {
        return privateKey;
    }
    public Header getHeader() {
        return header;
    }
}
