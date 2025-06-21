package org.muni.pa165.data.repository;

import org.muni.pa165.data.domain.Driver;
import org.muni.pa165.data.domain.DriverPerk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByPerk(DriverPerk perk);

    List<Driver> findByNationality(String nationality);
}
