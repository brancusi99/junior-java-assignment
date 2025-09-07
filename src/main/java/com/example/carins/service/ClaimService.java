package com.example.carins.service;

import com.example.carins.model.InsClaim;
import com.example.carins.repo.InsClaimRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimService {
    private final InsClaimRepository claimRepository;

    public ClaimService(InsClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public List<InsClaim> listClaims(){return claimRepository.findAll();}
}
