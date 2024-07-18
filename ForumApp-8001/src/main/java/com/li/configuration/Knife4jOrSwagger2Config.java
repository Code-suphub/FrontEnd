package com.li.configuration;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EnableSwagger2
@EnableKnife4j
//@Configuration
public class Knife4jOrSwagger2Config {

  private static final String headerKey = "token"; //header参数的key

  @Bean
  public Docket createRestApi() {
    // 添加header参数headerKey
    ParameterBuilder parameterBuilder = new ParameterBuilder();
    List<Parameter> parameterList = new ArrayList<>();
    parameterBuilder.name(headerKey).description(headerKey)
            .modelRef(new ModelRef("string")).parameterType("header")
            .required(false).build();
    parameterList.add(parameterBuilder.build());
    return new Docket(DocumentationType.SWAGGER_2)
            .pathMapping("/")
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.andon.springbootutil.controller"))
            .paths(PathSelectors.any())
            .build()
            .globalOperationParameters(parameterList)
            .apiInfo(new ApiInfoBuilder()
                    .title("spring-boot-util")
                    .description("")
                    .version("v1.0")
                    .contact(new Contact("", "", ""))
                    .license("")
                    .licenseUrl("")
                    .build());
  }

  @Bean(value = "frontendApi")
  public Docket frontendApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .groupName("前端分组")
            .select()
            // 前端控制器的包路径
            .apis(RequestHandlerSelectors.basePackage("com.li.controller.frontend"))
            .paths(PathSelectors.any())
            .build();
  }

  @Bean(value = "backendApi")
  public Docket backendApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .groupName("后台分组")
            .select()
            // 后台控制器的包路径
            .apis(RequestHandlerSelectors.basePackage("com.li.controller.backend"))
            .paths(PathSelectors.any())
            .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Spring Boot IceCMS API V3.4")
        .description("IceCMS RESTful APIs")
        .version("3.4")
        .build();
  }

//  @Bean
//  public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
//    return new BeanPostProcessor() {
//
//      @Override
//      public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
//          customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
//        }
//        return bean;
//      }
//
//      private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
//        List<T> copy = mappings.stream()
//                .filter(mapping -> mapping.getPatternParser() == null)
//                .collect(Collectors.toList());
//        mappings.clear();
//        mappings.addAll(copy);
//      }
//
//      @SuppressWarnings("unchecked")
//      private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
//        try {
//          Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
//          field.setAccessible(true);
//          return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//          throw new IllegalStateException(e);
//        }
//      }
//    };
//  }
}
