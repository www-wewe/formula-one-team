package org.muni.pa165.service;

import org.muni.pa165.api.RaceView;
import org.muni.pa165.data.domain.Race;

import java.util.List;

public interface RaceService {

    Race save(Race race);

    Race findById(Long id);

    List<Race> findAll();

    void deleteById(Long id);

    void deleteAll();

    void update(Race race);

    void assignCarOne(Long raceId, Long carId);

    void assignCarTwo(Long raceId, Long carId);

    List<Race> findByLocation_CountryOrLocation_CityOrLocation_Street(String country, String city, String street);

    List<Race> findByCarId(Long carId);

    List<RaceView> findAllWithCars();
}
