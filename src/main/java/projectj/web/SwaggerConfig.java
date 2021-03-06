/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import projectj.web.v1.UserController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = {UserController.class})
public class SwaggerConfig {

    @SuppressWarnings("unchecked")
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(UserController.class.getPackage().getName()))
                .paths(PathSelectors.regex("/api/v1/.*"))
                .build();
    }
}