package com.powerledger.virtualpowerplant.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatteryRequest {

    @NotBlank(message = "Battery name is required")
    private String name;

    @NotBlank(message = "Postcode is required")
    private String postcode;

    @NotNull(message = "Capacity is required")
    @Min(value = 0, message = "Capacity must be positive")
    private Long capacity;
}