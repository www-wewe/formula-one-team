package org.muni.pa165.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.muni.pa165.api.CarCreateViewDto;
import org.muni.pa165.api.CarViewDto;
import org.muni.pa165.data.domain.Car;
import org.muni.pa165.mapper.CarMapper;
import org.muni.pa165.service.CarService;
import org.muni.pa165.utils.TestCarFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarFacadeTest {

    @Mock
    private CarService carService;
    @Mock
    private CarMapper carMapper;
    @InjectMocks
    private CarFacade carFacade;
    private Car testCar;
    private CarViewDto testCarViewDto;
    private CarCreateViewDto testCarCreateViewDto;

    @BeforeEach
    void setUp() {
        testCar = TestCarFactory.getCarEntity();
        testCarViewDto = TestCarFactory.getCarViewDto();
        testCarCreateViewDto = TestCarFactory.getCarCreateViewDto();
    }

    @Test
    void findCarById_whenFound_returnsCarDto() {
        when(carService.findById(any(Long.class))).thenReturn(testCar);
        when(carMapper.toCarViewDto(any(Car.class))).thenReturn(testCarViewDto);

        CarViewDto foundDto = carFacade.findById(1L);

        assertThat(foundDto).as("Ensure the found car DTO matches the expected DTO for a given ID.").isEqualTo(testCarViewDto);
    }

    @Test
    void saveCar_savesAndReturnsCarDto() {
        when(carMapper.fromCarCreateViewDto(any(CarCreateViewDto.class))).thenReturn(testCar);
        when(carService.save(any(Car.class))).thenReturn(testCar);
        when(carMapper.toCarViewDto(any(Car.class))).thenReturn(testCarViewDto);

        CarViewDto savedDto = carFacade.create(testCarCreateViewDto);

        assertThat(savedDto).as("Verify the saved car DTO is equivalent to the expected DTO after saving.").isEqualTo(testCarViewDto);
        verify(carService, times(1)).save(testCar);
    }

    @Test
    void deleteCarById_carDeleted_invokesDeletion() {
        carFacade.delete(1L);

        verify(carService, times(1)).delete(1L);
    }

    @Test
    void deleteAllCars_carsDeleted_invokesDeletion() {
        carFacade.deleteAll();

        verify(carService, times(1)).deleteAll();
    }

    @Test
    void findAllCars_carsFound_returnsCarsList() {
        when(carService.findAll()).thenReturn(TestCarFactory.getListOfCarEntities());
        when(carMapper.toCarViewDto(any())).thenReturn(testCarViewDto);

        List<CarViewDto> foundDtos = carFacade.findAll();

        assertThat(foundDtos).as("Verify that the list of found car DTOs matches the expected list from TestDataFactory").isEqualTo(TestCarFactory.getListOfCarViewDto());
    }

    @Test
    void updateCar_carUpdated_invokesUpdate() {
        carFacade.update(testCar);

        verify(carService, times(1)).update(testCar);
    }

    @Test
    void findCarsByCarMake_whenCarsFound_returnsCarList() {
        List<Car> carList = List.of(testCar);
        when(carService.findByCarMake("Toyota")).thenReturn(carList);
        when(carMapper.toCarViewDto(any(Car.class))).thenReturn(testCarViewDto);

        List<CarViewDto> result = carFacade.findByCarMake("Toyota");

        assertThat(result).as("Ensure the result contains the expected car DTOs when cars are found by make").contains(testCarViewDto);
        assertEquals(1, result.size(), "Check that the correct number of car DTOs is returned.");
    }

    @Test
    void findCarsByCarMake_whenNoCarsFound_returnsEmptyList() {
        when(carService.findByCarMake("Tesla")).thenReturn(List.of());
        List<CarViewDto> result = carFacade.findByCarMake("Tesla");

        assertThat(result).as("Verify that an empty list is returned when no cars are found by make").isEmpty();
    }

    @Test
    void findCarsByMainDriver_whenCarsFound_returnsCarList() {
        List<Car> carList = List.of(testCar);
        when(carService.findByMainDriver(1L)).thenReturn(carList);
        when(carMapper.toCarViewDto(any(Car.class))).thenReturn(testCarViewDto);

        List<CarViewDto> result = carFacade.findByMainDriver(1L);

        assertThat(result).as("Ensure the result contains the expected car DTOs when cars are found by main driver ID").contains(testCarViewDto);
        assertEquals(1, result.size(), "Check that the correct number of car DTOs is returned.");
    }

    @Test
    void findCarsByMainDriver_whenNoCarsFound_returnsEmptyList() {
        when(carService.findByMainDriver(2L)).thenReturn(List.of());
        List<CarViewDto> result = carFacade.findByMainDriver(2L);

        assertThat(result).as("Verify that an empty list is returned when no cars are found by main driver ID").isEmpty();
    }

    @Test
    void updateCar_whenCarExists_updatesSuccessfully() {
        carFacade.update(testCar);
        verify(carService).update(testCar);
    }
}
