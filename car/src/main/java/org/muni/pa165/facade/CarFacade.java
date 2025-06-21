package org.muni.pa165.facade;

import org.muni.pa165.api.CarCreateViewDto;
import org.muni.pa165.api.CarViewDto;
import org.muni.pa165.data.domain.Car;
import org.muni.pa165.mapper.CarMapper;
import org.muni.pa165.service.CarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarFacade {

    private final CarService carService;
    private final CarMapper carMapper;

    public CarFacade(CarService carService, CarMapper carMapper) {
        this.carService = carService;
        this.carMapper = carMapper;
    }

    public CarViewDto findById(Long id) {
        return carMapper.toCarViewDto(carService.findById(id));
    }

    public CarViewDto create(CarCreateViewDto carCreateViewDto) {
        return carMapper.toCarViewDto(carService.save(carMapper.fromCarCreateViewDto(carCreateViewDto)));
    }

    public void delete(Long id) {
        carService.delete(id);
    }

    public void deleteAll() {
        carService.deleteAll();
    }

    public List<CarViewDto> findAll() {
        return carService.findAll().stream()
                .map(carMapper::toCarViewDto)
                .toList();
    }

    public void update(Car car) {
        carService.update(car);
    }

    public List<CarViewDto> findByCarMake(String carMake) {
        return carService.findByCarMake(carMake).stream()
                .map(carMapper::toCarViewDto)
                .toList();
    }

    public List<CarViewDto> findByMainDriver(Long mainDriverId) {
        return carService.findByMainDriver(mainDriverId).stream()
                .map(carMapper::toCarViewDto)
                .toList();
    }
}
