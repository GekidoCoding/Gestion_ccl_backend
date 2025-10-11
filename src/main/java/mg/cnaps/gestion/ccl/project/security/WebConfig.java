package mg.cnaps.gestion.ccl.project.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import mg.cnaps.gestion.ccl.project.interceptor.AutorisationInterceptor;
//import mg.cnaps.gestion.ccl.project.interceptor.AutorisationInterceptor;
import mg.cnaps.gestion.ccl.project.interceptor.AutorisationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableWebMvc
@EnableAsync(proxyTargetClass = true)
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${auth.base_url}")
    private String urlAuthPrincipal;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        String[] allowedOriginArray = allowedOrigins.split(",");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> exclude = new ArrayList<>();
        exclude.add("/path/to/exclude**");
        registry.addInterceptor(new AutorisationInterceptor(urlAuthPrincipal)).excludePathPatterns(exclude);
    }
}
