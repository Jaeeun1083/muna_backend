package com.life.muna.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30) // Swagger 3.0
                .useDefaultResponseMessages(false)
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

}
