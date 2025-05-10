package com.powerledger.virtualpowerplant.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatteryFilterRequest {

    private String minPostcode;
    private String maxPostcode;
    private Integer minCapacity;
    private Integer maxCapacity;
}