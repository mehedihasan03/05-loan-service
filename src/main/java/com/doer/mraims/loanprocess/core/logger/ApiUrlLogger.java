package com.doer.mraims.loanprocess.core.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Component
public class ApiUrlLogger implements CommandLineRunner {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private ApiLoggerProperties apiLoggerProperties;

    @Override
    public void run(String... args) {
        System.out.println("Registered Total API : " + requestMappingHandlerMapping.getHandlerMethods().size());
        if (!apiLoggerProperties.isEnabled()) {
            return; // Exit if logging is disabled
        }
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        System.out.println("Registered API Endpoints:");
        if (handlerMethods.isEmpty()) {
            System.out.println("No API Endpoints Found!!!");
        } else {
            requestMappingHandlerMapping.getHandlerMethods().forEach((info, method) -> {
                System.out.println(info.getMethodsCondition() + "  " + info.getPathPatternsCondition().getFirstPattern());
            });
        }
    }
}
