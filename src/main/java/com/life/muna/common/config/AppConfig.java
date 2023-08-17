package com.life.muna.common.config;

import com.life.muna.auth.util.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    public AppConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/muna/v1/**")
                .excludePathPatterns(excludePathPatterns());
    }

    private String[] excludePathPatterns() {
        return new String[]{
                 "/api/muna/v1/users/duplicate/email", "/api/muna/v1/users/duplicate/nickname"
                ,"/api/muna/v1/users/signUp", "/api/muna/v1/users/signIn"
                , "/api/muna/v1/users/signOut", "/api/muna/v1/users/reissue"
                , "/api/muna/v1/users/find/password", "/api/muna/v1/users//find/email"
        };
    }

}
