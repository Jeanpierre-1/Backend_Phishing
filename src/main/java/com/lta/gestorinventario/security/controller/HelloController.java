package com.lta.gestorinventario.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String index(){
        System.out.println("Hello en el hello controller");
        return "Hello desde hello controller";
    }

}
