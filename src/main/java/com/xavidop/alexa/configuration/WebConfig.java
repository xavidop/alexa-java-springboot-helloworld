package com.xavidop.alexa.configuration;

import com.amazon.ask.servlet.ServletConstants;
import com.xavidop.alexa.properties.PropertiesUtils;
import com.xavidop.alexa.servlet.AlexaServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;

@Configuration
public class WebConfig {
    @Bean
    public ServletRegistrationBean<HttpServlet> alexaServlet() {

        System.setProperty(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, PropertiesUtils.getPropertyValue(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY));
        System.setProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, PropertiesUtils.getPropertyValue(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY));

        ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
        servRegBean.setServlet(new AlexaServlet());
        servRegBean.addUrlMappings("/alexa/*");
        servRegBean.setLoadOnStartup(1);
        return servRegBean;
    }

}