package com.example.BiteMeBee.config;

import com.example.BiteMeBee.rest.filter.BeeFamilyFilter;
import com.example.BiteMeBee.rest.filter.HiveRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    BeeFamilyFilter getSessionManager() {
        return new BeeFamilyFilter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getSessionManager())
                .addPathPatterns("/api/bee_families");
    }

    @Bean
    public FilterRegistrationBean<HiveRequestFilter> hivePathLoggingFilter(){
        FilterRegistrationBean<HiveRequestFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new HiveRequestFilter());
        registrationBean.addUrlPatterns("/api/hives");

        return registrationBean;
    }

}
