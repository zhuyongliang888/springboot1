package com.hoperun.micro.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan("com.hoperun.micro.security.filter")
@MapperScan("com.hoperun.micro.security.dao")
public class Application extends SpringBootServletInitializer
{

    public static void main(String[] args)
    {
	System.out.println("==============SpringApplication.run start===========");
	SpringApplication.run(Application.class, args);
	System.out.println("==============SpringApplication.run end=============");
    }
}
