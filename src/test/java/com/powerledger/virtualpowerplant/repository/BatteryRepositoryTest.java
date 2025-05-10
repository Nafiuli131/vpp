package com.powerledger.virtualpowerplant.repository;


import com.powerledger.virtualpowerplant.model.Battery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class BatteryRepositoryTest {

    @Autowired
    private BatteryRepository batteryRepository;

    @BeforeEach
    public void setup() {
        batteryRepository.deleteAll();

        // Create test batteries with different postcodes and capacities
        Battery battery1 = new Battery();
        battery1.setName("Battery 1");
        battery1.setPostcode("1000");
        battery1.setCapacity(10000L);

        Battery battery2 = new Battery();
        battery2.setName("Battery 2");
        battery2.setPostcode("2000");
        battery2.setCapacity(20000L);

        Battery battery3 = new Battery();
        battery3.setName("Battery 3");
        battery3.setPostcode("3000");
        battery3.setCapacity(30000L);

        Battery battery4 = new Battery();
        battery4.setName("Battery 4");
        battery4.setPostcode("4000");
        battery4.setCapacity(40000L);

        Battery battery5 = new Battery();
        battery5.setName("Battery 5");
        battery5.setPostcode("5000");
        battery5.setCapacity(50000L);

        batteryRepository.saveAll(Arrays.asList(battery1, battery2, battery3, battery4, battery5));
    }

    @Test
    public void testFindAll() {
        List<Battery> batteries = batteryRepository.findAll();
        assertThat(batteries).isNotEmpty();
        assertThat(batteries).hasSize(5);
    }

    @Test
    public void testFindById() {
        List<Battery> batteries = batteryRepository.findAll();
        Battery firstBattery = batteries.get(0);

        Battery foundBattery = batteryRepository.findById(firstBattery.getId()).orElse(null);
        assertThat(foundBattery).isNotNull();
        assertThat(foundBattery.getName()).isEqualTo(firstBattery.getName());
        assertThat(foundBattery.getPostcode()).isEqualTo(firstBattery.getPostcode());
        assertThat(foundBattery.getCapacity()).isEqualTo(firstBattery.getCapacity());
    }

    @Test
    public void testFindBatteriesByPostcodeRange() {
        // Find batteries with postcodes between 2000 and 4000
        List<Battery> batteries = batteryRepository.findBatteriesByPostcodeAndCapacity(2000, 4000, null, null);

        assertThat(batteries).hasSize(3);
        assertThat(batteries.stream().map(Battery::getPostcode))
                .containsExactlyInAnyOrder("2000", "3000", "4000");
    }

    @Test
    public void testFindBatteriesByCapacityRange() {
        // Find batteries with capacity between 20000 and 40000
        List<Battery> batteries = batteryRepository.findBatteriesByPostcodeAndCapacity(1000, 5000, 20000, 40000);

        assertThat(batteries).hasSize(3);
        assertThat(batteries.stream().map(Battery::getName))
                .containsExactlyInAnyOrder("Battery 2", "Battery 3", "Battery 4");
    }

    @Test
    public void testFindBatteriesByPostcodeAndCapacityRange() {
        // Find batteries with postcodes between 3000 and 5000 and capacity between 30000 and 50000
        List<Battery> batteries = batteryRepository.findBatteriesByPostcodeAndCapacity(3000, 5000, 30000, 50000);

        assertThat(batteries).hasSize(3);
        assertThat(batteries.stream().map(Battery::getName))
                .containsExactlyInAnyOrder("Battery 3", "Battery 4", "Battery 5");
    }

    @Test
    public void testFindBatteriesWithMinCapacityOnly() {
        // Find batteries with capacity of at least 30000
        List<Battery> batteries = batteryRepository.findBatteriesByPostcodeAndCapacity(1000, 5000, 30000, null);

        assertThat(batteries).hasSize(3);
        assertThat(batteries.stream().map(Battery::getName))
                .containsExactlyInAnyOrder("Battery 3", "Battery 4", "Battery 5");
    }

    @Test
    public void testFindBatteriesWithMaxCapacityOnly() {
        // Find batteries with capacity up to 30000
        List<Battery> batteries = batteryRepository.findBatteriesByPostcodeAndCapacity(1000, 5000, null, 30000);

        assertThat(batteries).hasSize(3);
        assertThat(batteries.stream().map(Battery::getName))
                .containsExactlyInAnyOrder("Battery 1", "Battery 2", "Battery 3");
    }

    @Test
    public void testNoMatchingBatteries() {
        // No batteries in postcode range 6000-7000
        List<Battery> batteries = batteryRepository.findBatteriesByPostcodeAndCapacity(6000, 7000, null, null);
        assertThat(batteries).isEmpty();

        // No batteries with capacity over 60000
        batteries = batteryRepository.findBatteriesByPostcodeAndCapacity(1000, 5000, 60000, null);
        assertThat(batteries).isEmpty();
    }
}