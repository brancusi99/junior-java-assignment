package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;

    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    //@JsonFormat(pattern = "yyyy/MM/dd")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam String date) {
        if(carId == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
            LocalDate d;
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withResolverStyle(ResolverStyle.STRICT);
                d = LocalDate.parse(date, formatter);
        // TODO: validate date format and handle errors consistently
        }
        catch(DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        boolean valid = service.isInsuranceValid(carId, d);  
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, d.toString(), valid));
    }


    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {}
}
