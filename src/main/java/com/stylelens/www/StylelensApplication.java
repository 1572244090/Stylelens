package com.stylelens.www;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.stylelens.www.dao")
public class StylelensApplication {

    public static void main(String[] args) {
        SpringApplication.run(StylelensApplication.class, args);
    }

}
