package com.example.carins.web;

import com.example.carins.service.InsurancePolicyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.example.carins.service.InsurancePolicyService;

@RestController
@RequestMapping("/api")
public class InsurancePolicyController {

    private final InsurancePolicyService policyService;

    @PostMapping("/cars")

    
}
