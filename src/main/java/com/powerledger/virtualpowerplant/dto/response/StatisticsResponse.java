package com.powerledger.virtualpowerplant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsResponse {
    private double totalCapacity;
    private double averageCapacity;
}