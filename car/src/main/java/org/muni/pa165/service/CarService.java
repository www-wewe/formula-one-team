package org.muni.pa165.service;

import org.muni.pa165.data.domain.Car;

import java.util.List;

public interface CarService {

    void checkIfExists(Long id);

    Car save(Car car);

    Car findById(Long id);

    void delete(Long id);

    void deleteAll();

    void update(Car car);

    List<Car> findAll();

    List<Car> findByCarMake(String carMake);

    List<Car> findByMainDriver(Long mainDriverId);
}
