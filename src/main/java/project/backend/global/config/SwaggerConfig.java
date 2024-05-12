package project.backend.global.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.util.List;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static springfox.documentation.builders.RequestHandlerSelectors.withMethodAnnotation;

@Configuration
public class SwaggerConfig {

    // Docket 설정
    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) // optional
                .securitySchemes(singletonList(apiKey()))
                .securityContexts(singletonList(securityContext())) // Security 관련 설정
                .produces(singleton("application/json"))
                .consumes(singleton("application/json"))
                .useDefaultResponseMessages(false)
                .select()
                .apis(withMethodAnnotation(ApiOperation.class))
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT Token", "Authorization", "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("티캣츠 API")
                .description("알파벳이 붙은 api만 사용해주세요.")
                .version("1.0.0")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(securityReference())
                .operationSelector(o -> true)
                .build();
    }

    private List<SecurityReference> securityReference() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return singletonList(new SecurityReference("apiKey", authorizationScopes));
    }
}