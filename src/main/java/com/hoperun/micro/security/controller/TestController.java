package com.hoperun.micro.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController
{
    @RequestMapping("/hello")
    private String hello()
    {
	return "success";
    }
}
