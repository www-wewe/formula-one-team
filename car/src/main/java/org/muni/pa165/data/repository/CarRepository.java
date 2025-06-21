package org.muni.pa165.data.repository;

import org.muni.pa165.data.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByCarMake(String carMake);

    List<Car> findByMainDriver(Long mainDriverId);
}
