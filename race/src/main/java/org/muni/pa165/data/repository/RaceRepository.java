package org.muni.pa165.data.repository;

import org.muni.pa165.data.domain.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {

    List<Race> findByLocation_CountryOrLocation_CityOrLocation_Street(String country, String city, String street);

    @Query("SELECT r FROM Race r WHERE r.car1Id = :carId OR r.car2Id = :carId")
    List<Race> findCarById(@Param("carId") Long carId);
}
