package project.backend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.host}")
    private String swaggerHost;

    @Value("${swagger.protocol}")
    private String swaggerProtocol;

    @Profile("default")
    @Bean
    public Docket apiDocket() {
        return createDocket(swaggerHost, swaggerProtocol);
    }

    @Profile("local")
    @Bean
    public Docket apiDocketLocal(@Value("127.0.0.1:8080") String hostLocal,
                                 @Value("http") String protocolLocal) {
        return createDocket(hostLocal, protocolLocal);
    }

    private Docket createDocket(String host, String protocol) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build()
                .host(host)
                .protocols(new HashSet<>(Arrays.asList(protocol)));
    }
}