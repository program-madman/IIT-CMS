package com.iit.project.cms.CMSServer.config;

import com.iit.project.cms.CMSServer.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean filterReg() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new AuthFilter());
        bean.addUrlPatterns("/*");
        bean.setName("AuthFilter");
        bean.setOrder(1);
        return bean;
    }
}
