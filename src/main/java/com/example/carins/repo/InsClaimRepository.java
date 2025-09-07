package com.example.carins.repo;

import com.example.carins.model.InsClaim;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface InsClaimRepository extends JpaRepository<InsClaim, Long> {}

