package com.zdxr.cc.mgr.sl.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class BpmsWebMvcConfigure extends WebMvcConfigurationSupport {

    private static final Logger log = LoggerFactory.getLogger(BpmsWebMvcConfigure.class);

//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        String path = "file:" + System.getProperty("user.dir") + "/mgr/";
//        registry.addInterceptor(new HandlerInterceptor() {})
//                .addPathPatterns("/**")
//                .excludePathPatterns(path)
//                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/bpms/**");
//        super.addInterceptors(registry);
//
//    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = "file:" + System.getProperty("user.dir") + "/sl/";
        registry.addResourceHandler("/**")
                .addResourceLocations(path).addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/template/");

        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        super.addResourceHandlers(registry);
    }
}
