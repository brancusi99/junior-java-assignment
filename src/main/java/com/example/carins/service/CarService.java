package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsClaim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;
    private final InsClaimRepository claimRepository;

    public CarService(CarRepository carRepository, InsurancePolicyRepository policyRepository, InsClaimRepository claimRepository) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
        this.claimRepository = claimRepository;
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public boolean isInsuranceValid(Long carId, LocalDate date) {
        if (carId == null || date == null) return false;
        // TODO: optionally throw NotFound if car does not exist
        return policyRepository.existsActiveOnDate(carId, date);
    }

    public InsClaim registerClaim(Long carId, LocalDate claimDate, String description, Double amount) {
        // TODO Auto-generated method stub
            Car car = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("car not found"));
        InsClaim claim = new InsClaim();
        claim.setCar(car);
        claim.setClaimDate(claimDate);
        claim.setDescription(description);
        claim.setAmount(amount);

        return claimRepository.save(claim);
    }
}
