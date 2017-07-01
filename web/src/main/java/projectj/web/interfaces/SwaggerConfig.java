package projectj.web.interfaces;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import projectj.web.interfaces.v1.UserController;
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
                //       .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("projectj.api.application.v1"))
                .paths(PathSelectors.regex("/api/v1/users"))
                .build();
    }
}