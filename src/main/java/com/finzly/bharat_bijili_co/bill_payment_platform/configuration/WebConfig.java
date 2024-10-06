package com.finzly.bharat_bijili_co.bill_payment_platform.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true); // This makes page numbers start at 1 instead of 0
        resolver.setMaxPageSize(100); // Maximum allowed page size
        resolver.setFallbackPageable(PageRequest.of(0, 10, Sort.by("id").ascending())); // Default values
        argumentResolvers.add(resolver);
    }
}

