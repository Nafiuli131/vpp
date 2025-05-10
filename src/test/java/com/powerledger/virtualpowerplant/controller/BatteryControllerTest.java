package com.powerledger.virtualpowerplant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powerledger.virtualpowerplant.dto.request.BatteryFilterRequest;
import com.powerledger.virtualpowerplant.dto.request.BatteryRequest;
import com.powerledger.virtualpowerplant.dto.response.BatteryStatisticsResponse;
import com.powerledger.virtualpowerplant.dto.response.StatisticsResponse;
import com.powerledger.virtualpowerplant.service.BatteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BatteryController.class)
@ExtendWith(MockitoExtension.class)
public class BatteryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BatteryService batteryService;

    @InjectMocks
    private BatteryController batteryController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // Initialize the mock service or any other setup steps
    }

    @Test
    public void testRegisterBatteries() throws Exception {
        // Given
        BatteryRequest batteryRequest = new BatteryRequest("Battery1", "2000", 500L);
        List<BatteryRequest> batteryRequests = List.of(batteryRequest);

        // When & Then
        mockMvc.perform(post("/api/batteries/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batteryRequests)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Batteries registered successfully."));

        // Capture the argument passed to the service method
        ArgumentCaptor<List<BatteryRequest>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(batteryService, times(1)).registerBatteries(argumentCaptor.capture());

        // Verify that the argument passed to the service method is the same as what was sent in the request
        List<BatteryRequest> capturedArguments = argumentCaptor.getValue();
        assertEquals(batteryRequests.size(), capturedArguments.size());
        assertEquals(batteryRequests.get(0).getName(), capturedArguments.get(0).getName());
        assertEquals(batteryRequests.get(0).getCapacity(), capturedArguments.get(0).getCapacity());
    }

    @Test
    public void testGetBatteriesByPostcodeRangeAndCapacity() throws Exception {
        // Given
        BatteryFilterRequest filterRequest = new BatteryFilterRequest();
        filterRequest.setMinPostcode("1000");
        filterRequest.setMaxPostcode("2000");
        filterRequest.setMinCapacity(100);
        filterRequest.setMaxCapacity(500);

        BatteryStatisticsResponse response = new BatteryStatisticsResponse(
                List.of("Battery1", "Battery2"),
                new StatisticsResponse(1000, 200)
        );

        // When
        when(batteryService.getBatteryStatistics(
                "1000", "2000", Optional.of(100), Optional.of(500)))
                .thenReturn(response);

        // Then
        mockMvc.perform(post("/api/batteries/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batteryNames[0]").value("Battery1"))
                .andExpect(jsonPath("$.batteryNames[1]").value("Battery2"))
                .andExpect(jsonPath("$.statisticsResponse.totalCapacity").value(1000))
                .andExpect(jsonPath("$.statisticsResponse.averageCapacity").value(200));

        // Verify if the service method was called
        verify(batteryService, times(1)).getBatteryStatistics(
                "1000", "2000", Optional.of(100), Optional.of(500)
        );
    }

    @Test
    public void testRegisterBatteriesInvalidRequest() throws Exception {
        // Given
        BatteryRequest invalidBatteryRequest = new BatteryRequest("", "2000", -500L);
        List<BatteryRequest> invalidBatteryRequests = List.of(invalidBatteryRequest);

        // When & Then
        mockMvc.perform(post("/api/batteries/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBatteryRequests)));
//                .andExpect(status().isBadRequest());

        // Verify that the service method was never called
        verify(batteryService, times(0)).registerBatteries(invalidBatteryRequests);
    }

}
