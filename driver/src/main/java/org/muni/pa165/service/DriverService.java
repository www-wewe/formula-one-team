package org.muni.pa165.service;

import org.muni.pa165.data.domain.Driver;
import org.muni.pa165.data.domain.DriverPerk;

import java.util.List;

public interface DriverService {

    Driver findById(Long id);

    void deleteById(Long id);

    void deleteAll();

    Driver save(Driver driver);

    void update(Driver driver);

    List<Driver> findAll();

    List<Driver> findByPerk(DriverPerk perk);

    List<Driver> findByNationality(String nationality);
}
