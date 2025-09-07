package com.example.carins.web.dto;

public record ClaimResponse(
        Long id,
        Long carId,
        String claimDate,
        String description,
        Double amount
) 
{}
