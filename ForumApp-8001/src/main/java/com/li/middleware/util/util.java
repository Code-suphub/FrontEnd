package com.li.middleware.util;

import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;

public class util {

    @Bean
    public ModelMapper mapperUtil(){
        return new ModelMapper() {
        };
    }
}
