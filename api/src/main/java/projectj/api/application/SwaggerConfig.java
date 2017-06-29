package projectj.api.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import projectj.api.application.v1.UserController;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.or;

@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = { UserController.class })
public class SwaggerConfig {

    @SuppressWarnings("unchecked")
    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).select()
                .paths(or(containsPattern("/api*"))).build();
    }

    //@Value("${application.service.api.version}")
    @Value("${application.version}")
    private String apiVersion;

    private ApiInfo getApiInfo() {
        return new ApiInfo("Project J API",
                "This is a demo to play around",
                apiVersion,
                "Free of use",
                "myemail@yahoo.com",
                "No license required",
                "http://demo.com");
    }
}