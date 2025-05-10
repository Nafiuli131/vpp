package com.powerledger.virtualpowerplant.repository;

import com.powerledger.virtualpowerplant.model.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {
    @Query("SELECT b FROM Battery b WHERE " +
            "(CAST(b.postcode AS integer) >= :minPostcode AND CAST(b.postcode AS integer) <= :maxPostcode) " +
            "AND (:minCapacity IS NULL OR b.capacity >= :minCapacity) " +
            "AND (:maxCapacity IS NULL OR b.capacity <= :maxCapacity)")
    List<Battery> findBatteriesByPostcodeAndCapacity(
            Integer minPostcode,
            Integer maxPostcode,
            Integer minCapacity,
            Integer maxCapacity
    );
}