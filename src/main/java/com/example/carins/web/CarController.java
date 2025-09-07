package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.InsClaim;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.service.CarService;
import com.example.carins.service.ClaimService;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.ClaimRequest;
import com.example.carins.web.dto.ClaimResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;
    private final ClaimService claimService;
    private final InsurancePolicyService insurancePolicyService;

    public CarController(CarService service, ClaimService claimService, InsurancePolicyService insurancePolicyService) {
        this.service = service;
        this.claimService = claimService;
        this.insurancePolicyService = insurancePolicyService;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    public record ErrorResponse(String error){}

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam String date) {
        if(carId == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("carId cant be null"));
        }
            LocalDate d;
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE.withResolverStyle(ResolverStyle.STRICT);
                d = LocalDate.parse(date, formatter);
                System.out.println("date: " + date);

            }
        catch(DateTimeParseException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("invalid date format, should be yyyy-MM-dd"));
        }

        boolean valid = service.isInsuranceValid(carId, d);  
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, d.toString(), valid));
    }

    @GetMapping("/cars/{carId}/history")
    public List<ClaimResponse> getClaims(@PathVariable Long carId){
        return claimService.listClaims().stream()
                .filter(claim -> claim.getCar().getId().equals(carId))
                .sorted(Comparator.comparing(InsClaim::getClaimDate))
                .map(insClaim ->
        new ClaimResponse(insClaim.getId(),
                insClaim.getCar().getId(),
                insClaim.getClaimDate().toString(),
                insClaim.getDescription(),
                insClaim.getAmount())
    )
                .toList();}

    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<?> newClaim(@PathVariable Long carId, @RequestBody ClaimRequest request){
        if(carId == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("carid cannot be null"));
        }

        LocalDate claimDate;
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US).withResolverStyle(ResolverStyle.STRICT);
                claimDate = LocalDate.parse(request.claimDate(), formatter);
                //System.out.println("date: " + claimDate);

            }
        catch(DateTimeParseException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("invalid date format, should be yyyy-MM-dd"));
        }

        InsClaim claim = service.registerClaim(carId, claimDate, request.description(), request.amount());

        ClaimResponse response = new ClaimResponse(
            claim.getId(),
            carId,
            claim.getClaimDate().toString(),
            claim.getDescription(),
            claim.getAmount()
    );

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .header("Location", "/api/cars/" + carId + "/claims/" + claim.getId())
            .body(response);
}

    
    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    @PostMapping("/cars/policy/create")
    public ResponseEntity<?> createPolicy(@RequestBody InsurancePolicy policy) {
        if(policy.getEndDate() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("end date cannot be null"));
        }

        return ResponseEntity.ok(insurancePolicyService.createOrUpdatePolicy(policy));
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {}
}
