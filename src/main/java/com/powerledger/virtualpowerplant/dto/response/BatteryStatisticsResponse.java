package com.powerledger.virtualpowerplant.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class BatteryStatisticsResponse {
    private List<String> batteryNames;
    private StatisticsResponse statisticsResponse;

}
