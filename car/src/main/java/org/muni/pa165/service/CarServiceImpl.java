package org.muni.pa165.service;

import jakarta.transaction.Transactional;
import org.muni.pa165.data.domain.Car;
import org.muni.pa165.data.repository.CarRepository;
import org.muni.pa165.exceptions.CarNotFoundException;
import org.muni.pa165.exceptions.DataStorageException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ExternalService externalService;

    public CarServiceImpl(CarRepository carRepository, ExternalService externalService) {
        this.carRepository = carRepository;
        this.externalService = externalService;
    }

    @Override
    public void checkIfExists(Long id) {
        if (!carRepository.existsById(id)) {
            throw new CarNotFoundException("Car with id: " + id + " not found.");
        }
    }

    @Override
    public Car save(Car car) {
        verifyParameters(car);
        return carRepository.save(car);
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with id: " + id + " not found."));
    }

    @Override
    public void delete(Long id) {
        checkIfExists(id);
        carRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        carRepository.deleteAll();
    }

    @Override
    public void update(Car car) {
        verifyParameters(car);
        checkIfExists(car.getId());
        carRepository.save(car);
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> findByCarMake(String carMake) {
        return carRepository.findByCarMake(carMake);
    }

    @Override
    public List<Car> findByMainDriver(Long mainDriverId) {
        return carRepository.findByMainDriver(mainDriverId);
    }

    private void verifyParameters(Car car) {
        if (car.getComponents() == null || car.getComponents().isEmpty()) {
            throw new DataStorageException("Car must have at least one component.");
        }

        for (long id : car.getComponents()) {
            if (!externalService.componentExists(id)) {
                throw new DataStorageException("Component with id: " + id + " does not exist.");
            }
        }

        if (car.getMainDriver() != null) {
            if (!externalService.driverExists(car.getMainDriver())) {
                throw new DataStorageException("Driver with id: " + car.getMainDriver() + " does not exist.");
            }
        }

        if (car.getTestDrivers() != null) {
            for (long id : car.getTestDrivers()) {
                if (!externalService.driverExists(id)) {
                    throw new DataStorageException("Driver with id: " + id + " does not exist.");
                }
            }
        }

        if (car.getCarMake() == null || car.getCarMake().isEmpty()) {
            throw new DataStorageException("Car make cannot be null or empty.");
        }
    }
}
