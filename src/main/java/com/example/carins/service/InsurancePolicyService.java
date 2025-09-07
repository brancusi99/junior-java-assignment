package com.example.carins.service;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.springframework.stereotype.Service;

@Service
public class InsurancePolicyService {
    private final InsurancePolicyRepository insurancePolicyRepository;

    public InsurancePolicyService(InsurancePolicyRepository insurancePolicyRepository) {
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    public InsurancePolicy createOrUpdatePolicy(InsurancePolicy policy) {
        return insurancePolicyRepository.save(policy);
    }

}
