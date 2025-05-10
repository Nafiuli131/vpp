package com.powerledger.virtualpowerplant.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powerledger.virtualpowerplant.model.Battery;
import com.powerledger.virtualpowerplant.repository.BatteryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private BatteryRepository batteryRepository;  

    private static final String INITIAL_DATA_FILE = "src/main/resources/test-data May 2025.json";

    @Override
    public void run(String... args) throws Exception {
        // Check if the data is already present, to avoid inserting duplicates
        if (batteryRepository.count() == 0) {
            // Read the JSON file and map it to a list of Battery objects
            List<Battery> batteries = readDataFromJson(INITIAL_DATA_FILE);

            // Insert the data into the database
            batteryRepository.saveAll(batteries);
        }
    }

    private List<Battery> readDataFromJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Read and convert JSON into a list of Battery objects
        return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, Battery.class));
    }
}
