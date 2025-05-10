package com.powerledger.virtualpowerplant.controller;

import com.powerledger.virtualpowerplant.dto.request.BatteryFilterRequest;
import com.powerledger.virtualpowerplant.dto.request.BatteryRequest;
import com.powerledger.virtualpowerplant.dto.response.BatteryStatisticsResponse;
import com.powerledger.virtualpowerplant.service.BatteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/batteries")
@RequiredArgsConstructor
@Validated
public class BatteryController {

    private final BatteryService batteryService;

    @PostMapping("/register")
    @Operation(summary = "Register new batteries", description = "Registers a list of batteries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Batteries registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<String> registerBatteries(@RequestBody @Valid List<BatteryRequest> batteryRequests) {
        log.info("Registering {} batteries", batteryRequests.size());
        batteryService.registerBatteries(batteryRequests);
        log.info("Batteries registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body("Batteries registered successfully.");
    }

    @PostMapping("/filter")
    @Operation(summary = "Filter batteries by postcode and capacity", description = "Filters batteries based on postcode range and capacity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved battery data"),
            @ApiResponse(responseCode = "400", description = "Invalid filter request")
    })
    public ResponseEntity<BatteryStatisticsResponse> getBatteriesByPostcodeRangeAndCapacity(
            @RequestBody @Valid BatteryFilterRequest filterRequest) {

        log.info("Filtering batteries with request: {}", filterRequest);

        // Delegate the filtering logic to the service layer
        BatteryStatisticsResponse response = batteryService.getBatteryStatistics(
                filterRequest.getMinPostcode(),
                filterRequest.getMaxPostcode(),
                Optional.ofNullable(filterRequest.getMinCapacity()),
                Optional.ofNullable(filterRequest.getMaxCapacity())
        );

        return ResponseEntity.ok(response);
    }

}
