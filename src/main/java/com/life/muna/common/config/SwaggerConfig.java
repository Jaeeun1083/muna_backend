package com.life.muna.common.config;

import com.life.muna.auth.dto.UnLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30) // Swagger 3.0
                .useDefaultResponseMessages(false)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select() // ApiSelectorBuilder 생성
                .apis(RequestHandlerSelectors.basePackage("com.life.muna")) // Swagger 적용할 API 선택 (특정 패키지 내의 모든 Controller에 적용)
                .paths(PathSelectors.any()) // apis로 선택된 API 중 paths 내의 조건에 맞는 API 필터링 ( PathSelectors.any : 모든 api 통과)
                .build()
                .apiInfo(apiInfo()); // 제목, 설명 등 Swagger API 정보를 받아와 출력
    }

    private ApiInfo apiInfo() { // Swagger UI 로 노출할 정보
        return new ApiInfoBuilder()
                .title("Muna API")
                .description("Muna API 테스트를 위한 Swagger 페이지")
                .version("3.0")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(oc -> oc.findAnnotation(UnLock.class).isEmpty()) // Lock 어노테이션이 달려있는 X 만 자물쇠 걸리도록
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

}
