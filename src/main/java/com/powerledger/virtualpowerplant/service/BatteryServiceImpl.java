package com.powerledger.virtualpowerplant.service;

import com.powerledger.virtualpowerplant.dto.request.BatteryRequest;
import com.powerledger.virtualpowerplant.dto.response.BatteryStatisticsResponse;
import com.powerledger.virtualpowerplant.dto.response.StatisticsResponse;
import com.powerledger.virtualpowerplant.model.Battery;
import com.powerledger.virtualpowerplant.repository.BatteryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatteryServiceImpl implements BatteryService {

    private final BatteryRepository batteryRepository;

    @Transactional
    public void registerBatteries(List<BatteryRequest> requests) {
        List<Battery> batteries = requests.stream()
                .map(req -> Battery.builder()
                        .name(req.getName())
                        .postcode(req.getPostcode())
                        .capacity(req.getCapacity())
                        .build())
                .collect(Collectors.toList());

        log.debug("Saving batteries: {}", batteries);
        batteryRepository.saveAll(batteries);
    }

    public BatteryStatisticsResponse getBatteryStatistics(
            String minPostcode,
            String maxPostcode,
            Optional<Integer> minCapacity,
            Optional<Integer> maxCapacity) {

        log.info("Filtering batteries with minPostcode={}, maxPostcode={}, minCapacity={}, maxCapacity={}",
                minPostcode, maxPostcode, minCapacity.orElse(null), maxCapacity.orElse(null));

        // Call the repository to fetch the filtered batteries
        List<Battery> filteredBatteries = filterBatteriesByPostcodeAndCapacity(
                minPostcode, maxPostcode, minCapacity, maxCapacity
        );

        log.debug("Filtered batteries size: {}", filteredBatteries.size());

        if (filteredBatteries.isEmpty()) {
            log.warn("No batteries found matching the filter criteria.");
        }

        // Create the sorted battery names and calculate statistics
        List<String> sortedBatteryNames = filteredBatteries.stream()
                .map(Battery::getName)
                .sorted()
                .collect(Collectors.toList());

        double totalCapacity = filteredBatteries.stream()
                .mapToDouble(Battery::getCapacity)
                .sum();

        double averageCapacity = filteredBatteries.isEmpty() ? 0 : totalCapacity / filteredBatteries.size();

        // Return the final response
        return new BatteryStatisticsResponse(
                sortedBatteryNames,
                new StatisticsResponse(totalCapacity, averageCapacity)
        );
    }

    public List<Battery> filterBatteriesByPostcodeAndCapacity(
            String minPostcode,
            String maxPostcode,
            Optional<Integer> minCapacity,
            Optional<Integer> maxCapacity) {

        // Convert String to Integer for postcodes
        Integer minPostcodeInt = minPostcode != null && !minPostcode.isEmpty() ? Integer.parseInt(minPostcode) : null;
        Integer maxPostcodeInt = maxPostcode != null && !maxPostcode.isEmpty() ? Integer.parseInt(maxPostcode) : null;

        // Use the repository to fetch filtered batteries
        return batteryRepository.findBatteriesByPostcodeAndCapacity(
                minPostcodeInt,
                maxPostcodeInt,
                minCapacity.orElse(null),
                maxCapacity.orElse(null)
        );
    }

}
