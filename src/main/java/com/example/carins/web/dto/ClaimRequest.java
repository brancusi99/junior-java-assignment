package com.example.carins.web.dto;

public record ClaimRequest(
        String claimDate, // expect yyyy-MM-dd
        String description,
        Double amount
) 
{}
