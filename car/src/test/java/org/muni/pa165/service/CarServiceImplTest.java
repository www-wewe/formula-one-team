package org.muni.pa165.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.muni.pa165.data.domain.Car;
import org.muni.pa165.data.repository.CarRepository;
import org.muni.pa165.exceptions.CarNotFoundException;
import org.muni.pa165.exceptions.DataStorageException;
import org.muni.pa165.utils.TestCarFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private ExternalService externalService;

    @InjectMocks
    private CarServiceImpl carService;

    private Car testCar;

    @BeforeEach
    void setUp() {
        testCar = TestCarFactory.getCarEntity();
    }

    @Test
    void saveCar_savesAndReturnsCar() {
        when(carRepository.save(any(Car.class))).thenReturn(testCar);
        when(externalService.driverExists(any())).thenReturn(true);
        when(externalService.componentExists(any())).thenReturn(true);

        Car result = carService.save(testCar);

        verify(carRepository).save(testCar);
        assertThat(result).as("Ensure the car saved is the same as the car returned").isSameAs(testCar);
    }

    @Test
    void findById_whenFound_returnsCar() {
        Long carId = testCar.getId();
        when(carRepository.findById(carId)).thenReturn(Optional.ofNullable(testCar));

        Car result = carService.findById(carId);

        verify(carRepository).findById(carId);
        assertThat(result).as("Verify the car returned by getCarById matches the expected car").isSameAs(testCar);
    }

    @Test
    void findById_whenNotFound_throwsCarNotFoundException() {
        when(carRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carService.findById(1L))
                .isInstanceOf(CarNotFoundException.class);
    }

    @Test
    void deleteCarById_whenCarFound_deletesCar() {
        Long carId = testCar.getId();
        when(carRepository.existsById(carId)).thenReturn(true);

        carService.delete(carId);

        verify(carRepository).deleteById(carId);
    }

    @Test
    void deleteCarById_whenCarNotFound_ThrowsCarNotFoundException() {
        Long carId = testCar.getId();
        when(carRepository.existsById(carId)).thenReturn(false);

        assertThatThrownBy(() -> carService.delete(carId))
                .isInstanceOf(CarNotFoundException.class);
    }

    @Test
    void deleteAllCars_deletesAllCars() {
        carService.deleteAll();

        verify(carRepository).deleteAll();
    }

    @Test
    void updateCar_whenCarFound_updatesAndSavesCar() {
        when(carRepository.existsById(testCar.getId())).thenReturn(true);
        when(carRepository.save(any(Car.class))).thenReturn(testCar);
        when(externalService.driverExists(any())).thenReturn(true);
        when(externalService.componentExists(any())).thenReturn(true);

        carService.update(testCar);

        verify(carRepository).save(testCar);
    }

    @Test
    void updateCar_whenCarNotFound_throwsCarNotFoundException() {
        when(externalService.driverExists(any())).thenReturn(true);
        when(externalService.componentExists(any())).thenReturn(true);
        when(carRepository.existsById(testCar.getId())).thenReturn(false);

        assertThatThrownBy(() -> carService.update(testCar))
                .isInstanceOf(CarNotFoundException.class);
    }

    @Test
    void findAllCars_returnsAllCars() {
        List<Car> expectedCars = Arrays.asList(testCar, TestCarFactory.getCarEntity());
        when(carRepository.findAll()).thenReturn(expectedCars);

        List<Car> result = carService.findAll();

        verify(carRepository).findAll();
        assertThat(result).as("Check if the list of all cars matches the expected list").isEqualTo(expectedCars);
    }

    @Test
    void findCarsByCarMake_whenCarsFound_returnsCarList() {
        List<Car> expectedCars = List.of(testCar);
        when(carRepository.findByCarMake("Toyota")).thenReturn(expectedCars);

        List<Car> result = carService.findByCarMake("Toyota");

        verify(carRepository).findByCarMake("Toyota");
        assertThat(result).as("Verify that the list of cars found matches the expected list for make 'Toyota'").isEqualTo(expectedCars);
    }

    @Test
    void findCarsByCarMake_whenNoCarsFound_returnsEmptyList() {
        when(carRepository.findByCarMake("Tesla")).thenReturn(Collections.emptyList());

        List<Car> result = carService.findByCarMake("Tesla");

        verify(carRepository).findByCarMake("Tesla");
        assertThat(result).as("Verify that an empty list is returned when no cars are found by make 'Tesla'").isEmpty();
    }

    @Test
    void findCarsByMainDriver_whenCarsFound_returnsCarList() {
        List<Car> expectedCars = List.of(testCar);
        when(carRepository.findByMainDriver(1L)).thenReturn(expectedCars);

        List<Car> result = carService.findByMainDriver(1L);

        verify(carRepository).findByMainDriver(1L);
        assertThat(result).as("Verify that the list of cars found matches the expected list for main driver ID 1").isEqualTo(expectedCars);
    }

    @Test
    void findCarsByMainDriver_whenNoCarsFound_returnsEmptyList() {
        when(carRepository.findByMainDriver(2L)).thenReturn(Collections.emptyList());

        List<Car> result = carService.findByMainDriver(2L);

        verify(carRepository).findByMainDriver(2L);
        assertThat(result).as("Verify that an empty list is returned when no cars are found by main driver ID 2").isEmpty();
    }

    @Test
    void saveCar_withInvalidComponent_throwsDataStorageException() {
        when(externalService.componentExists(any())).thenReturn(false);

        assertThatThrownBy(() -> carService.save(testCar))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Component with id");
    }

    @Test
    void saveCar_withInvalidDriver_throwsDataStorageException() {
        Car carWithInvalidDriver = TestCarFactory.getCarEntity();
        when(externalService.componentExists(any())).thenReturn(true);
        when(externalService.driverExists(carWithInvalidDriver.getMainDriver())).thenReturn(false);

        assertThatThrownBy(() -> carService.save(carWithInvalidDriver))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Driver with id: " + carWithInvalidDriver.getMainDriver() + " does not exist.");

        verify(externalService, atLeastOnce()).componentExists(any());
        verify(externalService).driverExists(carWithInvalidDriver.getMainDriver());
    }

    @Test
    void saveCar_withNullCarMake_throwsDataStorageException() {
        testCar.setCarMake(null);
        when(externalService.componentExists(any())).thenReturn(true);
        when(externalService.driverExists(any())).thenReturn(true);

        assertThatThrownBy(() -> carService.save(testCar))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Car make cannot be null or empty");
    }

    @Test
    void updateCar_withInvalidComponent_throwsDataStorageException() {
        when(externalService.componentExists(any())).thenReturn(false);

        assertThatThrownBy(() -> carService.update(testCar))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Component with id");
    }

    @Test
    void updateCar_withInvalidDriver_throwsDataStorageException() {
        when(externalService.componentExists(any())).thenReturn(true);
        when(externalService.driverExists(any())).thenReturn(false);

        assertThatThrownBy(() -> carService.update(testCar))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Driver with id: " + testCar.getMainDriver() + " does not exist.");
    }

    @Test
    void updateCar_withNullCarMake_throwsDataStorageException() {
        testCar.setCarMake("");
        when(externalService.componentExists(any())).thenReturn(true);
        when(externalService.driverExists(any())).thenReturn(true);

        assertThatThrownBy(() -> carService.update(testCar))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Car make cannot be null or empty");
    }

    @Test
    void updateCar_withNullComponents_doesThrowValidationException() {
        testCar.setComponents(null);

        assertThatThrownBy(() -> carService.update(testCar))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Car must have at least one component.");
    }

    @Test
    void saveCar_withNonExistingTestDriver_throwsDataStorageException() {
        testCar.setTestDrivers(new HashSet<>(Arrays.asList(1L, 2L))); // Assuming 2L does not exist

        when(externalService.componentExists(any())).thenReturn(true);
        when(externalService.driverExists(1L)).thenReturn(true);
        when(externalService.driverExists(2L)).thenReturn(false); // Mock that driver 2L does not exist

        assertThatThrownBy(() -> carService.save(testCar))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Driver with id: 2 does not exist.");

        verify(externalService).driverExists(2L);
    }
}
