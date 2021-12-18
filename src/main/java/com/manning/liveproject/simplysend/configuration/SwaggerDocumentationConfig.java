package com.manning.liveproject.simplysend.configuration;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@EnableOpenApi
@Configuration
public class SwaggerDocumentationConfig {

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.OAS_30)
                .securitySchemes(Lists.newArrayList(jwt()))
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.manning.liveproject.simplysend.api"))
                    .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private HttpAuthenticationScheme jwt() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER
                .name("jwt")
                .build();
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("SimplySend Procurement API")
            .description("An API to help companies oversee and track employee spending to prevent overspending")
            .license("MANNING")
            .licenseUrl("http://unlicense.org")
            .termsOfServiceUrl("")
            .version("1.0.0")
            .contact(new Contact("","", ""))
            .build();
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .info(new Info()
                .title("SimplySend Procurement API")
                .description("An API to help companies oversee and track employee spending to prevent overspending")
                .termsOfService("")
                .version("1.0.0")
                .license(new License()
                    .name("MANNING")
                    .url("http://unlicense.org"))
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .email("")));
    }
}
