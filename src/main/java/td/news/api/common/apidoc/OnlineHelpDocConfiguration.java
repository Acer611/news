package td.news.api.common.apidoc;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.Annotation;

/**
 * Created by sanlion on 17-3-3.
 */
@Configuration
@EnableSwagger2
@Controller
public class OnlineHelpDocConfiguration {

    @GetMapping("/doc")
    public String doc() {
        return "redirect:swagger-ui.html";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/zq8";
    }

    @Value("${config.api.doc.enable}")
    private boolean enable;
    @Value("${server.port}") private int port;

    @Bean
    public Docket all() {
        return apidoc(
                ApiOperation.class,
                "all",
                "所有的可用Api都在这里能找到");
    }

    private Docket apidoc(Class<? extends Annotation> annotation, String version, String description) {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .groupName(version)
                .select().apis(RequestHandlerSelectors.withMethodAnnotation(annotation)).build()
                .apiInfo(new ApiInfo(
                        ":" + port,
                        description,
                        version,
                        "http://www.lovedou.com/",
                        new Contact("sanlion.do", "https://github.com/sanlion/", "sanlion@vip.qq.com"),
                        "apache 2.0",
                        ""))
                .useDefaultResponseMessages(false);
    }
}
