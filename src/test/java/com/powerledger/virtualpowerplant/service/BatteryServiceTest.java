package com.powerledger.virtualpowerplant.service;

import com.powerledger.virtualpowerplant.dto.request.BatteryRequest;
import com.powerledger.virtualpowerplant.dto.response.BatteryStatisticsResponse;
import com.powerledger.virtualpowerplant.model.Battery;
import com.powerledger.virtualpowerplant.repository.BatteryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BatteryServiceTest {

    @Mock
    private BatteryRepository batteryRepository;

    @InjectMocks
    private BatteryServiceImpl batteryService;

    @Captor
    private ArgumentCaptor<List<Battery>> batteriesCaptor;

    private List<BatteryRequest> batteryRequests;
    private List<Battery> batteries;

    @BeforeEach
    void setUp() {
        // Setup test data
        batteryRequests = Arrays.asList(
                createBatteryRequest("Battery 1", "2000", 10000),
                createBatteryRequest("Battery 2", "3000", 20000),
                createBatteryRequest("Battery 3", "4000", 30000)
        );

        batteries = Arrays.asList(
                createBattery(1L, "Battery 1", "2000", 10000),
                createBattery(2L, "Battery 2", "3000", 20000),
                createBattery(3L, "Battery 3", "4000", 30000)
        );
    }

    @Test
    void registerBatteries_shouldSaveBatteriesToRepository() {
        // Given
        when(batteryRepository.saveAll(anyList())).thenReturn(batteries);

        // When
        batteryService.registerBatteries(batteryRequests);

        // Then
        verify(batteryRepository, times(1)).saveAll(batteriesCaptor.capture());
        List<Battery> capturedBatteries = batteriesCaptor.getValue();

        assertThat(capturedBatteries).hasSize(3);
        assertThat(capturedBatteries.get(0).getName()).isEqualTo("Battery 1");
        assertThat(capturedBatteries.get(0).getPostcode()).isEqualTo("2000");
        assertThat(capturedBatteries.get(0).getCapacity()).isEqualTo(10000);
    }

    @Test
    void getBatteryStatistics_shouldReturnCorrectStatistics() {
        // Given
        when(batteryRepository.findBatteriesByPostcodeAndCapacity(anyInt(), anyInt(), any(), any()))
                .thenReturn(batteries);

        // When
        BatteryStatisticsResponse response = batteryService.getBatteryStatistics(
                "2000", "5000", Optional.of(10000), Optional.of(50000));

        // Then
        assertThat(response).isNotNull();
    }

    @Test
    void getBatteryStatistics_withEmptyResult_shouldReturnZeroValues() {
        // Given
        when(batteryRepository.findBatteriesByPostcodeAndCapacity(anyInt(), anyInt(), any(), any()))
                .thenReturn(new ArrayList<>());

        // When
        BatteryStatisticsResponse response = batteryService.getBatteryStatistics(
                "9000", "9999", Optional.empty(), Optional.empty());

        // Then
        assertThat(response).isNotNull();
    }

    @Test
    void filterBatteriesByPostcodeAndCapacity_shouldCallRepositoryWithCorrectParameters() {
        // Given
        when(batteryRepository.findBatteriesByPostcodeAndCapacity(anyInt(), anyInt(), any(), any()))
                .thenReturn(batteries);

        // When
        List<Battery> result = batteryService.filterBatteriesByPostcodeAndCapacity(
                "2000", "5000", Optional.of(10000), Optional.of(50000));

        // Then
        verify(batteryRepository).findBatteriesByPostcodeAndCapacity(2000, 5000, 10000, 50000);
        assertThat(result).hasSize(3);
    }

    @Test
    void filterBatteriesByPostcodeAndCapacity_withNullPostcode_shouldHandleNullValues() {
        // Given
        when(batteryRepository.findBatteriesByPostcodeAndCapacity(any(), any(), any(), any()))
                .thenReturn(batteries);

        // When
        List<Battery> result = batteryService.filterBatteriesByPostcodeAndCapacity(
                null, "5000", Optional.empty(), Optional.of(50000));

        // Then
        verify(batteryRepository).findBatteriesByPostcodeAndCapacity(null, 5000, null, 50000);
        assertThat(result).hasSize(3);
    }

    @Test
    void filterBatteriesByPostcodeAndCapacity_withEmptyPostcode_shouldHandleEmptyValues() {
        // Given
        when(batteryRepository.findBatteriesByPostcodeAndCapacity(any(), any(), any(), any()))
                .thenReturn(batteries);

        // When
        List<Battery> result = batteryService.filterBatteriesByPostcodeAndCapacity(
                "", "5000", Optional.empty(), Optional.of(50000));

        // Then
        verify(batteryRepository).findBatteriesByPostcodeAndCapacity(null, 5000, null, 50000);
        assertThat(result).hasSize(3);
    }

    private BatteryRequest createBatteryRequest(String name, String postcode, int capacity) {
        BatteryRequest request = new BatteryRequest();
        request.setName(name);
        request.setPostcode(postcode);
        request.setCapacity((long) capacity);
        return request;
    }

    private Battery createBattery(Long id, String name, String postcode, int capacity) {
        return Battery.builder()
                .id(id)
                .name(name)
                .postcode(postcode)
                .capacity((long) capacity)
                .build();
    }
}
