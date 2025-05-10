package com.powerledger.virtualpowerplant.service;

import com.powerledger.virtualpowerplant.dto.request.BatteryRequest;
import com.powerledger.virtualpowerplant.dto.response.BatteryStatisticsResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface BatteryService {
    void registerBatteries(@Valid List<BatteryRequest> batteryRequests);

    BatteryStatisticsResponse getBatteryStatistics(String minPostcode, String maxPostcode, Optional<Integer> minCapacity, Optional<Integer> maxCapacity);
}
