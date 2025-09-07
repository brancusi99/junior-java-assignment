package com.example.carins.service;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
public class InsurancePolicyService {
    private final InsurancePolicyRepository insurancePolicyRepository;
    private static final Logger logger = Logger.getLogger(InsurancePolicyService.class.getName());

    public InsurancePolicyService(InsurancePolicyRepository insurancePolicyRepository) {
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    public InsurancePolicy createOrUpdatePolicy(InsurancePolicy policy) {
        return insurancePolicyRepository.save(policy);
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void checkExpiredPolicies() {
        LocalDate now = LocalDate.now();
        List<InsurancePolicy> expiredPolicies = insurancePolicyRepository.findExpiredPolicies(now);

        for (InsurancePolicy policy : expiredPolicies) {

            if (!policy.isLogged()) {
                logger.info(String.format("Policy %d for car %d expired on %s", policy.getId(), policy.getCar().getId(), policy.getEndDate()));
                policy.setLogged(true);
                insurancePolicyRepository.save(policy);
            }
        }
    }

}
